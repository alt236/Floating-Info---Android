/*
 * Copyright 2016 Alexandros Schillings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.alt236.floatinginfo.data.access.generalinfo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.data.access.BaseProvider;
import uk.co.alt236.floatinginfo.data.access.generalinfo.asynctask.MonitorTask;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.InfoStore;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.cpu.CpuUtilisationReader;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo.ForegroundAppData;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.memory.MemoryInfoReader;
import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.model.NetData;
import uk.co.alt236.floatinginfo.ui.activity.main.MainActivity;
import uk.co.alt236.floatinginfo.ui.activity.share.ShareActivity;
import uk.co.alt236.floatinginfo.ui.overlay.OverlayManager;

public class GeneralInfoProvider extends BaseProvider implements GeneralInfoReceiver.Callbacks {

    private static final int NOTIFICATION_ID = 1138;
    private static final String TAG = "GeneralInfoProvider";
    private final AtomicBoolean mIsLogPaused = new AtomicBoolean(false);
    private final AtomicInteger mForegroundAppPid = new AtomicInteger();
    private final CpuUtilisationReader mCpuUtilisationReader;
    private final GeneralInfoReceiver mLogReceiver;
    private final InfoStore mInfoStore;
    private final MemoryInfoReader mMemoryInfoReader;
    private final NotificationManager mNotificationManager;
    private final SharedPreferences mPrefs;
    private final OverlayManager mOverlayManager;
    private final Handler mViewUpdateHandler = new Handler();
    private MonitorTask mProcessMonitorTask;

    public GeneralInfoProvider(final Service context) {
        super(context);
        mCpuUtilisationReader = new CpuUtilisationReader();
        mMemoryInfoReader = new MemoryInfoReader(getContext());
        mInfoStore = new InfoStore();
        mOverlayManager = new OverlayManager(context, mInfoStore);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mPrefs.registerOnSharedPreferenceChangeListener(this);
        mLogReceiver = new GeneralInfoReceiver(this);
        registerReceiver(mLogReceiver, mLogReceiver.getIntentFilter());
    }

    private void createSystemWindow() {
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                0,
                PixelFormat.TRANSLUCENT
        );
        final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.addView(mOverlayManager.getView(), lp);
    }

    @Override
    public void destroy() {
        mPrefs.unregisterOnSharedPreferenceChangeListener(this);
        unregisterReceiver(mLogReceiver);
        stopProcessMonitor();
        removeSystemWindow();
        removeNotification();
    }

    private PendingIntent getNotificationIntent(final String action) {
        if (action == null) {
            final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            return PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        } else if (GeneralInfoReceiver.ACTION_SHARE.equals(action)) {
            final Intent intent = new Intent(
                    getApplicationContext(),
                    ShareActivity.class);
            return PendingIntent.getActivity(
                    getApplicationContext(),
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            final Intent intent = new Intent(action);
            return PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
    }

    @Override
    public void onLogClear() {
        updateDisplay(true);
    }

    @Override
    public void onLogPause() {
        mIsLogPaused.set(true);
        showNotification();
    }

    @Override
    public void onLogResume() {
        mIsLogPaused.set(false);
        showNotification();
    }

    @Override
    public void onLogShare() {
        final StringBuilder sb = new StringBuilder();
        sb.append(mOverlayManager.getSharePayload());

        final Time now = new Time();
        now.setToNow();

        final String ts = now.format3339(false);

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject) + " " + ts);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shareIntent);
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        if (key.equals(getString(R.string.pref_key_bg_opacity))) {
            mOverlayManager.updateBackground();
        } else if (key.equals(getString(R.string.pref_key_text_alpha))) {
            mOverlayManager.updateTextColor();
        } else if (key.equals(getString(R.string.pref_key_text_size))) {
            mOverlayManager.updateTextSize();
        } else if (key.equals(getString(R.string.pref_key_text_color_red))) {
            mOverlayManager.updateTextColor();
        } else if (key.equals(getString(R.string.pref_key_text_color_green))) {
            mOverlayManager.updateTextColor();
        } else if (key.equals(getString(R.string.pref_key_text_color_blue))) {
            mOverlayManager.updateTextColor();
        } else if (key.equals(getString(R.string.pref_key_screen_position))) {
            mOverlayManager.updateAlignment();
        }
    }

    private void removeNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    private void removeSystemWindow() {
        if (mOverlayManager.getView() != null && mOverlayManager.getView().getParent() != null) {
            final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mOverlayManager.getView());
        }
    }

    private void showNotification() {
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.notification_big_text)))
                .setSmallIcon(R.drawable.ic_stat_main)
                .setOngoing(true)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_small_text))
                .setContentIntent(getNotificationIntent(null));

        if (mIsLogPaused.get()) {
            mBuilder.addAction(
                    R.drawable.ic_stat_play,
                    getString(R.string.statusbar_play),
                    getNotificationIntent(GeneralInfoReceiver.ACTION_PLAY));
        } else {
            mBuilder.addAction(
                    R.drawable.ic_stat_pause,
                    getString(R.string.statusbar_pause),
                    getNotificationIntent(GeneralInfoReceiver.ACTION_PAUSE));
        }

//		mBuilder.addAction(
//				R.drawable.ic_stat_clear,
//				getString(R.string.statusbar_clear),
//				getNotificationIntent(GeneralInfoReceiver.ACTION_CLEAR));

        mBuilder.addAction(
                R.drawable.ic_stat_share,
                getString(R.string.statusbar_share),
                getNotificationIntent(GeneralInfoReceiver.ACTION_SHARE));

        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public boolean start() {
        createSystemWindow();
        showNotification();
        startProcessMonitor();
        return true;
    }

    private void startProcessMonitor() {
        mProcessMonitorTask = new MonitorTask(getContext()) {
            @Override
            protected void onProgressUpdate(final MonitorTask.MonitorUpdate... values) {
                if (!mIsLogPaused.get()) {
                    final boolean change; // = false;
                    final ForegroundAppData appData = values[0].getForegroundAppData();
                    final NetData netData = values[0].getNetData();

                    if (appData.getPid() != mForegroundAppPid.get()) {
                        change = true;
                        mForegroundAppPid.set(appData.getPid());
                        mOverlayManager.clearPeakUsage();
                    } else {
                        change = false;
                    }

                    mMemoryInfoReader.update(mForegroundAppPid.get());
                    mCpuUtilisationReader.update();

                    mInfoStore.set(appData);
                    mInfoStore.set(netData);
                    mInfoStore.set(mCpuUtilisationReader.getCpuInfo());
                    mInfoStore.set(mMemoryInfoReader.getInfo());

                    updateDisplay(false);
                    if (change) {
                        showNotification();
                    }
                }
            }
        };

        mProcessMonitorTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        Log.i(TAG, "process monitor task started");
    }

    private void stopProcessMonitor() {
        if (mProcessMonitorTask != null) {
            mProcessMonitorTask.cancel(true);
        }
        mProcessMonitorTask = null;

        Log.i(TAG, "process monitor task stopped");
    }

    private void updateDisplay(final boolean clear) {

        mViewUpdateHandler.post(new Runnable() {
            @Override
            public void run() {
                mOverlayManager.update();
            }
        });
    }
}
