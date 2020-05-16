/*
 * Copyright 2017 Alexandros Schillings
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

package uk.co.alt236.floatinginfo.data.access.generalinfo;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.ViewGroup;
import android.view.WindowManager;

/*package*/ class SystemWindowLayoutParamsFactory {
    public ViewGroup.LayoutParams getParams() {
        final int windowType = getWindowType();
        final int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        return new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                windowType,
                flags,
                PixelFormat.TRANSLUCENT
        );
    }

    private int getWindowType() {
        final int retVal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            retVal = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            retVal = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        }
        return retVal;
    }
}
