package uk.co.alt236.floatinginfo.asynctask;

import java.util.List;

import uk.co.alt236.floatinginfo.container.ForegroundProcessInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

public class ProcessMonitorAsyncTask extends AsyncTask<Void, ForegroundProcessInfo, Void> {

	private static final String UNKNOWN_APP_NAME = "???";

	private static final String TAG = "ProcessMonitorAsyncTask";

	private ActivityManager mActivityManager;
	private PackageManager mPackageManager;

	private int mPidCache = 0;

	public ProcessMonitorAsyncTask(Context context) {
		mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		mPackageManager = context.getApplicationContext().getPackageManager();
	}

	@Override
	protected Void doInBackground(Void... voids) {
		ForegroundProcessInfo p;

		while (!isCancelled()) {
			p = getForegroundApp(mActivityManager);

			if (p.getPid() != mPidCache) {
				mPidCache = p.getPid();
				Log.i(TAG, "new foreground pid = " + mPidCache);
				publishProgress(p);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		return null;
	}

	private ForegroundProcessInfo getForegroundApp(ActivityManager am) {

		final ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
		final String pkg = foregroundTaskInfo.topActivity.getPackageName();

		final List<ActivityManager.RunningAppProcessInfo> appProcesses = am.getRunningAppProcesses();

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				for (final String ap : appProcess.pkgList) {
					if (ap.equals(pkg)) {
						return new ForegroundProcessInfo(
								appProcess.pid,
								pkg,
								getAppName(pkg));
					}
				}
			}
		}

		return new ForegroundProcessInfo(0, pkg, UNKNOWN_APP_NAME);
	}

	private CharSequence getAppName(String packageName){
		ApplicationInfo ai;

		try {
		    ai = mPackageManager.getApplicationInfo( packageName, 0);
		} catch (final NameNotFoundException e) {
		    ai = null;
		}

		return (ai != null ? mPackageManager.getApplicationLabel(ai) : UNKNOWN_APP_NAME);
	}

}