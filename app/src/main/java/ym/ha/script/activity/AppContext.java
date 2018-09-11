package ym.ha.script.activity;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


public class AppContext extends MultiDexApplication {
    private static AppContext sAppContext;
    private long mLaunchTime;


    @Override
    public void onCreate() {
        super.onCreate();

        sAppContext = this;

        init();
    }

    public void init() {
        Fabric.with(this, new Crashlytics());


    }

    public static AppContext getInstance() {
        return sAppContext;
    }

    public long getLaunchTime() {
        return mLaunchTime;
    }

    public void setLaunchTime(long launchTime) {
        mLaunchTime = launchTime;
    }
}
