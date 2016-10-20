package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

import android.content.Context;

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
        final String ssid = mNetworkInfo.getCurrentSsid();
        final android.net.NetworkInfo networkInfo = mNetworkInfo.getActiveNetInfo();
        mNetData = new NetData(networkInfo, proxyInfo, ssid);
    }

    public NetData getNetData() {
        return mNetData;
    }
}
