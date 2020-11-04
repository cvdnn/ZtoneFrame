package android.frame;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.framework.context.FrameActivity;
import android.framework.module.Setting;
import android.net.wifi.WifiManager;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by handy on 17-2-6.
 */

public interface Component {

    Application app();

    Component set(@NonNull Application application);

    Activity main();

    Component set(Activity activity);

    <S> S getSystemService(@NonNull String name);

    Intent registerReceiver(@Nullable BroadcastReceiver receiver, IntentFilter filter);

    Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter,
                            @Nullable String broadcastPermission, @Nullable Handler scheduler);

    void unregisterReceiver(@Nullable BroadcastReceiver receiver);

    WifiManager wifi();

    Setting setting();
}
