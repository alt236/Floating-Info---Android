package uk.co.alt236.floatinginfo.util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class FloatingInfoReceiver extends BroadcastReceiver {

    public static final String ACTION_PLAY = "uk.co.alt236.memoryoverlay.ACTION_PLAY";
    public static final String ACTION_PAUSE = "uk.co.alt236.memoryoverlay.ACTION_PAUSE";
    public static final String ACTION_CLEAR = "uk.co.alt236.memoryoverlay.ACTION_CLEAR";
    public static final String ACTION_SHARE = "uk.co.alt236.memoryoverlay.ACTION_SHARE";

    public interface Callbacks {
        public void onLogPause();
        public void onLogResume();
        public void onLogClear();
        public void onLogShare();
    }

    private Callbacks mCallbacks;

    public FloatingInfoReceiver(Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    public IntentFilter getIntentFilter() {
        IntentFilter f = new IntentFilter();
        f.addAction(ACTION_PLAY);
        f.addAction(ACTION_PAUSE);
        f.addAction(ACTION_CLEAR);
        f.addAction(ACTION_SHARE);
        return f;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_PLAY.equals(action)) {
            mCallbacks.onLogResume();
        } else if (ACTION_PAUSE.equals(action)) {
            mCallbacks.onLogPause();
        } else if (ACTION_CLEAR.equals(action)) {
            mCallbacks.onLogClear();
        } else if (ACTION_SHARE.equals(action)) {
            mCallbacks.onLogShare();
        }
    }
}