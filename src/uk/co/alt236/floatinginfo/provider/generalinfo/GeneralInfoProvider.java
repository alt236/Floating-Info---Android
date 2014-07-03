package uk.co.alt236.floatinginfo.provider.generalinfo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.activity.MainActivity;
import uk.co.alt236.floatinginfo.activity.ShareActivity;
import uk.co.alt236.floatinginfo.provider.BaseProvider;
import uk.co.alt236.floatinginfo.provider.generalinfo.asynctask.ForegroundProcessInfo;
import uk.co.alt236.floatinginfo.provider.generalinfo.asynctask.ProcessMonitorAsyncTask;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.InfoStore;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.cpu.CpuUtilisationReader;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.memory.MemoryInfoReader;
import uk.co.alt236.floatinginfo.provider.generalinfo.ui.UiManager;
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

public class GeneralInfoProvider extends BaseProvider implements GeneralInfoReceiver.Callbacks {

	private static final int NOTIFICATION_ID = 1138;
	private static final String TAG = "GeneralInfoProvider";

	private ProcessMonitorAsyncTask mProcessMonitorTask;
	private final AtomicBoolean mIsLogPaused = new AtomicBoolean(false);
	private final AtomicInteger mForegroundAppPid = new AtomicInteger();
	private final CpuUtilisationReader mCpuUtilisationReader;
	private final GeneralInfoReceiver mLogReceiver;
	private final InfoStore mInfoStore;
	private final MemoryInfoReader mMemoryInfoReader;
	private final NotificationManager mNotificationManager;
	private final SharedPreferences mPrefs;
	private final UiManager mUiManager;

	private Handler mViewUpdateHandler = new Handler();

	public GeneralInfoProvider(Service context) {
		super(context);
		mCpuUtilisationReader = new CpuUtilisationReader();
		mMemoryInfoReader = new MemoryInfoReader(getContext());
		mInfoStore = new InfoStore();
		mUiManager = new UiManager(context, mInfoStore);
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
		wm.addView(mUiManager.getView(), lp);
	}

	@Override
	public void destroy() {
		mPrefs.unregisterOnSharedPreferenceChangeListener(this);
		unregisterReceiver(mLogReceiver);
		stopProcessMonitor();
		removeSystemWindow();
		removeNotification();
	}

	private PendingIntent getNotificationIntent(String action) {
		if (action == null) {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			return PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		} else if (action == GeneralInfoReceiver.ACTION_SHARE) {
			final Intent intent = new Intent(
					getApplicationContext(),
					ShareActivity.class);
			return PendingIntent.getActivity(
					getApplicationContext(),
					0,
					intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
		} else {
			Intent intent = new Intent(action);
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
		sb.append(mUiManager.getSharePayload());

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
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(getString(R.string.pref_bg_opacity))) {
			mUiManager.setBackground();
		} else if (key.equals(getString(R.string.pref_text_opacity))) {
			mUiManager.setTextColor();
		} else if (key.equals(getString(R.string.pref_text_size))) {
			mUiManager.setTextSize();
		} else if (key.equals(getString(R.string.pref_text_color_red))) {
			mUiManager.setTextColor();
		} else if (key.equals(getString(R.string.pref_text_color_green))) {
			mUiManager.setTextColor();
		} else if (key.equals(getString(R.string.pref_text_color_blue))) {
			mUiManager.setTextColor();
		}
	}

	private void removeNotification() {
		mNotificationManager.cancel(NOTIFICATION_ID);
	}

	private void removeSystemWindow() {
		if (mUiManager.getView() != null && mUiManager.getView().getParent() != null) {
			final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			wm.removeView(mUiManager.getView());
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
		mProcessMonitorTask = new ProcessMonitorAsyncTask(getContext()) {
			@Override
			protected void onProgressUpdate(ForegroundProcessInfo... values) {
				if(!mIsLogPaused.get()){
					final boolean change; // = false;

					if (values[0].getPid() != mForegroundAppPid.get()) {
						change = true;
						mForegroundAppPid.set(values[0].getPid());
						mUiManager.clearPeakUsage();
					} else {
						change = false;
					}

					mMemoryInfoReader.update(mForegroundAppPid.get());
					mCpuUtilisationReader.update();

					mInfoStore.set(values[0]);
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
				mUiManager.update();
			}
		});
	}
}
