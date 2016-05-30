package uk.co.alt236.floatinginfo.overlay;

import java.util.List;

import uk.co.alt236.floatinginfo.provider.generalinfo.inforeader.cpu.CpuData;
import uk.co.alt236.floatinginfo.util.StringBuilderHelper;

/*package*/ class CpuTextWriter implements TextWriter<CpuData> {
    @Override
    public void writeText(final CpuData input, final StringBuilderHelper sb) {
        if (input != null) {
            sb.appendBold("Global CPU Utilisation");
            sb.startKeyValueSection();
            sb.append("Total", String.valueOf(input.getOveralCpu()) + "%");
            final List<Integer> list = input.getPerCpuUtilisation();

            int count = 0;

            for (final Integer value : list) {
                sb.append("CPU" + count, String.valueOf(value) + "%");
                count++;
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
