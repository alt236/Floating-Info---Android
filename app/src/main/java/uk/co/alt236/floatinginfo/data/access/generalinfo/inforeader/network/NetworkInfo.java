package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;

/*package*/ class NetworkInfo {

    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private final WifiManager mWifiManager;

    public NetworkInfo(final Context context) {
        mContext = context.getApplicationContext();
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    @Nullable
    public WifiInfo getCurrentWifiInfo() {

        final WifiInfo retVal;
        if (mWifiManager == null) {
            retVal = null;
        } else {
            retVal = mWifiManager.getConnectionInfo();
        }

        return retVal;
    }

    @Nullable
    public android.net.NetworkInfo getActiveNetInfo() {
        final android.net.NetworkInfo retVal;

        if (mConnectivityManager == null) {
            retVal = null;
        } else {
            retVal = mConnectivityManager.getActiveNetworkInfo();
        }

        return retVal;
    }
}
