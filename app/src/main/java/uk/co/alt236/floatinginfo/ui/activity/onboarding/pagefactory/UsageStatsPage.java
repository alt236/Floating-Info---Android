package uk.co.alt236.floatinginfo.ui.activity.onboarding.pagefactory;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import agency.tango.materialintroscreen.SlideFragment;
import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.permissions.PermissionChecker;
import uk.co.alt236.floatinginfo.permissions.UsageStatsPermissionChecker;

public class UsageStatsPage extends SlideFragment {

    private PermissionChecker mCheker;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCheker = new UsageStatsPermissionChecker(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_onboard_slide, container, false);
        final TextView title = (TextView) view.findViewById(R.id.txt_title_slide);
        final TextView text = (TextView) view.findViewById(R.id.txt_description_slide);
        final Button button = (Button) view.findViewById(R.id.button);

        title.setText(R.string.slide_usage_stats_title);
        text.setText(R.string.slide_usage_stats_description);

        button.setText(R.string.button_click_to_grant);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ask();
            }
        });

        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.colorPrimary;
    }

    @Override
    public int buttonsColor() {
        return R.color.colorAccent;
    }

    @Override
    public boolean canMoveFurther() {
        return !mCheker.isNeeded();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.message_access_needed);
    }

    private void ask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** check if we already  have permission to draw over other apps */
            if (mCheker.isNeeded()) {
                try {
                    final Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                } catch (final ActivityNotFoundException exception) {
                    final Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    final ComponentName componentName
                            = new ComponentName("com.android.settings", "com.android.settings.Settings$SecuritySettingsActivity");
                    intent.setComponent(componentName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                }
            }
        }
    }
}
