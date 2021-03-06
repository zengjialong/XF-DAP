package com.chs.mt.xf_dap.cache.util;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.StatFs;

/**
 * ���湤����.
 *@Title:
 *@Description:
 *@Author:Justlcw
 *@Since:2014-3-6
 *@Version:
 */
public final class CacheUtils
{
    /**
     * ���bitmap���ֽڴ�С.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public static int getBitmapSize(Bitmap bitmap)
    {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    /**
     * �ⲿ�洢�Ƿ�ɶ�д.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-5
     */
    public static boolean isExternalStorageRWable()
    {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * ��ÿ�ʹ�õ�<������Ŀ¼>(���ⲿ,���ⲿ)
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    public static File getEnabledCacheDir(Context context, String cacheName)
    {
        String cachePath;
        if(isExternalStorageRWable())
        {
            cachePath = Environment.getExternalStorageDirectory().getPath();
        }
        else
        {
            cachePath = context.getCacheDir().getPath();
        }
        File cacheFile = new File(cachePath + CacheConfig.DISK_CACHE_NAME + cacheName);
        //�������Ŀ¼������,��������Ŀ¼.
        if (!cacheFile.exists())
        {
            cacheFile.mkdirs();
        }
        return cacheFile;
    }
    
    /**
     * ����ļ�·����,�ж��ٿռ����.
     * @param path �ļ�·��.
     * @return ���ÿռ�.
     */
    public static long getUsableSpace(File path)
    {
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }

    /**
     * Return the approximate per-application memory class of the current device. 
     * This gives you an idea of how hard a memory limit you should impose on your 
     * application to let the overall system work best. The returned value is in megabytes; 
     * the baseline Android memory class is 16 (which happens to be the Java heap limit of those devices); 
     * some device with more memory may return 24 or even higher numbers.
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-6
     */
    private static int getMemoryClass(Context context)
    {
        return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    }
    
    /**
     * 
     * @param context context
     * @param shrinkFactor �ڴ��ܴ�С����С����(��:�������8,���õ��ڴ�1/8�Ĵ�С)
     * @Description:
     * @Author Justlcw
     * @Date 2014-3-7
     */
    public static int getMemorySize(Context context, int shrinkFactor)
    {
        int totalSize = getMemoryClass(context)*1024*1024;
        
        return totalSize / shrinkFactor;
    }
}
