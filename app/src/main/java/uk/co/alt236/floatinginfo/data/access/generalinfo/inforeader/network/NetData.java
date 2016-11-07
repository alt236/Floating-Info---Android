package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;

/**
 *
 */
public class NetData {
    private final String mProxy;
    private final android.net.NetworkInfo mNetworkInfo;
    private final WifiInfo mWifiInfo;

    public NetData(final NetworkInfo networkInfo, final WifiInfo wifiInfo, final String proxy) {
        mProxy = proxy;
        mNetworkInfo = networkInfo;
        mWifiInfo = wifiInfo;
    }

    public String getProxy() {
        return mProxy;
    }

    public WifiInfo getWifiInfo() {
        return mWifiInfo;
    }

    public android.net.NetworkInfo getNetworkInfo() {
        return mNetworkInfo;
    }
}
