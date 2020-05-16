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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.common.data.InfoStore;
import uk.co.alt236.floatinginfo.common.data.model.ForegroundAppData;
import uk.co.alt236.floatinginfo.common.prefs.EnabledInfoPrefs;
import uk.co.alt236.floatinginfo.common.prefs.OverlayPrefs;
import uk.co.alt236.floatinginfo.data.access.BaseProvider;
import uk.co.alt236.floatinginfo.data.access.generalinfo.monitortask.ProcessMonitor;
import uk.co.alt236.floatinginfo.notifications.NotificationControl;
import uk.co.alt236.floatinginfo.overlay.OverlayManager;

public class GeneralInfoProvider extends BaseProvider implements GeneralInfoReceiver.Callbacks {
    private static final String TAG = GeneralInfoProvider.class.getSimpleName();
    private final AtomicBoolean mIsLogPaused = new AtomicBoolean(false);
    private final AtomicInteger mForegroundAppPid = new AtomicInteger();
    private final GeneralInfoReceiver mLogReceiver;
    private final InfoStore mInfoStore;
    private final PrefsChangeListener mPrefsChangeListener;
    private final OverlayManager mOverlayManager;
    private final Handler mViewUpdateHandler = new Handler();
    private final NotificationControl mNotificationControl;
    private final ProcessMonitor mProcessMonitor;
    private final SystemWindowLayoutParamsFactory mLayoutParamsFactory;

    public GeneralInfoProvider(final Service context) {
        super(context);
        final EnabledInfoPrefs enabledInfoPrefs = new EnabledInfoPrefs(context);
        final OverlayPrefs overlayPrefs = new OverlayPrefs(context);
        final LayoutInflater inflater = LayoutInflater.from(getContext());

        mLayoutParamsFactory = new SystemWindowLayoutParamsFactory();
        mNotificationControl = new NotificationControl(context);
        mInfoStore = new InfoStore();
        mOverlayManager = new OverlayManager(inflater, mInfoStore, overlayPrefs, enabledInfoPrefs);
        mLogReceiver = new GeneralInfoReceiver(this);
        mPrefsChangeListener = new PrefsChangeListener(context, mOverlayManager);
        mProcessMonitor = new ProcessMonitor(context, enabledInfoPrefs);
    }

    private void createSystemWindow() {
        final ViewGroup.LayoutParams lp = mLayoutParamsFactory.getParams();
        final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.addView(mOverlayManager.getView(), lp);
    }

    @Override
    public void onLogClear() {
        updateDisplay();
    }

    @Override
    public void onLogPause() {
        mIsLogPaused.set(true);
        mNotificationControl.show(mIsLogPaused.get());
    }

    @Override
    public void onLogResume() {
        mIsLogPaused.set(false);
        mNotificationControl.show(mIsLogPaused.get());
    }

    @Override
    public void onLogShare() {
        final Time now = new Time();
        now.setToNow();

        final String ts = now.format3339(false);

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(mOverlayManager.getSharePayload()));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject) + " " + ts);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shareIntent);
    }

    private void removeNotification() {
        mNotificationControl.dismiss();
    }

    private void removeSystemWindow() {
        if (mOverlayManager.getView() != null && mOverlayManager.getView().getParent() != null) {
            final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mOverlayManager.getView());
        }
    }

    @Override
    public boolean start() {
        Log.d(TAG, "Starting Monitor");
        mPrefsChangeListener.register();
        registerReceiver(mLogReceiver, mLogReceiver.getIntentFilter());
        createSystemWindow();
        mNotificationControl.show(mIsLogPaused.get());
        startProcessMonitor();
        return true;
    }

    @Override
    public void stop() {
        Log.d(TAG, "Stopping Monitor");
        mPrefsChangeListener.unRegister();
        unregisterReceiver(mLogReceiver);
        stopProcessMonitor();
        removeSystemWindow();
        removeNotification();
    }

    private void startProcessMonitor() {
        mProcessMonitor.start(update -> {
            if (!mIsLogPaused.get()) {
                final ForegroundAppData appData = update.getForegroundAppData();

                final boolean change; // = false;
                if (appData.getPid() != mForegroundAppPid.get()) {
                    change = true;
                    mForegroundAppPid.set(appData.getPid());
                    mOverlayManager.clearPeakUsage();
                } else {
                    change = false;
                }

                mInfoStore.set(appData);
                mInfoStore.set(update.getNetData());
                mInfoStore.set(update.getCpuData());
                mInfoStore.set(update.getMemoryData());
                mInfoStore.set(update.getGeneralData());

                updateDisplay();

                if (change) {
                    mNotificationControl.show(mIsLogPaused.get());
                }
            }
        });
        Log.i(TAG, "process monitor task started");
    }

    private void stopProcessMonitor() {
        mProcessMonitor.stop();
        Log.i(TAG, "process monitor task stopped");
    }

    private void updateDisplay() {
        mViewUpdateHandler.post(mOverlayManager::update);
    }
}
