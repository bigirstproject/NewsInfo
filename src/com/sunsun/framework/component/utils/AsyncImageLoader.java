package com.sunsun.framework.component.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.methods.RequestEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.sunsun.framework.component.debug.KGLog;
import com.sunsun.framework.http.AbstractRequestPackage;
import com.sunsun.framework.http.KGHttpClient;
import com.sunsun.framework.http.KGHttpClient.IDownloadListener;
import com.sunsun.framework.http.RequestPackage;

/**
 * 描述:异步加载图片
 * 
 */
public class AsyncImageLoader {

	private ThreadPoolExecutor mThreadPoolExecutor;

	// 最大bitmap缓存数
	private static final int MAX_CACHE_SIZE = 4;

	// 已加载好的图片缓存
	@SuppressWarnings("serial")
	private HashMap<String, SoftReference<Bitmap>> mBitmapCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
			MAX_CACHE_SIZE, 0.75f, true);

	// 图片下载任务队列
	private LinkedHashMap<String, ImageCallback> mImageLoadingTasks = new LinkedHashMap<String, ImageCallback>(
			0);

	// 被临时取消的任务
	// private ArrayBlockingQueue<Runnable> mCancelTasks = new
	// ArrayBlockingQueue<Runnable>(100);

	private RejectedExecutionHandler mRejectedExecutionHandler = new RejectedExecutionHandler() {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			if (!executor.isShutdown()) {
				final Runnable cancelRunnable = executor.getQueue().poll();
				if (cancelRunnable instanceof ThreadPoolTask) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							mImageLoadingTasks
									.remove(((ThreadPoolTask) cancelRunnable).key);
							KGLog.d("111", "---rejected task.");
						}

					});

					// if (cancelRunnable != null &&
					// !mCancelTasks.contains(cancelRunnable)) {
					// mCancelTasks.add(cancelRunnable);
					// }
					// executor.execute(r);
				}
			}
		}
	};

	// 图片下载完毕后刷新UI
	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			Bitmap bitmap = message.obj == null ? null : (Bitmap) message.obj;
			String key = message.getData().getString("key");
			ImageCallback imageCallback = mImageLoadingTasks.get(key);
			if (imageCallback != null) {
				imageCallback.imageLoaded(bitmap, key);
			}
			mImageLoadingTasks.remove(key);
		}
	};

	public AsyncImageLoader() {
		mThreadPoolExecutor = new ThreadPoolExecutor(2, 3, 3, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(30), mRejectedExecutionHandler);
	}

	/**
	 * 加载图片
	 * 
	 * @param context
	 * @param key
	 * @param imageurl
	 * @param savePath
	 * @param imageCallback
	 * @return
	 */
	public Bitmap loadImage(Hashtable<String, String> headers, Context context,
			String key, String imageurl, String savePath,
			ImageCallback imageCallback) {
		return loadImage(headers, context, key, imageurl, savePath,
				imageCallback, -1, -1);
	}

	/**
	 * 加载图片
	 * 
	 * @param context
	 * @param key
	 * @param imageurl
	 * @param savePath
	 * @param imageCallback
	 * @param width
	 *            显示宽度 -1时不裁剪
	 * @param height
	 *            显示高度 -1时不裁剪
	 * @return
	 */
	public Bitmap loadImage(Hashtable<String, String> headers, Context context,
			String key, String imageurl, String savePath,
			ImageCallback imageCallback, int width, int height) {
		if (TextUtils.isEmpty(key)) {
			return null;
		}

		// 先检查是否有缓存
		Bitmap bitmap = getBitmapFromCache(imageurl);
		if (bitmap != null) {
			return bitmap;
		}

		if (TextUtils.isEmpty(savePath)) {
			return null;
		}
		if ((FileUtil.getSDFreeSize() < 20)
				|| (FileUtil.getmem_UNUSED(context) < 10)) {
			Toast.makeText(context, "SD卡空间已满，无法加载更多内容", Toast.LENGTH_LONG)
					.show();
			return null;
		}

		// 开始下载
		if (!mImageLoadingTasks.containsKey(key)) {
			addToDownloadPool(headers, key, imageurl, savePath, imageCallback,
					width, height);
		} else {
			KGLog.d("111", "loading:" + key);
		}

		return null;
	}

	private synchronized void addToDownloadPool(
			Hashtable<String, String> headers, String key, String imageurl,
			String savePath, ImageCallback imageCallback, int width, int height) {
		if (TextUtils.isEmpty(key)) {
			return;
		}
		mImageLoadingTasks.put(key, imageCallback);
		ThreadPoolTask threadPoolTask = new ThreadPoolTask(headers, key,
				imageurl, savePath, width, height);
		mThreadPoolExecutor.execute(threadPoolTask);
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that
	 * for memory efficiency reasons, the cache will automatically be cleared
	 * after a certain inactivity delay.
	 */
	public void clearCache() {
		// mThreadPoolExecutor.shutdownNow();
		mBitmapCache.clear();
		// mImageLoadingTasks.clear();
	}

	private class ThreadPoolTask implements Runnable {

		String key;

		String imageurl;

		String savePath;

		int width;

		int height;

		Hashtable<String, String> headers;

		public ThreadPoolTask(Hashtable<String, String> headers, String key,
				String imageurl, String savePath, int width, int height) {
			this.key = key;
			this.imageurl = imageurl;
			this.savePath = savePath;
			this.width = width;
			this.height = height;
			this.headers = headers;
		}

		@Override
		public void run() {
			// 检查本地图片是否缓存
			File file = new File(savePath);
			if (file.exists() && file.length() > 0) {
				Bitmap bitmap;
				if (width > 0 && height > 0) {
					bitmap = BitmapUtil.decodeFile(savePath, width, height);
				} else {
					bitmap = ImageUtil.readBitmap(savePath);
				}
				if (bitmap != null) {
					KGLog.d("111", "getBitmapFromSdcard.");
					addBitmapToCache(imageurl, bitmap);
					// 刷新UI
					refreshUi(key, bitmap);
					return;
				}
			}

			// 下载图片
			Bitmap bitmap = loadImageFromUrl(headers, imageurl, savePath,
					width, height);
			if (bitmap != null) {
				addBitmapToCache(imageurl, bitmap);
				// 刷新UI
				refreshUi(key, bitmap);
				KGLog.d("111", "loadImageFromUrl success.");
			} else {
				handler.post(new Runnable() {

					@Override
					public void run() {
						mImageLoadingTasks.remove(key);
						KGLog.d("111", "loadImageFromUrl faile.");
					}

				});
			}

			// if (!mThreadPoolExecutor.isShutdown()) {
			// if (mThreadPoolExecutor.getQueue().size() == 0 &&
			// mCancelTasks.size() > 0) {
			// mThreadPoolExecutor.execute(mCancelTasks.poll());
			// }
			// }
		}
	}

	private void refreshUi(String key, Bitmap bitmap) {
		Message message = handler.obtainMessage();
		message.obj = bitmap;
		Bundle bundle = new Bundle();
		bundle.putString("key", key);
		message.setData(bundle);
		handler.sendMessage(message);
	}

	private Bitmap getBitmapFromCache(String imageurl) {
		synchronized (mBitmapCache) {
			final SoftReference<Bitmap> softBitmap = mBitmapCache.get(imageurl);
			if (softBitmap != null) {
				Bitmap bitmap = softBitmap.get();
				if (bitmap != null && !bitmap.isRecycled()) {
					KGLog.d("111", "getBitmapFromHardCache.");
					// Bitmap found in hard cache
					// Move element to first position, so that it is removed
					// last
					mBitmapCache.remove(imageurl);
					mBitmapCache.put(imageurl, softBitmap);
					return bitmap;
				} else {
					mBitmapCache.remove(imageurl);
				}
			}
		}

		return null;
	}

	private void addBitmapToCache(String imageurl, Bitmap bitmap) {
		synchronized (mBitmapCache) {
			mBitmapCache.put(imageurl, new SoftReference<Bitmap>(bitmap));
		}
	}

	/**
	 * 从网络下载图片，下载成功保存在SD卡
	 * 
	 * @param url
	 * @param savePath
	 * @param width
	 *            -1时不裁剪
	 * @param height
	 *            -1时不裁剪
	 * @return
	 */
	public static Bitmap loadImageFromUrl(Hashtable<String, String> headers,
			String url, String savePath, int width, int height) {
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(savePath)) {
			return null;
		}
		String savePathTemp = savePath + ".tmp";
		ImageRequestPackage req = new ImageRequestPackage(url, headers);
		ImageDownloadListener listener = new ImageDownloadListener(savePathTemp);
		try {
			KGHttpClient.download(req, listener);
		} catch (Exception e) {
			e.printStackTrace();
			FileUtil.deleteFile(savePathTemp);
			return null;
		}
		FileUtil.rename(savePathTemp, savePath);
		Bitmap bitmap;
		if (width > 0 && height > 0) {
			bitmap = BitmapUtil.decodeFile(savePath, width, height);
		} else {
			bitmap = BitmapUtil.decodeFile(savePath);
		}
		return bitmap;
	}

	public interface ImageCallback {

		public void imageLoaded(Bitmap bitmap, String key);
	}

	static class ImageRequestPackage extends AbstractRequestPackage {

		private String reqUrl;

		private Hashtable<String, String> headers;

		public ImageRequestPackage(String url, Hashtable<String, String> headers) {
			this.reqUrl = url;
			this.headers = headers;
		}

		@Override
		public String getGetRequestParams() {
			return "";
		}

		@Override
		public RequestEntity getPostRequestEntity() {
			return null;
		}

		@Override
		public String getUrl() {
			return reqUrl;
		}

		@Override
		public int getRequestType() {
			return RequestPackage.TYPE_GET;
		}

		@Override
		public Hashtable<String, String> getRequestHeaders() {
			return headers;
		}

	}

	static class ImageDownloadListener implements IDownloadListener {

		private String savePath;

		private FileOutputStream fos;

		ImageDownloadListener(String savePath) {
			this.savePath = savePath;
		}

		@Override
		public void onProgressChanged(byte[] data, int offset, int length,
				int progress) {
			try {
				if (fos == null) {
					File file = new File(savePath);
					if (!file.exists()) {
						File parent = file.getParentFile();
						if (!parent.exists()) {
							parent.mkdirs();
						}
						file.createNewFile();
					}
					this.fos = new FileOutputStream(file);
				}
				fos.write(data, offset, length);
				// CompressFormat format = CompressFormat.JPEG;
				// Bitmap bitmap = BitmapFactory.decodeFile(savePath);
				// ImageUtil.saveBitmap(bitmap, savePath + ".jpg", format, 40);
				// FileUtil.rename(savePath + ".jpg", savePath);

			} catch (Exception e) {
				FileUtil.deleteFile(savePath);
			}
		}

		@Override
		public void onProgressFinish(String hash) {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (hash != null) {
				File file = new File(savePath);
				if (!(file.exists() && FileMD5Util.getInstance()
						.getFileHash(file).equals(hash))) {
					FileUtil.deleteFile(savePath);
				}
			}
		}

	}

}
