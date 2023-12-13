package com.example.ktx_ute;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.HashMap;

public class Global extends Application implements LifecycleObserver {

    private static Global instance;
    public static Global getInstance() {
        return instance;
    }


    public void makeToast(String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private static HashMap<Class, Object> services = new HashMap<Class, Object>();

    public static <T> T getService(Class<T> type) {
        return type.cast(services.get(type));
    }

    public static void registerService(Object service)
    {
        if (service != null)
        {
            services.put(service.getClass(), service);
        }
    }


    private static boolean isChatRoomVisible = false;
    private static boolean isLogged = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerService(new StudentData());
        registerService(new AdminData());
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public static boolean isInForeground() {
        return ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
    }

    public static boolean isChatRoomVisible() {
        return isChatRoomVisible;
    }

    public static void startChatRoom() {
        isChatRoomVisible = true;
    }

    public static void stopChatRoom() {
        isChatRoomVisible = false;
    }

    public static void setLoginStatus(boolean value) {
        isLogged = value;
    }

    public static boolean IsLogged() {
        return isLogged;
    }
}
