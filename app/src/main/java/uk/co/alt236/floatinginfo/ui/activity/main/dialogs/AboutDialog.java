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
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import uk.co.alt236.floatinginfo.BuildConfig;
import uk.co.alt236.floatinginfo.R;

public class AboutDialog extends DialogFragment {

    public AboutDialog() {
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View content = inflater.inflate(R.layout.dialog_about, null, false);
        final TextView version = content.findViewById(R.id.version);

        final String name = BuildConfig.VERSION_NAME;
        version.setText(getString(R.string.version) + " " + name);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.app_name)
                .setView(content)
                .setPositiveButton(R.string.ok, (dialog, whichButton) -> dialog.dismiss())
                .create();
    }
}
