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
