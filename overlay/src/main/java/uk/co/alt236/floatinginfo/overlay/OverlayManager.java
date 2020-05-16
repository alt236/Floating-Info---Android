/*
 * Copyright 2020 Alexandros Schillings
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
package uk.co.alt236.floatinginfo.overlay;

import android.view.LayoutInflater;
import android.view.View;

import uk.co.alt236.floatinginfo.common.data.InfoStore;
import uk.co.alt236.floatinginfo.common.prefs.EnabledInfoPrefs;
import uk.co.alt236.floatinginfo.common.prefs.OverlayPrefs;
import uk.co.alt236.floatinginfo.common.string.StringBuilderHelper;
import uk.co.alt236.floatinginfo.overlay.writers.TextWriter;
import uk.co.alt236.floatinginfo.overlay.writers.TextWriterWrapper;

public class OverlayManager {
    private final InfoStore mInfoStore;
    private final TextOverlayController mTextOverlayController;
    private final TextWriter<InfoStore> mTextWriter;

    public OverlayManager(final LayoutInflater layoutInflater,
                          final InfoStore store,
                          final OverlayPrefs overlayPrefs,
                          final EnabledInfoPrefs enabledInfoPrefs) {
        mInfoStore = store;
        mTextWriter = new TextWriterWrapper(enabledInfoPrefs);
        mTextOverlayController = new TextOverlayController(layoutInflater, overlayPrefs);

        updateBackground();
        updateTextSize();
        updateTextColor();
        updateAlignment();
    }

    public void clearPeakUsage() {
        mTextWriter.clear();
    }

    private CharSequence getInfoText() {
        final StringBuilderHelper sb = new StringBuilderHelper();
        mTextWriter.writeText(mInfoStore, sb);
        return sb.toString().trim();
    }

    public CharSequence getSharePayload() {
        return getInfoText();
    }

    public View getView() {
        return mTextOverlayController.getView();
    }

    public void updateBackground() {
        mTextOverlayController.updateBackground();
    }

    public void updateTextColor() {
        mTextOverlayController.updateTextColor();
    }

    public void updateAlignment() {
        mTextOverlayController.updateAlignment();
    }

    public void updateTextSize() {
        mTextOverlayController.updateTextSize();
    }

    public void update() {
        mTextOverlayController.setText(getInfoText());
    }
}
