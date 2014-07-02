package uk.co.alt236.floatinginfo.provider.generalinfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.activity.MainActivity;
import uk.co.alt236.floatinginfo.provider.BaseProvider;
import uk.co.alt236.floatinginfo.provider.generalinfo.asynctask.ForegroundProcessInfo;
import uk.co.alt236.floatinginfo.provider.generalinfo.asynctask.ProcessMonitorAsyncTask;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.InfoStore;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.cpu.CpuUtilisationReader;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.memory.MemoryData;
import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.memory.MemoryInfoReader;
import uk.co.alt236.floatinginfo.util.FloatingInfoReceiver;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;
import uk.co.alt236.floatinginfo.util.Util;
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

	private static final int NOTIFICATION_ID = 1138;
	private static final String TAG = "GeneralInfoProvider";

	private final CpuUtilisationReader mCpuUtilisationReader;
	private final AtomicInteger mForegroundAppPid = new AtomicInteger();
	private final InfoStore mInfoStore;
	private final AtomicBoolean mIsLogPaused = new AtomicBoolean(false);
	private final FloatingInfoReceiver mLogReceiver;
	private final MemoryInfoReader mMemoryInfoReader;
	private final NotificationManager mNotificationManager;
	private final SharedPreferences mPrefs;
	private ProcessMonitorAsyncTask mProcessMonitorTask;
	private TextView mTextView;

	private Handler mViewUpdateHandler = new Handler();

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
		setTextSize();
		setTextColor();
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
				sb.append("DalvikPrivateClean", Util.getHumanReadableKiloByteCount(memoryInfo.getDalvikPrivateClean(), true));
				sb.append("DalvikPrivateDirty", Util.getHumanReadableKiloByteCount(memoryInfo.getDalvikPrivateDirty(), true));
				sb.append("DalvikPss", Util.getHumanReadableKiloByteCount(memoryInfo.getDalvikPss(), true));
				sb.append("DalvikSharedClean", Util.getHumanReadableKiloByteCount(memoryInfo.getDalvikSharedClean(), true));
				sb.append("DalvikSharedDirty", Util.getHumanReadableKiloByteCount(memoryInfo.getDalvikSharedDirty(), true));
				sb.append("DalvikSwappablePss", Util.getHumanReadableKiloByteCount(memoryInfo.getDalvikSwappablePss(), true));
				sb.append("DalvikSwappedOut", Util.getHumanReadableKiloByteCount(memoryInfo.getDalvikSwappedOut(), true));

				sb.append("NativePrivateClean", Util.getHumanReadableKiloByteCount(memoryInfo.getNativePrivateClean(), true));
				sb.append("NativePrivateDirty",Util.getHumanReadableKiloByteCount( memoryInfo.getNativePrivateDirty(), true));
				sb.append("NativePss", Util.getHumanReadableKiloByteCount(memoryInfo.getNativePss(), true));
				sb.append("NativeSharedClean", Util.getHumanReadableKiloByteCount(memoryInfo.getNativeSharedClean(), true));
				sb.append("NativeSharedDirty", Util.getHumanReadableKiloByteCount(memoryInfo.getNativeSharedDirty(), true));
				sb.append("NativeSwappablePss", Util.getHumanReadableKiloByteCount(memoryInfo.getNativeSwappablePss(), true));
				sb.append("NativeSwappedOut", Util.getHumanReadableKiloByteCount(memoryInfo.getNativeSwappedOut(), true));

				sb.append("OtherPrivateClean", Util.getHumanReadableKiloByteCount(memoryInfo.getOtherPrivateClean(), true));
				sb.append("OtherPrivateDirty", Util.getHumanReadableKiloByteCount(memoryInfo.getOtherPrivateDirty(), true));
				sb.append("OtherPss", Util.getHumanReadableKiloByteCount(memoryInfo.getOtherPss(), true));
				sb.append("OtherSharedClean", Util.getHumanReadableKiloByteCount(memoryInfo.getOtherSharedClean(), true));
				sb.append("OtherSharedDirty", Util.getHumanReadableKiloByteCount(memoryInfo.getOtherSharedDirty(), true));
				sb.append("OtherSwappablePss", Util.getHumanReadableKiloByteCount(memoryInfo.getOtherSwappablePss(), true));
				sb.append("OtherSwappedOut", Util.getHumanReadableKiloByteCount(memoryInfo.getOtherSwappedOut(), true));
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
		} else if (key.equals(getString(R.string.pref_text_opacity))) {
			setTextColor();
		} else if (key.equals(getString(R.string.pref_text_size))) {
			setTextSize();
		} else if (key.equals(getString(R.string.pref_text_color_red))) {
			setTextColor();
		} else if (key.equals(getString(R.string.pref_text_color_green))) {
			setTextColor();
		} else if (key.equals(getString(R.string.pref_text_color_blue))) {
			setTextColor();
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

	private void setTextColor(){
		final int alpha = mPrefs.getInt(
				getString(R.string.pref_text_opacity),
				getInteger(R.integer.default_text_opacity));
		final int red = mPrefs.getInt(
				getString(R.string.pref_text_color_red),
				getInteger(R.integer.default_text_red));
		final int green = mPrefs.getInt(
				getString(R.string.pref_text_color_green),
				getInteger(R.integer.default_text_green));
		final int blue = mPrefs.getInt(
				getString(R.string.pref_text_color_blue),
				getInteger(R.integer.default_text_blue));
		mTextView.setTextColor(Color.argb(alpha, red, green, blue));
	}

	private void setTextSize(){
		final int dp = 6 + mPrefs.getInt(getContext().getString(R.string.pref_text_size), 0);
        mTextView.setTextSize(dp);
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

		if (mIsLogPaused.get()) {
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
				if(!mIsLogPaused.get()){
					final boolean change; // = false;

					if (values[0].getPid() != mForegroundAppPid.get()) {
						change = true;
						mForegroundAppPid.set(values[0].getPid());
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
		final CharSequence output;

		if(clear){
			output = "";
		} else {
			output = getInfoText();
		}

		mViewUpdateHandler.post(new Runnable() {
			@Override
			public void run() {
				mTextView.setText(output);
			}
		});
	}
}
