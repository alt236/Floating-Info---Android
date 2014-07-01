package uk.co.alt236.memoryoverlay.service;

import uk.co.alt236.memoryoverlay.provider.BaseProvider;
import uk.co.alt236.memoryoverlay.provider.GeneralInfoProvider;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MemoryInfoService extends Service {

    private static boolean sIsRunning = false;

    private BaseProvider mMonitor;
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMonitor = new GeneralInfoProvider(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sIsRunning = false;
        mMonitor.destroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sIsRunning = true;
        mMonitor.start();
        return Service.START_STICKY;
    }

    public static boolean isRunning() {
        return sIsRunning;
    }

}