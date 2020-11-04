package android.frame.app;

import android.Android;
import android.frame.Args;
import android.log.Log;
import android.log.monitoring.MonitorUtils;
import android.os.Process;

import androidx.multidex.MultiDexApplication;

public class AppBase extends MultiDexApplication {
    public static final String TAG = "AppBase";

    @Override
    public void onCreate() {
        super.onCreate();
        onAppInit();

        if (Args.Env.inPackageProcess()) {
            onAppProcess();

        } else {
            Log.i(TAG, ">> [PP]: %s", Args.Env.myProcessName());
        }
    }

    protected void onAppInit() {

    }

    protected void onAppProcess() {
        MonitorUtils.startMonitorMainThread();

        Log.i(TAG, ">> [PID]: %d, [CPU]: %s", Process.myPid(), Android.Build.cpuSerial());
    }
}
