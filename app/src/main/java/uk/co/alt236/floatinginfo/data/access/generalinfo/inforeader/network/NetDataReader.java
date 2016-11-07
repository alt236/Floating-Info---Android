package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

import android.content.Context;
import android.net.wifi.WifiInfo;

/**
 *
 */
public class NetDataReader {
    private final ProxyInfo mProxyInfo;
    private final NetworkInfo mNetworkInfo;
    private NetData mNetData;

    public NetDataReader(final Context context) {
        final Context appContext = context.getApplicationContext();
        mProxyInfo = new ProxyInfo(appContext);
        mNetworkInfo = new NetworkInfo(appContext);

        update();
    }

    public void update() {
        final String proxyInfo = mProxyInfo.getProxyUrl();
        final WifiInfo wifiInfo = mNetworkInfo.getCurrentWifiInfo();
        final android.net.NetworkInfo networkInfo = mNetworkInfo.getActiveNetInfo();
        mNetData = new NetData(networkInfo, wifiInfo, proxyInfo);
    }

    public NetData getNetData() {
        return mNetData;
    }
}
