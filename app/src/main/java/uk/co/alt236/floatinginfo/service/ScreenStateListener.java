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

package uk.co.alt236.floatinginfo.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/*package*/ class ScreenStateListener {

    private final Context mContext;
    private final IntentFilter mFilter;
    private final BroadcastReceiver mReceiver;

    public ScreenStateListener(final Context context,
                               final OnScreenStateListener listener) {
        mContext = context;

        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_SCREEN_ON);
        mFilter.addAction(Intent.ACTION_SCREEN_OFF);

        mReceiver = new ScreenReceiver(listener);
    }

    public void register() {
        mContext.registerReceiver(mReceiver, mFilter);
    }

    public void unregister() {
        mContext.unregisterReceiver(mReceiver);
    }

    public interface OnScreenStateListener {
        void onScreenOn();

        void onScreenOff();
    }

    private static class ScreenReceiver extends BroadcastReceiver {
        private final OnScreenStateListener mListener;

        public ScreenReceiver(final OnScreenStateListener listener) {
            mListener = listener;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
                mListener.onScreenOff();
            } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                mListener.onScreenOn();
            }
        }
    }
}
