
package com.sunsun.framework.component.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 描述: 文件缓存
 * 
 */
public class FileCache extends CacheBase {

    private volatile static FileCache mInstance = null;

    private FileCache() {

    }

    public static FileCache getInstance() {
        if (mInstance == null) {

            synchronized (FileCache.class) {
                if (mInstance == null) {
                    mInstance = new FileCache();
                }
            }
        }

        return mInstance;
    }

    /**
     * 判断缓存文件是否存在
     * 
     * @param FilePath 文件绝对路径
     * @return 存在true 否则false
     */
    public boolean isFileExist(String filePath) {
        if (filePath == null || filePath.length() < 1) {
            errorFilePath(filePath);
            return false;
        }

        File file = new File(filePath);

        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 删除对应文件
     * 
     * @param FilePath 文件绝对路径
     * @return 删除错误false
     */
    public boolean deleteFile(String filePath) {
        if (null == filePath) {
            errorFilePath(filePath);
            return false;
        }

        File file = new File(filePath);

        if (file == null || !file.exists()) {
            return false;
        }

        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 返回对应文件
     * 
     * @param filePath 文件绝对路径
     * @return 文件Bytes
     */
    public byte[] getFile(String filePath) {
        if (null == filePath) {
            errorFilePath(filePath);
            return null;
        }

        try {
            if (isFileExist(filePath)) {

                BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                int c = in.read();
                while (c != -1) {
                    out.write(c);
                    c = in.read();
                }
                in.close();

                return out.toByteArray();

            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 自动删除部分缓存
     * 
     * @基本机制 优先删除创建日期较早的文件
     * @param KeepNum 保留缓存文件数量
     * @param CachePath 缓存的绝对路径
     * @return 删除错误false
     */

    public boolean autoDeleteCache(String CachePath, int KeepNum) {
        if (null == CachePath) {
            errorFilePath(CachePath);
            return false;
        }
        File cachefile = new File(CachePath);
        File[] cachefiles = cachefile.listFiles();

        if (KeepNum <= cachefiles.length) {
            return true;
        }

        Arrays.sort(cachefiles, new CompratorByLastModified());

        try {

            for (int i = cachefiles.length - 1; i >= KeepNum && i >= 0; i++) {
                cachefiles[i].delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private static class CompratorByLastModified implements Comparator<Object> {
        public int compare(Object o1, Object o2) {
            File file1 = (File) o1;
            File file2 = (File) o2;
            long diff = file1.lastModified() - file2.lastModified();
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }

        public boolean equals(Object obj) {
            return true; // 简单做法
        }
    }

}
