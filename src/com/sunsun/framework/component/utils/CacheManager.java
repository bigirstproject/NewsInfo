
package com.sunsun.framework.component.utils;

/**
 * 描述: 缓存管理入口类
 * 
 */
public class CacheManager {
    private static MemoryCache mMemoryCache = MemoryCache.getInstance();

    private static FileCache mFileCache = FileCache.getInstance();

    private CacheManager() {

    }

    /*********************** 文件缓存控制 *********************/

    /**
     * 获取文件缓存中的文件
     * 
     * @param filePath
     * @return
     */
    public static byte[] getFileCache(String filePath) {
        return mFileCache.getFile(filePath);
    }

    /**
     * 判断缓存文件是否存在
     * 
     * @param filePath
     * @return
     */
    public static boolean existFileCache(String filePath) {
        return mFileCache.isFileExist(filePath);
    }

    /**
     * 删除文件
     * 
     * @param filePath
     * @return
     */
    public static boolean deleteFileCache(String filePath) {
        return mFileCache.deleteFile(filePath);
    }

    /**
     * 删除较早创建的文件
     * 
     * @param CachePath 缓存的绝对路径
     * @param KeepNum 保留缓存文件数量
     * @return
     */
    public static boolean autoDeleteFileCache(String CachePath, int KeepNum) {
        return mFileCache.autoDeleteCache(CachePath, KeepNum);
    }

    /*********************** 内存缓存控制 *********************/

    /**
     * 获取内存缓存中的文件
     * 
     * @param key
     * @return
     */
    public static byte[] getMemoryCache(String key) {
        return mMemoryCache.outCache(key);
    }

    /**
     * 装载入内存缓存
     * 
     * @param key
     * @param bytes
     * @return
     */
    public static boolean putMemoryCache(String key, byte[] bytes) {
        return mMemoryCache.inCache(key, bytes);

    }

    /**
     * 设置限制最大允许内存缓存大小
     * 
     * @param new_limit
     */
    public static void setLimiteMemoryCache(long new_limit) {
        mMemoryCache.setLimit(new_limit);
    }

    /**
     * 清空内存缓存
     */
    public static void clearMemoryCache() {
        mMemoryCache.clear();
    }

}
