package com.example.pluginproject.hook.merge;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MergePluginManager {

    private AssetManager mAssets;
    private Resources mResources;

    private static final MergePluginManager INSTANCE = new MergePluginManager();

    private MergePluginManager(){

    }

    public static MergePluginManager getInstance(){
        return INSTANCE;
    }

    /**
     * 合并宿主apk和插件apk的dexElements数组
     * @param context
     * @param filePath
     */
    public void mergeDex(Context context, String filePath){
        try {
            //1.根据加载插件apk的DexClassLoader并找出插件apk的dexElements数组
            String cacheDir = context.getCacheDir().getAbsolutePath();
            DexClassLoader dexClassLoader = new DexClassLoader(filePath, cacheDir, null, context.getClassLoader());
            Field pluginPathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            pluginPathListField.setAccessible(true);
            Object pluginPathList = pluginPathListField.get(dexClassLoader);

            Field pluginDexElementsField = pluginPathList.getClass().getDeclaredField("dexElements");
            pluginDexElementsField.setAccessible(true);
            Object pluginDexElements = pluginDexElementsField.get(pluginPathList);

            //2.根据加载系统的PathClassLoader找到系统的dexElements数组
            PathClassLoader pathClassLoader = (PathClassLoader) context.getClassLoader();
            Field systemPathListField = BaseDexClassLoader.class.getDeclaredField("pathList");
            systemPathListField.setAccessible(true);
            Object systemPathList = systemPathListField.get(pathClassLoader);
            
            Field systemDexElementsField = systemPathList.getClass().getDeclaredField("dexElements");
            systemDexElementsField.setAccessible(true);
            Object systemDexElements = systemDexElementsField.get(systemPathList);

            //3.将插件的dexElements数组和系统的dexElements数组合并成一个新的dexElements数组

            // 先得到每个数组中的长度
            int pluginDexElementsLength = Array.getLength(pluginDexElements);
            int systemDexElementsLength = Array.getLength(systemDexElements);
            //新的数组的长度
            int dexElementsLength = pluginDexElementsLength + systemDexElementsLength;

            //得到dexElements数组中的类型 Element
            Class dexElementsComponentType = systemDexElements.getClass().getComponentType();
            //根据元素类型和长度重新生成一个新的dexElements数组
            Object dexElements = Array.newInstance(dexElementsComponentType, dexElementsLength);

            //将两个数组中的元素添加到新的数组中
            for (int i = 0; i < dexElementsLength; i++){
                if (i < pluginDexElementsLength){
                    Array.set(dexElements, i, Array.get(pluginDexElements, i));
                }else{
                    Array.set(dexElements, i, Array.get(systemDexElements, i - pluginDexElementsLength));
                }
            }

            //4. 将系统中原来的dexElements数组换成我们新生成的dexElements数组
            systemDexElementsField.set(systemPathList, dexElements);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并宿主apk和插件apk的资源
     * @param context
     * @param filePath
     */
    public void mergeResources(Context context, String filePath) {
        try {
            mAssets = AssetManager.class.newInstance();
            Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.setAccessible(true);
            addAssetPathMethod.invoke(mAssets, filePath);

            // getResources().getDrawable() 、getResources().getColor()、getResources().getString()
            // string.xml   一个xml资源文件对应一个StringBlock
            // color.xml    一个xml资源文件对应一个StringBlock
            // styles.xml   一个xml资源文件对应一个StringBlock
            // ...........

            // 系统如何找到一个资源的?
            // 最终是找到AssetManager中的StringBlock数组，然后根据资源id找到对应的block下标，然后通过block在Block数组中找到对应的资源

            //实例化StringBlock数组  调用ensureStringBlocks()后，插件的StringBlock数组就会被实例化
            Method ensureStringBlocksMethod = AssetManager.class.getDeclaredMethod("ensureStringBlocks");
            ensureStringBlocksMethod.setAccessible(true);
            ensureStringBlocksMethod.invoke(mAssets);

            //拿到了插件apk中的资源
            Resources systemResources = context.getResources();
            mResources = new Resources(mAssets, systemResources.getDisplayMetrics(), systemResources.getConfiguration());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AssetManager getAssets(){
        return mAssets;
    }

    public Resources getResources(){
        return mResources;
    }

}
