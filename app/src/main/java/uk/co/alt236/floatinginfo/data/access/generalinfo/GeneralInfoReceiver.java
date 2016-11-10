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
package uk.co.alt236.floatinginfo.data.access.generalinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class GeneralInfoReceiver extends BroadcastReceiver {

    public static final String ACTION_PLAY = GeneralInfoReceiver.class.getName() + ".ACTION_PLAY";
    public static final String ACTION_PAUSE = GeneralInfoReceiver.class.getName() + ".ACTION_PAUSE";
    public static final String ACTION_CLEAR = GeneralInfoReceiver.class.getName() + ".ACTION_CLEAR";
    public static final String ACTION_SHARE = GeneralInfoReceiver.class.getName() + ".ACTION_SHARE";
    private final Callbacks mCallbacks;

    public GeneralInfoReceiver(final Callbacks callbacks) {
        mCallbacks = callbacks;
    }

    @SuppressWarnings("MethodMayBeStatic")
    public IntentFilter getIntentFilter() {
        final IntentFilter f = new IntentFilter();
        f.addAction(ACTION_PLAY);
        f.addAction(ACTION_PAUSE);
        f.addAction(ACTION_CLEAR);
        f.addAction(ACTION_SHARE);
        return f;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();
        if (ACTION_PLAY.equals(action)) {
            mCallbacks.onLogResume();
        } else if (ACTION_PAUSE.equals(action)) {
            mCallbacks.onLogPause();
        } else if (ACTION_CLEAR.equals(action)) {
            mCallbacks.onLogClear();
        } else if (ACTION_SHARE.equals(action)) {
            mCallbacks.onLogShare();
        }
    }

    public interface Callbacks {
        void onLogPause();

        void onLogResume();

        void onLogClear();

        void onLogShare();
    }
}
