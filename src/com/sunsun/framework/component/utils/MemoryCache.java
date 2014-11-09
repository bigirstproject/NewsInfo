
package com.sunsun.framework.component.utils;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 描述:自动内存缓存(自动管理缓存大小)
 * 
 */
public class MemoryCache extends CacheBase {

    private volatile static MemoryCache mInstance = null;

    private Map<String, byte[]> cache = Collections
            .synchronizedMap(new LinkedHashMap<String, byte[]>(10, 0.75f, true));

    private long size = 0; // 当前缓存分配的大小

    private long limit = 1000000; // 允许最大字节数

    private MemoryCache() {
        // 默认可用大小
        setLimit(Runtime.getRuntime().maxMemory() / 10);
    }

    public static MemoryCache getInstance() {
        if (mInstance == null) {
            synchronized (MemoryCache.class) {
                if (mInstance == null) {
                    mInstance = new MemoryCache();
                }
            }
        }

        return mInstance;
    }

    /**
     * 设置限制最大缓存大小
     * 
     * @param new_limit
     */
    public void setLimit(long new_limit) {

        this.limit = new_limit;
        commonLog("MemoryCache will use up to " + limit / 1024. / 1024. + "MB");
    }

    public byte[] outCache(String key) {
        try {

            if (!cache.containsKey(key))
                return null;

            return cache.get(key);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getBitmapCache(String key) {
        byte[] bytes = outCache(key);
        if (bytes != null) {
            return Bytes2Bimap(bytes);
        } else {
            return null;
        }
    }

    public boolean inCache(String key, byte[] bytes) {
        try {
            if (cache.containsKey(key)) {
                size -= cache.get(key).length;
            }
            cache.put(key, bytes);
            size += bytes.length;
            checkSize();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean inCache(String key, Bitmap bitmap) {
        return inCache(key, Bitmap2Bytes(bitmap));
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 控制堆内存大小 LRU机制
     */
    private void checkSize() {
        commonLog("cache size=" + size + " length=" + cache.size());
        if (size > limit) {
            // 先便利最近最少使用的元素
            Iterator<Entry<String, byte[]>> iter = cache.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, byte[]> entry = iter.next();
                size -= entry.getValue().length;
                iter.remove();
                if (size <= limit) {
                    break;
                }

                commonLog("Clean cache. New size " + cache.size());
            }
        }

    }

    public void clear() {
        cache.clear();
    }
}
