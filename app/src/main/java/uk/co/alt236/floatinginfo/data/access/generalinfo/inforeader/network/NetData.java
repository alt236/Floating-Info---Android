package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

/**
 *
 */
public class NetData {
    private final String mProxy;
    private final String mSsid;

    public NetData(final String proxy, final String ssid) {
        mProxy = proxy;
        mSsid = ssid;
    }

    public String getProxy() {
        return mProxy;
    }

    public String getSsid() {
        return mSsid;
    }
}
