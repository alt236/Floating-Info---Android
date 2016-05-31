package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 *
 */
/*package*/ abstract class FgAppDiscovery {
    protected static final String UNKNOWN_APP_NAME = "???";
    protected static final String UNKNOWN_PKG_NAME = "???";
    protected static final ForegroundAppData FALLBACK_APP_INFO
            = new ForegroundAppData(-1, UNKNOWN_PKG_NAME, UNKNOWN_APP_NAME);

    private final PackageManager mPackageManager;

    public FgAppDiscovery(final Context context) {
        mPackageManager = context.getApplicationContext().getPackageManager();
    }

    protected CharSequence getAppName(final String packageName) {
        ApplicationInfo ai;

        try {
            ai = mPackageManager.getApplicationInfo(packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }

        return (ai != null ? mPackageManager.getApplicationLabel(ai) : UNKNOWN_APP_NAME);
    }

    public abstract ForegroundAppData getForegroundApp();
}
