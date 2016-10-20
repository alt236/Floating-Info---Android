package uk.co.alt236.floatinginfo.permissions;

import android.app.Activity;

import java.util.Arrays;
import java.util.List;

public class PermissionCheckerWrapper {

    private final List<PermissionChecker> mPermisionLogicList;

    public PermissionCheckerWrapper(final Activity activity) {
        mPermisionLogicList = Arrays.asList(
                new AndroidPermissionChecker(activity),
                new OverlayPermissionChecker(activity),
                new UsageStatsPermissionChecker(activity)
        );
    }

    public boolean needToAsk() {
        boolean retVal = false;

        for (final PermissionChecker logic : mPermisionLogicList) {
            if (logic.isNeeded()) {
                retVal = true;
                break;
            }
        }

        return retVal;
    }


}
