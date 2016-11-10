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

package uk.co.alt236.floatinginfo.ui.activity.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragment;
import uk.co.alt236.floatinginfo.ui.activity.onboarding.pagefactory.PageFactory;

public class OnBoardingActivity extends MaterialIntroActivity {
    private static final String TAG = OnBoardingActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final List<SlideFragment> pages = new PageFactory(this).getPages();
        Log.d(TAG, "Number of pages: " + pages.size());

        if (pages.isEmpty()) {
            finish();
        } else {
            for (final SlideFragment page : pages) {
                addSlide(page);
            }
        }
    }
}
