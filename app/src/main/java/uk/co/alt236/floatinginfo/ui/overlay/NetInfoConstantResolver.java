package uk.co.alt236.floatinginfo.ui.overlay;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

/**
 *
 */
/*package*/ class NetInfoConstantResolver {

    public String getNetworkType(@NonNull final NetworkInfo info) {
        final String retVal;


        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                retVal = "WIFI";
                break;
            case ConnectivityManager.TYPE_BLUETOOTH:
                retVal = "BLUETOOTH";
                break;
            case ConnectivityManager.TYPE_DUMMY:
                retVal = "DUMMY";
                break;
            case ConnectivityManager.TYPE_ETHERNET:
                retVal = "ETHERNET";
                break;
            case ConnectivityManager.TYPE_MOBILE:
                retVal = getMobileType(info);
                break;
            case ConnectivityManager.TYPE_MOBILE_DUN:
                retVal = "MOBILE DUN";
                break;
            case ConnectivityManager.TYPE_VPN:
                retVal = "VPN";
                break;
            case ConnectivityManager.TYPE_WIMAX:
                retVal = "WIMAX";
                break;
            default:
                retVal = "???";
        }


        return retVal;
    }

    private String getMobileType(final NetworkInfo info) {
        final String retVal;

        final String prefix = "MOBILE/";
        final int subType = info.getSubtype();
        final String subtypeAsString;

        switch (subType) {
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                subtypeAsString = "UNKNOWN";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                subtypeAsString = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                subtypeAsString = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                subtypeAsString = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                subtypeAsString = "1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                subtypeAsString = "IDEN";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                subtypeAsString = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                subtypeAsString = "EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                subtypeAsString = "EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                subtypeAsString = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                subtypeAsString = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                subtypeAsString = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                subtypeAsString = "EVDO_B";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                subtypeAsString = "EHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                subtypeAsString = "HSPAP";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                subtypeAsString = "LTE";
                break;
            default:
                subtypeAsString = "???";
                break;
        }

        if (info.isRoaming()) {
            retVal = prefix + subtypeAsString + "/ROAMING";
        } else {
            retVal = prefix + subtypeAsString;
        }

        return retVal;
    }
}
