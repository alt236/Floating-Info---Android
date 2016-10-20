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
