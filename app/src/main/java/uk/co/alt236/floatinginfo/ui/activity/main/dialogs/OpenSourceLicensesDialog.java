package uk.co.alt236.floatinginfo.ui.activity.main.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.WebView;

import uk.co.alt236.floatinginfo.R;

public class OpenSourceLicensesDialog extends DialogFragment {

    public static final String LICENSES_FILE = "file:///android_asset/licenses.html";

    public OpenSourceLicensesDialog() {
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final WebView webView = new WebView(getActivity());
        webView.loadUrl(LICENSES_FILE);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.open_source_licences)
                .setView(webView)
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
