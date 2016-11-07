package uk.co.alt236.floatinginfo.ui.overlay;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import java.util.Locale;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.NetData;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/**
 *
 */
/*package*/ class NetDataTextWriter implements TextWriter<NetData> {
    private final TextWriter<WifiInfo> wifiInfoTextWriter;

    public NetDataTextWriter() {
        wifiInfoTextWriter = new WifiInfoTextWriter();
    }

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
                    wifiInfoTextWriter.writeText(input.getWifiInfo(), sb);
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


    private static class WifiInfoTextWriter implements TextWriter<WifiInfo> {
        private static final String NUMBER_WITH_UNIT = "%d %s";
        private static final String RSSI = "%d %s (%d/%d)";
        private static final int RSSI_LEVELS = 5;

        @Override
        public void writeText(final WifiInfo input, final StringBuilderHelper sb) {
            final String ssid;
            final String bssid;
            final int linkSpeed;
            final int rssi;

            if (input == null) {
                bssid = null;
                ssid = null;
                linkSpeed = 0;
                rssi = 0;
            } else {
                bssid = input.getBSSID();
                ssid = input.getSSID();
                linkSpeed = input.getLinkSpeed();
                rssi = input.getRssi();
            }

            sb.append("SSID", pretty(ssid));
            sb.append("BSSID", pretty(bssid));
            sb.append("RSSI", getRssi(input));
            sb.append("Speed", String.format(Locale.US, NUMBER_WITH_UNIT, linkSpeed, WifiInfo.LINK_SPEED_UNITS));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    && input != null) {
                final int freq = input.getFrequency();
                sb.append("Freq", String.format(Locale.US, NUMBER_WITH_UNIT, freq, WifiInfo.FREQUENCY_UNITS));
            }
        }

        private String getRssi(final WifiInfo wifiInfo) {
            final int rssi = wifiInfo.getRssi();
            final int humanValue = WifiManager.calculateSignalLevel(rssi, RSSI_LEVELS);

            return String.format(Locale.US, RSSI, rssi, "dBm", humanValue, RSSI_LEVELS);
        }

        private String pretty(final String string) {
            return string == null ? "n/a" : string;
        }

        @Override
        public void clear() {
            // NOOP
        }
    }
}
