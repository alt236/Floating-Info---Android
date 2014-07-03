/*******************************************************************************
 * Copyright 2014 Alexandros Schillings
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package uk.co.alt236.floatinginfo.provider.generalinfo.asynctask;

import java.util.List;

import uk.co.alt236.floatinginfo.util.Constants;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;

public class ProcessMonitorAsyncTask extends AsyncTask<Void, ForegroundProcessInfo, Void> {

	private static final String UNKNOWN_APP_NAME = "???";

	private ActivityManager mActivityManager;
	private PackageManager mPackageManager;

	public ProcessMonitorAsyncTask(Context context) {
		mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		mPackageManager = context.getApplicationContext().getPackageManager();
	}

	@Override
	protected Void doInBackground(Void... voids) {
		ForegroundProcessInfo p;

		while (!isCancelled()) {
			p = getForegroundApp(mActivityManager);

			publishProgress(p);

			try {
				Thread.sleep(Constants.PROC_MONITOR_SLEEP);
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
