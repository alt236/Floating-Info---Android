package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.ProxyInfo;
import android.os.Build;

/**
 *
 */
public class NetDataReader {

    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private NetData mNetData;

    public NetDataReader(final Context context) {
        mContext = context.getApplicationContext();
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        update();
    }

    public void update() {
        String proxyHost = null;
        String proxyPort = null;

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                final ProxyInfo proxyInfo = mConnectivityManager.getDefaultProxy();
                if (proxyInfo != null) {
                    proxyHost = proxyInfo.getHost();
                    proxyPort = String.valueOf(proxyInfo.getPort());
                }

            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
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

        mNetData = new NetData(proxyAddress);
    }


    public NetData getNetData() {
        return mNetData;
    }
}
