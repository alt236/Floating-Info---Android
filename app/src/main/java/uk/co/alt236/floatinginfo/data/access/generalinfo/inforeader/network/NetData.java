package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

/**
 *
 */
public class NetData {
    private final String mProxy;
    private final String mSsid;
    private final android.net.NetworkInfo mNetworkInfo;

    public NetData(final android.net.NetworkInfo networkInfo,
                   final String proxy,
                   final String ssid) {
        mProxy = proxy;
        mSsid = ssid;
        mNetworkInfo = networkInfo;
    }

    public String getProxy() {
        return mProxy;
    }

    public String getSsid() {
        return mSsid;
    }

    public android.net.NetworkInfo getNetworkInfo() {
        return mNetworkInfo;
    }
}
