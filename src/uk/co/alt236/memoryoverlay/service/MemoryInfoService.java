package uk.co.alt236.memoryoverlay.service;

import uk.co.alt236.memoryoverlay.monitor.BaseMonitor;
import uk.co.alt236.memoryoverlay.monitor.MemoryMonitor;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MemoryInfoService extends Service {

    private static boolean sIsRunning = false;

    private BaseMonitor mMonitor;
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMonitor = new MemoryMonitor(this);
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