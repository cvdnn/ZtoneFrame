package android.frame;

import android.frame.exception.OnMainThreadException;
import android.log.Log;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.lang.reflect.Method;

public class Loople {
    public static Loople Impl;

    private final Handler mMainHandler;

    public static <O> boolean invokeInMainThread(@NonNull O o, @NonNull String methodName, Class<?>... parameterTypes) {
        boolean result = false;

        if (o != null) {
            try {
                Class<?> clazz = o.getClass();
                Method method = clazz.getMethod(methodName, parameterTypes);
                if (method != null) {
                    MainThread ann = method.getAnnotation(MainThread.class);
                    if (ann != null) {
                        result = true;
                    }
                }
            } catch (Exception e) {
                Log.e(e);
            }
        }


        return result;
    }

    public static void assertNotInMainThread() {
        if (inMainThread())
            throw new OnMainThreadException("ERROR: Cannot execute in main thread!");
    }

    public static boolean inMainThread() {

        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static void init() {
        Impl = new Loople();
    }

    ///////////////
    // Loople
    private Loople() {
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    public void post(Runnable r) {
        if (r != null) {
            if (!inMainThread()) {
                mMainHandler.post(r);
            } else {
                r.run();
            }

        }
    }

    public void postDelayed(@NonNull Runnable r, long delayMillis) {
        mMainHandler.postDelayed(r, delayMillis);
    }

    public void postAtFrontOfQueue(@NonNull Runnable r) {
        mMainHandler.postAtFrontOfQueue(r);
    }

    public void postAtTime(@NonNull Runnable r, long uptimeMillis) {
        mMainHandler.postAtTime(r, uptimeMillis);
    }

    public void postAtTime(@NonNull Runnable r, Object token, long uptimeMillis) {
        mMainHandler.postAtTime(r, token, uptimeMillis);
    }

    public void removeCallbacks(@NonNull Runnable r) {
        mMainHandler.removeCallbacks(r);
    }

    public void removeCallbacks(@NonNull Runnable r, Object token) {
        mMainHandler.removeCallbacks(r, token);
    }

    public void removeCallbacks(Object token) {
        mMainHandler.removeCallbacksAndMessages(token);
    }

    public void removeCallbacksAndMessages() {
        mMainHandler.removeCallbacksAndMessages(null);
    }
}
