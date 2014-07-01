package uk.co.alt236.floatinginfo.provider;

import uk.co.alt236.floatinginfo.MemoryInfoReceiver;
import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.activity.MainActivity;
import uk.co.alt236.floatinginfo.asynctask.ProcessMonitorAsyncTask;
import uk.co.alt236.floatinginfo.container.ForegroundProcessInfo;
import uk.co.alt236.floatinginfo.container.InfoStore;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

public class GeneralInfoProvider extends BaseProvider implements MemoryInfoReceiver.Callbacks {

	private static final String TAG = "GeneralInfoProvider";
	private static final int NOTIFICATION_ID = 1138;

	private Handler mViewUpdateHandler = new Handler();
	private TextView mTextView;
	private MemoryInfoReceiver mLogReceiver;
	private NotificationManager mNotificationManager;
	private SharedPreferences mPrefs;
	private boolean mIsLogPaused = false;
	private int mForegroundAppPid;
	private ProcessMonitorAsyncTask mProcessMonitorTask;
	private InfoStore mInfoStore;

	public GeneralInfoProvider(Service context) {
		super(context);
		mInfoStore = new InfoStore();
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		mPrefs.registerOnSharedPreferenceChangeListener(this);
		mLogReceiver = new MemoryInfoReceiver(this);
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

		final LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mTextView = (TextView) inflator.inflate(R.layout.screen_overlay, null);
		setSystemViewBackground();
		wm.addView(mTextView, lp);
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
			// } else if (action == MemoryInfoReceiver.ACTION_SHARE) {
			// Intent intent = new Intent(getApplicationContext(),
			// ShareActivity.class);
			// return PendingIntent.getActivity(getApplicationContext(), 0,
			// intent, PendingIntent.FLAG_CANCEL_CURRENT);
		} else {
			Intent intent = new Intent(action);
			return PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		}
	}

	@Override
	public void onLogClear() {
		updateDisplay();
	}

	@Override
	public void onLogPause() {
		mIsLogPaused = true;
		showNotification();
	}

	@Override
	public void onLogResume() {
		mIsLogPaused = false;
		updateDisplay();
		showNotification();
	}

	@Override
	public void onLogShare() {
		final StringBuilder sb = new StringBuilder();

		final Time now = new Time();
		now.setToNow();

		final String ts = now.format3339(false);

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject) + " " + ts);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(shareIntent);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(getString(R.string.pref_bg_opacity))) {
			setSystemViewBackground();
		}
	}

	private void removeNotification() {
		// cancel the notification
		mNotificationManager.cancel(NOTIFICATION_ID);
	}

	private void removeSystemWindow() {
		if (mTextView != null && mTextView.getParent() != null) {
			final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			wm.removeView(mTextView);
		}
	}

	@SuppressWarnings("deprecation")
	private void setSystemViewBackground() {
		final int v = mPrefs.getInt(getString(R.string.pref_bg_opacity), 0);
		final int level = 0
				;
		if (v > 0) {
			int a = (int) ((float) v / 100f * 255);
			mTextView.setBackgroundColor(Color.argb(a, level, level, level));
		} else {
			mTextView.setBackgroundDrawable(null);
		}
	}

	private void showNotification() {
		String smallText = "small text!";
		String bigText = "big text!";

		final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
				.setStyle(new NotificationCompat.BigTextStyle()
						.bigText(bigText))
				.setSmallIcon(R.drawable.ic_stat_main)
				.setOngoing(true)
				.setContentTitle(getString(R.string.notification_title))
				.setContentText(smallText)
				.setContentIntent(getNotificationIntent(null));

		if (mIsLogPaused) {
			mBuilder.addAction(
					R.drawable.ic_stat_play,
					getString(R.string.statusbar_play),
					getNotificationIntent(MemoryInfoReceiver.ACTION_PLAY));
		} else {
			mBuilder.addAction(
					R.drawable.ic_stat_pause,
					getString(R.string.statusbar_pause),
					getNotificationIntent(MemoryInfoReceiver.ACTION_PAUSE));
		}

		mBuilder.addAction(
				R.drawable.ic_stat_clear,
				getString(R.string.statusbar_clear),
				getNotificationIntent(MemoryInfoReceiver.ACTION_CLEAR))
				.addAction(
						R.drawable.ic_stat_share,
						getString(R.string.statusbar_share),
						getNotificationIntent(MemoryInfoReceiver.ACTION_SHARE));

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
				boolean change = false;
				if (values[0].getPid() != mForegroundAppPid) {
					change = true;
				}

				mInfoStore.set(values[0]);

				updateDisplay();
				if (change) {
					showNotification();
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

	private void updateDisplay() {
		mViewUpdateHandler.post(new Runnable() {
			@Override
			public void run() {

				final StringBuilderHelper sb  = new StringBuilderHelper();

				if(mInfoStore != null){
					final ForegroundProcessInfo procInfo = mInfoStore.getForegroundProcessInfo();
					if(procInfo != null){
						sb.appendBold("Application Info");
						sb.append("App Name", String.valueOf(procInfo.getAppName()));
						sb.append("Package", procInfo.getPackage());
						sb.append("PID", procInfo.getPid());
					}
				}

				sb.setTextToView(mTextView);
			}
		});
	}
}
