package uk.co.alt236.floatinginfo.data.access.generalinfo.inforeader.fgappinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import eu.chainfire.libsuperuser.Shell;

/*package*/ class ProcessStore {
    public static final int INVALID_PID = -1;
    private static final Pattern LINE_SPLIT_PATTERN = Pattern.compile("\\s+");
    private final Map<Integer, String> mPidToName = new HashMap<>();
    private final Map<String, Integer> mNameToPid = new HashMap<>();

    private static int extractPid(final String[] line) {
        return Integer.valueOf(line[1]);
    }

    private static String extractProcessName(final String[] line) {
        return line[line.length - 1].split(":")[0];
    }

    public void update() {
        mPidToName.clear();
        mNameToPid.clear();

        final List<String> stdout = Shell.SH.run("ps");

        int lineNo = 0;
        String[] arr;
        int pid;
        String processName;
        for (final String line : stdout) {
            if (lineNo > 0) { // the first line is the header
                arr = LINE_SPLIT_PATTERN.split(line);
                pid = extractPid(arr);
                processName = extractProcessName(arr);
                mPidToName.put(pid, processName);
                mNameToPid.put(processName, pid);
            }
            lineNo++;
        }
    }

    public int getPidFromName(final String name) {
        final Integer pid = mNameToPid.get(name);
        if (pid == null) {
            return INVALID_PID;
        } else {
            return pid;
        }
    }

    public String getNameFromPid(final int pid) {
        return mPidToName.get(pid);
    }
}
