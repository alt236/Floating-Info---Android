package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.chainfire.libsuperuser.Shell;

/*package*/ class ProcessStore {
    private final Map<Integer, String> mPidToName = new HashMap<>();
    private final Map<String, Integer> mNameToPid = new HashMap<>();

    public void update() {
        mPidToName.clear();
        mNameToPid.clear();

        final List<String> stdout = Shell.SH.run("ps");
        int lineNo = 0;
        for (final String line : stdout) {
            if (lineNo > 0) {
                // Get the process-name. It is the last column.
                final String[] arr = line.split("\\s+");
                final int pid = extractPid(arr);
                final String processName = extractProcessName(arr);
                mPidToName.put(pid, processName);
                mNameToPid.put(processName, pid);
            }
            lineNo++;
        }
    }

    public int getPidFromName(final String name) {
        final Integer pid = mNameToPid.get(name);
        if (pid == null) {
            return -1;
        } else {
            return pid;
        }
    }

    public String getNameFromPid(final int pid) {
        return mPidToName.get(pid);
    }

    private static int extractPid(final String[] line) {
        return Integer.valueOf(line[1]);
    }

    private static String extractProcessName(final String[] line) {
        return line[line.length - 1].split(":")[0];
    }
}
