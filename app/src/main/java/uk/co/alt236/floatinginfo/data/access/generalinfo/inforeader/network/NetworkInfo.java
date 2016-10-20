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
    public String getCurrentSsid() {
        final String ssid;

        if (mWifiManager == null) {
            ssid = null;
        } else {
            final WifiInfo info = mWifiManager.getConnectionInfo();
            if (info == null) {
                ssid = null;
            } else {
                ssid = info.getSSID();
            }
        }

        return ssid;
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
