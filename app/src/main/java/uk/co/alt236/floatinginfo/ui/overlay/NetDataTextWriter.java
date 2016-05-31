package uk.co.alt236.floatinginfo.ui.overlay;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.network.NetData;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/**
 *
 */
/*package*/ class NetDataTextWriter implements TextWriter<NetData> {
    @Override
    public void writeText(final NetData input, final StringBuilderHelper sb) {
        if (input != null) {
            final String proxy = input.getProxy() == null ? "OFF" : input.getProxy();

            sb.appendBold("Network Info");
            sb.startKeyValueSection();
            sb.append("Proxy", proxy);
            sb.endKeyValueSection();
            sb.appendNewLine();
        }
    }

    @Override
    public void clear() {
        // NOOP
    }
}
