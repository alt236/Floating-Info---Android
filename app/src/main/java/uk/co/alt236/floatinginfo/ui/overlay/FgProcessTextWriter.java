package uk.co.alt236.floatinginfo.ui.overlay;

import uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo.ForegroundAppData;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/**
 *
 */
/*package*/ class FgProcessTextWriter implements TextWriter<ForegroundAppData> {
    @Override
    public void writeText(final ForegroundAppData input, final StringBuilderHelper sb) {
        if (input != null) {
            sb.appendBold("Foreground Application Info");
            sb.startKeyValueSection();
            sb.append("App Name", String.valueOf(input.getAppName()));
            sb.append("Package", input.getPackage());
            sb.append("PID", input.getPid());
            sb.endKeyValueSection();
            sb.appendNewLine();
        }
    }

    @Override
    public void clear() {
        // NOOP
    }
}
