package android.frame.ui.context;

import android.framework.context.lifecycle.LifeCycleUtils;
import androidx.multidex.MultiDexApplication;

public class FrameApplication extends MultiDexApplication {
    private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        LifeCycleUtils.adhere().onStart(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        LifeCycleUtils.adhere().onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        LifeCycleUtils.adhere().onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        LifeCycleUtils.adhere().onTrimMemory(level);
    }
}
