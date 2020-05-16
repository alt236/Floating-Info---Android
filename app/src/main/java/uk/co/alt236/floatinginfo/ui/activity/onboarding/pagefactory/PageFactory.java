/*
 * Copyright 2016 Alexandros Schillings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.alt236.floatinginfo.ui.activity.onboarding.pagefactory;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.github.dreierf.materialintroscreen.SlideFragment;
import io.github.dreierf.materialintroscreen.SlideFragmentBuilder;
import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.permissions.AndroidPermissionChecker;
import uk.co.alt236.floatinginfo.permissions.OverlayPermissionChecker;
import uk.co.alt236.floatinginfo.permissions.PermissionChecker;
import uk.co.alt236.floatinginfo.permissions.UsageStatsPermissionChecker;

public class PageFactory {

    private final Context mContext;

    public PageFactory(final Context context) {
        mContext = context.getApplicationContext();
    }

    public final List<SlideFragment> getPages() {
        final List<SlideFragment> retVal = new ArrayList<>();

        addOverlayPermissionPage(retVal);
        addUsageStatsPage(retVal);
        addAndroidPermissionsPage(retVal);

        if (!retVal.isEmpty()) {
            addExplanationPage(retVal);
        }

        return retVal;
    }


    private void addExplanationPage(final List<SlideFragment> list) {
        final String title = mContext.getString(R.string.app_name);

        final String description = mContext.getString(R.string.message_onboarding_explanation);

        final SlideFragment fragment = new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .title(title)
                .description(description)
                .build();

        // Explanation goes first
        list.add(0, fragment);
    }

    private void addAndroidPermissionsPage(final List<SlideFragment> list) {
        final AndroidPermissionChecker logic = new AndroidPermissionChecker(mContext);

        if (logic.isNeeded()) {
            final String title = mContext.getString(R.string.slide_android_title);
            final String description = mContext.getString(R.string.slide_android_permissions);

            final SlideFragment fragment = new SlideFragmentBuilder()
                    .backgroundColor(R.color.colorPrimary)
                    .buttonsColor(R.color.colorAccent)
                    .neededPermissions(logic.getRequiredPermissions())
                    .description(description)
                    .description(title)
                    .build();

            list.add(fragment);
        }
    }

    private void addOverlayPermissionPage(final List<SlideFragment> list) {
        final PermissionChecker logic = new OverlayPermissionChecker(mContext);

        if (logic.isNeeded()) {
            final SlideFragment fragment = new OverlayPermsissionPage();
            list.add(fragment);
        }
    }

    private void addUsageStatsPage(final List<SlideFragment> list) {
        final PermissionChecker logic = new UsageStatsPermissionChecker(mContext);

        if (logic.isNeeded()) {
            final SlideFragment fragment = new UsageStatsPage();
            list.add(fragment);
        }
    }

}
