package uk.co.alt236.floatinginfo.ui.overlay;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.util.Locale;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.NetData;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/**
 *
 */
/*package*/ class NetDataTextWriter implements TextWriter<NetData> {
    @Override
    public void writeText(final NetData input, final StringBuilderHelper sb) {
        if (input != null) {

            final NetworkInfo netInfo = input.getNetworkInfo();

            sb.appendBold("Network Info");
            sb.startKeyValueSection();

            if (netInfo == null) {
                sb.append("State", "Offline");
            } else {
                final NetInfoConstantResolver resolver = new NetInfoConstantResolver();
                sb.append("Type", resolver.getNetworkType(netInfo));
                sb.append("State", String.valueOf(netInfo.getState()).toLowerCase(Locale.US));

                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    final String ssid = TextUtils.isEmpty(input.getSsid()) ? "n/a" : input.getSsid();
                    sb.append("SSID", ssid);
                }

                final String proxy = TextUtils.isEmpty(input.getProxy()) ? "OFF" : input.getProxy();
                sb.append("Proxy", proxy);
            }

            sb.endKeyValueSection();
            sb.appendNewLine();
        }
    }

    @Override
    public void clear() {
        // NOOP
    }
}
