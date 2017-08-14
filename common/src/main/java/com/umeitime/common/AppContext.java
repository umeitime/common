package com.umeitime.common;

import android.content.Context;

/**
 * 全局可用的context对象
 */
public class AppContext {
    private static AppContext instance;

    private Context applicationContext;

    public static AppContext getInstance() {
        if (instance == null) {
            throw new RuntimeException(AppContext.class.getSimpleName() + "has not been initialized!");
        }

        return instance;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public AppContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 全局信息 只能调用一次
     */
    public static void init(Context applicationContext) {
        if (instance != null) {
            throw new RuntimeException(AppContext.class.getSimpleName() + " can not be initialized multiple times!");
        }
        instance = new AppContext(applicationContext);
    }

    public static boolean isInitialized() {
        return (instance != null);
    }

}