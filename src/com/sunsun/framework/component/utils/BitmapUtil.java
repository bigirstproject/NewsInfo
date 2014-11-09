package com.sunsun.framework.component.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

/**
 * Bitmap创建类
 * 
 */
public class BitmapUtil {

	public static Bitmap createBitmap(Bitmap source, int x, int y, int width,
			int height, Matrix m, boolean filter) {
		Bitmap bitmap = null;
		if (source != null && !source.isRecycled()) {
			try {
				bitmap = Bitmap.createBitmap(source, 0, 0, width, height, m,
						true);
			} catch (OutOfMemoryError e) {
				System.gc();
				try {
					bitmap = Bitmap.createBitmap(source, 0, 0, width, height,
							m, true);
				} catch (OutOfMemoryError e1) {
				}
			} catch (Exception e2) {
			}
		}
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		}
		return bitmap;
	}

	public static Bitmap createBitmap(int width, int height, Config config) {
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(width, height, config);
		} catch (OutOfMemoryError e) {
			System.gc();
			try {
				bitmap = Bitmap.createBitmap(width, height, config);
			} catch (OutOfMemoryError e1) {
			}
		} catch (Exception e2) {
		}
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		}
		return bitmap;
	}

	public static Bitmap decodeFile(String pathName) {
		Bitmap bitmap = null;
		if (FileUtil.isExist(pathName)) {
			InputStream is = FileUtil.getFileInputStream(pathName);
			bitmap = decodeStream(is);
		}
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		}
		return bitmap;
	}

	public static Bitmap decodeFile(String pathName, int width, int height) {
		Bitmap bitmap = null;
		if (FileUtil.isExist(pathName)) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(pathName, opts);
			opts.inSampleSize = computeSampleSize(opts, -1, width * height);
			opts.inJustDecodeBounds = false;

			opts.inPurgeable = true;
			opts.inInputShareable = true;
			opts.inPreferredConfig = android.graphics.Bitmap.Config.ALPHA_8;
			opts.inDither = true;

			try {
				bitmap = BitmapFactory.decodeFile(pathName, opts);
			} catch (OutOfMemoryError e) {
				System.gc();
				try {
					bitmap = BitmapFactory.decodeFile(pathName, opts);
				} catch (OutOfMemoryError e1) {
				}
			} catch (Exception e2) {
			}
		}
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		}
		return bitmap;
	}

	public static Bitmap decodeStream(InputStream is) {
		Bitmap bitmap = null;
		if (is != null) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			opts.inPreferredConfig = android.graphics.Bitmap.Config.ALPHA_8;
			opts.inDither = true;

			try {
				bitmap = BitmapFactory.decodeStream(is, null, opts);
			} catch (OutOfMemoryError e) {
				System.gc();
				try {
					bitmap = BitmapFactory.decodeStream(is, null, opts);
				} catch (OutOfMemoryError e1) {
				}
			} catch (Exception e2) {
			}
		}
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		}
		return bitmap;
	}

	public static Bitmap createBitmap(int[] colors, int width, int height,
			Config config) {
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(colors, width, height, config);
		} catch (OutOfMemoryError e) {
			System.gc();
			try {
				bitmap = Bitmap.createBitmap(colors, width, height, config);
			} catch (OutOfMemoryError e1) {
			}
		} catch (Exception e2) {
		}
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		}
		return bitmap;
	}

	public static Bitmap createBitmap(Bitmap source, int x, int y, int width,
			int height) {
		Bitmap bitmap = null;
		if (source != null && !source.isRecycled()) {
			try {
				bitmap = Bitmap.createBitmap(source, x, y, width, height);
			} catch (OutOfMemoryError e) {
				System.gc();
				try {
					bitmap = Bitmap.createBitmap(source, x, y, width, height);
				} catch (OutOfMemoryError e1) {
				}
			} catch (Exception e2) {
			}
		}
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		}
		return bitmap;
	}

	// public static Bitmap decodeResource(Resources res, int id) {
	// Bitmap bitmap = null;
	// InputStream is =
	// KugouApplication.getContext().getResources().openRawResource(id);
	// bitmap = decodeStream(is);
	// if (bitmap == null) {
	// bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
	// }
	// return bitmap;
	// }

	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 读取
	 * 
	 * @param filePath
	 *            文件路径
	 * @return
	 */
	public static Bitmap readBitmap(final String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		} else {
			// BitmapFactory.Options options=new BitmapFactory.Options();
			// options.inSampleSize = 8;
			return BitmapUtil.decodeFile(filePath);
		}
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static void saveMyBitmap(String path, Bitmap mBitmap) {
		FileUtil.createFile(path, FileUtil.MODE_COVER);
		File f = new File(path);
		try {
			f.createNewFile();
		} catch (Exception e) {
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 80, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap decodeResource(Context context, int resId) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory
					.decodeResource(context.getResources(), resId);
		} catch (OutOfMemoryError e) {
			System.gc();
		} catch (Exception e2) {
		}
		if (bitmap == null) {
			bitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		}
		return bitmap;
	}
}
