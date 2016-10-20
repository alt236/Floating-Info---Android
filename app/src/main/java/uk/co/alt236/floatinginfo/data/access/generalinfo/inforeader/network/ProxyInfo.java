package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

import android.content.Context;
import android.net.ConnectivityManager;


/*package*/ class ProxyInfo {

    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;

    public ProxyInfo(final Context context) {
        mContext = context.getApplicationContext();
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public String getProxyUrl() {
        String proxyHost = null;
        String proxyPort = null;

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                if (mConnectivityManager != null) {
                    final android.net.ProxyInfo proxyInfo = mConnectivityManager.getDefaultProxy();
                    if (proxyInfo != null) {
                        proxyHost = proxyInfo.getHost();
                        proxyPort = String.valueOf(proxyInfo.getPort());
                    }
                }
            } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                proxyHost = System.getProperty("http.proxyHost");
                proxyPort = System.getProperty("http.proxyPort");
            } else {
                proxyHost = android.net.Proxy.getHost(mContext);
                proxyPort = String.valueOf(android.net.Proxy.getPort(mContext));
            }

        } catch (final Exception ex) {
            proxyHost = null;
            proxyPort = null;
        }

        final String proxyAddress;
        if (proxyHost != null) {
            proxyAddress = proxyHost + ":" + proxyPort;
        } else {
            proxyAddress = null;
        }

        return proxyAddress;
    }
}
