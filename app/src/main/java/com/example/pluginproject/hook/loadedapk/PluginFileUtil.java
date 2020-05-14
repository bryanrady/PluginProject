package com.example.pluginproject.hook.loadedapk;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PluginFileUtil {

    private static File mBaseDir;

    /**
     * 把Assets里面得文件复制到 /data/data/files 目录下
     * @param context
     * @param sourceName
     */
    public static void extractAssets(Context context, String sourceName) {
        AssetManager am = context.getAssets();
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = am.open(sourceName);
            File extractFile = context.getFileStreamPath(sourceName);
            fos = new FileOutputStream(extractFile);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(is);
            closeStream(fos);
        }

    }

    /**
     * 待加载插件经过opt优化之后存放odex的路径
     * @param context
     * @param packageName
     * @return
     */
    public static File getPluginOptDexPath(Context context, String packageName) {
        return enforceDirExists(new File(getPluginBaseDir(context, packageName), "odex"));
    }

    /**
     * 获取插件的lib库路径
     * @param context
     * @param packageName
     * @return
     */
    public static File getPluginLibPath(Context context, String packageName) {
        return enforceDirExists(new File(getPluginBaseDir(context, packageName), "lib"));
    }

    /**
     * 流的关闭
     * @param closeable
     */
    private static void closeStream(Closeable closeable) {
        if (closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 需要加载的插件的基本目录 /data/data/<package>/files/plugin/插件的包名
     * @param context
     * @param packageName
     * @return
     */
    private static File getPluginBaseDir(Context context, String packageName) {
        if (mBaseDir == null) {
            mBaseDir = context.getFileStreamPath("plugin");
            enforceDirExists(mBaseDir);
        }
        return enforceDirExists(new File(mBaseDir, packageName));
    }

    private static synchronized File enforceDirExists(File baseDir) {
        if (!baseDir.exists()) {
            boolean ret = baseDir.mkdir();
            if (!ret) {
                throw new RuntimeException("create dir " + baseDir + "failed");
            }
        }
        return baseDir;
    }
}
