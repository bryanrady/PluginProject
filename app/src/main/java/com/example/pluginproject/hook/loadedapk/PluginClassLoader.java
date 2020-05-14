package com.example.pluginproject.hook.loadedapk;

import dalvik.system.DexClassLoader;

/**
 * 自定义加载插件的类加载器
 */
public class PluginClassLoader extends DexClassLoader {

    public PluginClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }
}
