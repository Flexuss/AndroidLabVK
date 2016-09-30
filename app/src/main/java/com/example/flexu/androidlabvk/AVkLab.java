package com.example.flexu.androidlabvk;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * @author Rishad Mustafaev
 */

public class AVkLab extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);
    }
}
