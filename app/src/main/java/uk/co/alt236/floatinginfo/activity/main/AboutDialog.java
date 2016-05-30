package uk.co.alt236.floatinginfo.activity.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import uk.co.alt236.floatinginfo.BuildConfig;
import uk.co.alt236.floatinginfo.R;

/**
 *
 */
public class AboutDialog extends DialogFragment {

    public AboutDialog() {
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View content = inflater.inflate(R.layout.dialog_about, null, false);
        final TextView version = (TextView) content.findViewById(R.id.version);

        final String name = BuildConfig.VERSION_NAME;
        version.setText(getString(R.string.version) + " " + name);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.app_name)
                .setView(content)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }
}
