package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo;

import android.content.Context;

public class ForegroundAppDiscovery {

    private final FgAppDiscovery mAppDiscovery;

    public ForegroundAppDiscovery(final Context context) {
        final Context appContext = context.getApplicationContext();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mAppDiscovery = new FgAppDiscovery21(appContext);
        } else {
            mAppDiscovery = new FgAppDiscoveryLegacy(appContext);
        }

    }

    public ForegroundAppData getForegroundApp() {
        return mAppDiscovery.getForegroundApp();
    }
}
