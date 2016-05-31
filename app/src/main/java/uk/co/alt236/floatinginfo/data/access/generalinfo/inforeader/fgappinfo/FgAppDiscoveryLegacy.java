package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/*package*/ class FgAppDiscoveryLegacy extends FgAppDiscovery {

    private final ActivityManager mActivityManager;

    public FgAppDiscoveryLegacy(final Context context) {
        super(context);
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

    }

    @Override
    public ForegroundAppData getForegroundApp() {
        final ActivityManager.RunningTaskInfo foregroundTaskInfo = mActivityManager.getRunningTasks(1).get(0);
        final String pkg = foregroundTaskInfo.topActivity.getPackageName();

        final List<ActivityManager.RunningAppProcessInfo> appProcesses = mActivityManager.getRunningAppProcesses();

        for (final ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (final String ap : appProcess.pkgList) {
                    if (ap.equals(pkg)) {
                        return new ForegroundAppData(
                                appProcess.pid,
                                pkg,
                                getAppName(pkg));
                    }
                }
            }
        }

        return new ForegroundAppData(0, pkg, UNKNOWN_APP_NAME);
    }
}
