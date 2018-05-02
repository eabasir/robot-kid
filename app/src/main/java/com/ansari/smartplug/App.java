package com.ansari.smartplug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;


public class App extends android.app.Application {


    private static App instance;

    public static String PACKAGE_NAME;
    public static Activity CurrentActivity;
    public static Context context;
    public static PackageManager packageManager;
    public static final Handler HANDLER = new Handler();

    private Map<Class<?>, Collection<?>> uiListeners;

    /**
     * Thread to execute tasks in background..
     */
    private final ExecutorService backgroundExecutor;

    private final Handler handler;


    public static App getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    public App ()
    {
        instance = this;
        uiListeners = new HashMap<>();
        handler = new Handler();
        backgroundExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "Background executor service");
//                thread.setPriority(Thread.MIN_PRIORITY);
//                thread.setDaemon(true);
                return thread;
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            context = getApplicationContext();

            try {
                PACKAGE_NAME = context.getPackageName();
                packageManager = getPackageManager();

            } catch (Exception e) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Register new listener.
     * <p/>
     * Should be called from {@link Activity#onResume()}.
     */
    public <T> void addUIListener(Class<T> cls, T listener) {
        getOrCreateUIListeners(cls).add(listener);
    }
    public <T> Collection<T> getUIListeners(Class<T> cls) {
        return Collections.unmodifiableCollection(getOrCreateUIListeners(cls));
    }

    @SuppressWarnings("unchecked")
    private <T> Collection<T> getOrCreateUIListeners(Class<T> cls) {
        Collection<T> collection = (Collection<T>) uiListeners.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            uiListeners.put(cls, collection);
        }
        return collection;
    }

    /**
     * Unregister listener.
     * <p/>
     * Should be called from {@link Activity#onPause()}.
     */
    public <T> void removeUIListener(Class<T> cls, T listener) {
        getOrCreateUIListeners(cls).remove(listener);
    }

    /**
     * Submits request to be executed in background.
     */
    public void runInBackground(final Runnable runnable) {
        backgroundExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                }
            }
        });
    }

    public void runOnUiThread(final Runnable runnable) {
        handler.post(runnable);
    }
}