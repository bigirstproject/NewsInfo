
package com.sunsun.framework.component.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;

/**
 * 有序HashMap
 * 
 */
public class SortedHashMap<String, T extends Object> extends ConcurrentHashMap {

    private ArrayList<String> list = new ArrayList<String>();

    private int size;

    public SortedHashMap(int size) {
        this.size = size;
    }

    @Override
    public synchronized T put(Object key, Object value) {
        if (!containsKey(key)) {
            list.add((String) key);
        }
        return (T) super.put(key, value);
    }

    @Override
    public T get(Object key) {
        return (T) super.get(key);
    }

    public synchronized void checkFull() {
        if (size() >= this.size) {
            removeFirst();
        }
    }

    private void removeFirst() {
        if (list.size() > 0) {
            String key = list.get(0);
            Object obj = get(key);
            if (obj instanceof Bitmap) {
                ((Bitmap) obj).recycle();
            }
            list.remove(key);
            remove(key);
        }
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    public synchronized void recycle() {
        for (Iterator iter = entrySet().iterator(); iter.hasNext();) {
            Entry<String, WeakReference<Bitmap>> entry = (Entry<String, WeakReference<Bitmap>>) iter
                    .next();
            Bitmap bm = entry.getValue().get(); // 返回与此项对应的值
            if (bm != null) {
                bm.recycle();
            }
        }
    }

}
