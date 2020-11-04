package android.frame.app;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.frame.Loople;
import android.log.Log;

public class AppBase extends Application {

    public static AppBase Impl;

    public PackageManager Pmgr;
    public PackageInfo Package;
    public ApplicationInfo Info;

    @Override
    public void onCreate() {
        super.onCreate();

        Impl = this;
        Info = getApplicationInfo();
        Pmgr = getPackageManager();
        Package = getPackageInfo(Info.packageName);

        Loople.init();
    }

    public PackageInfo getPackageInfo(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = Pmgr.getPackageInfo(packageName, 0);
        } catch (Exception e) {
            Log.e(e);
        }

        return packageInfo != null ? packageInfo : new PackageInfo();
    }
}
