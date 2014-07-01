package uk.co.alt236.floatinginfo.provider;

import java.util.List;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.activity.MainActivity;
import uk.co.alt236.floatinginfo.asynctask.ProcessMonitorAsyncTask;
import uk.co.alt236.floatinginfo.container.CpuData;
import uk.co.alt236.floatinginfo.container.ForegroundProcessInfo;
import uk.co.alt236.floatinginfo.container.InfoStore;
import uk.co.alt236.floatinginfo.container.MemoryData;
import uk.co.alt236.floatinginfo.reader.CpuUtilisationReader;
import uk.co.alt236.floatinginfo.reader.MemoryInfoReader;
import uk.co.alt236.floatinginfo.util.FloatingInfoReceiver;
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

public class GeneralInfoProvider extends BaseProvider implements FloatingInfoReceiver.Callbacks {

	private static final String TAG = "GeneralInfoProvider";
	private static final int NOTIFICATION_ID = 1138;

	private Handler mViewUpdateHandler = new Handler();
	private TextView mTextView;
	private FloatingInfoReceiver mLogReceiver;
	private NotificationManager mNotificationManager;
	private SharedPreferences mPrefs;
	private boolean mIsLogPaused = false;
	private int mForegroundAppPid;
	private ProcessMonitorAsyncTask mProcessMonitorTask;
	private final CpuUtilisationReader mCpuUtilisationReader;
	private final MemoryInfoReader mMemoryInfoReader;

	private final InfoStore mInfoStore;

	public GeneralInfoProvider(Service context) {
		super(context);
		mCpuUtilisationReader = new CpuUtilisationReader();
		mMemoryInfoReader = new MemoryInfoReader(getContext());
		mInfoStore = new InfoStore();

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		mPrefs.registerOnSharedPreferenceChangeListener(this);
		mLogReceiver = new FloatingInfoReceiver(this);
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

	private CharSequence getInfoText(){
		final StringBuilderHelper sb  = new StringBuilderHelper();

		if(mInfoStore != null){
			final ForegroundProcessInfo procInfo = mInfoStore.getForegroundProcessInfo();
			if(procInfo != null){
				sb.appendBold("Foreground Application Info");
				sb.append("App Name", String.valueOf(procInfo.getAppName()));
				sb.append("Package", procInfo.getPackage());
				sb.append("PID", procInfo.getPid());
			}

			sb.appendNewLine();

			final CpuData cpuInfo = mInfoStore.getCpuInfo();
			sb.setPadding(6);
			if(cpuInfo != null){
				sb.appendBold("Global CPU Utilisation");
				sb.append("Total", String.valueOf(cpuInfo.getOveralCpu()) + "%");
				final List<Integer> list = cpuInfo.getPerCpuUtilisation();

				int count = 0;

				for(Integer value : list){
					sb.append("CPU" + count, String.valueOf(value) + "%");
					count++;
				}
			}

			sb.appendNewLine();

			final MemoryData memoryInfo = mInfoStore.getMemoryInfo();
			sb.setPadding(20);
			if(memoryInfo != null){
				sb.appendBold("Current Process Memory Utilisation");
				sb.append("DalvikPrivateClean", memoryInfo.getDalvikPrivateClean());
				sb.append("DalvikPrivateDirty", memoryInfo.getDalvikPrivateDirty());
				sb.append("DalvikPss", memoryInfo.getDalvikPss());
				sb.append("DalvikSharedClean", memoryInfo.getDalvikSharedClean());
				sb.append("DalvikSharedDirty", memoryInfo.getDalvikSharedDirty());
				sb.append("DalvikSwappablePss", memoryInfo.getDalvikSwappablePss());
				sb.append("DalvikSwappedOut", memoryInfo.getDalvikSwappedOut());

				sb.append("NativePrivateClean", memoryInfo.getNativePrivateClean());
				sb.append("NativePrivateDirty", memoryInfo.getNativePrivateDirty());
				sb.append("NativePss", memoryInfo.getNativePss());
				sb.append("NativeSharedClean", memoryInfo.getNativeSharedClean());
				sb.append("NativeSharedDirty", memoryInfo.getNativeSharedDirty());
				sb.append("NativeSwappablePss", memoryInfo.getNativeSwappablePss());
				sb.append("NativeSwappedOut", memoryInfo.getNativeSwappedOut());

				sb.append("OtherPrivateClean", memoryInfo.getOtherPrivateClean());
				sb.append("OtherPrivateDirty", memoryInfo.getOtherPrivateDirty());
				sb.append("OtherPss", memoryInfo.getOtherPss());
				sb.append("OtherSharedClean", memoryInfo.getOtherSharedClean());
				sb.append("OtherSharedDirty", memoryInfo.getOtherSharedDirty());
				sb.append("OtherSwappablePss", memoryInfo.getOtherSwappablePss());
				sb.append("OtherSwappedOut", memoryInfo.getOtherSwappedOut());
			}
		}

		return sb.toCharSequence();
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
			setSystemViewBackground();
		}
	}

	private void removeNotification() {
		mNotificationManager.cancel(NOTIFICATION_ID);
	}

	private void removeSystemWindow() {
		if (mTextView != null && mTextView.getParent() != null) {
			final WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			wm.removeView(mTextView);
		}
	}

	private void setSystemViewBackground() {
		final int v = mPrefs.getInt(getString(R.string.pref_bg_opacity), 0);
		final int level = 0
				;
		if (v > 0) {
			int a = (int) ((float) v / 100f * 255);
			mTextView.setBackgroundColor(Color.argb(a, level, level, level));
		} else {
			mTextView.setBackground(null);
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
					getNotificationIntent(FloatingInfoReceiver.ACTION_PLAY));
		} else {
			mBuilder.addAction(
					R.drawable.ic_stat_pause,
					getString(R.string.statusbar_pause),
					getNotificationIntent(FloatingInfoReceiver.ACTION_PAUSE));
		}

		mBuilder.addAction(
				R.drawable.ic_stat_clear,
				getString(R.string.statusbar_clear),
				getNotificationIntent(FloatingInfoReceiver.ACTION_CLEAR))
				.addAction(
						R.drawable.ic_stat_share,
						getString(R.string.statusbar_share),
						getNotificationIntent(FloatingInfoReceiver.ACTION_SHARE));

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
					mForegroundAppPid = values[0].getPid();
				}

				mMemoryInfoReader.update(mForegroundAppPid);
				mCpuUtilisationReader.update();

				mInfoStore.set(values[0]);
				mInfoStore.set(mCpuUtilisationReader.getCpuInfo());
				mInfoStore.set(mMemoryInfoReader.getInfo());

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
		final CharSequence output = getInfoText();

		mViewUpdateHandler.post(new Runnable() {
			@Override
			public void run() {
				mTextView.setText(output);
			}
		});
	}
}
