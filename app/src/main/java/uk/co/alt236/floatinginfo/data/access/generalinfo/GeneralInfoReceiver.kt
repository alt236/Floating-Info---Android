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
package uk.co.alt236.floatinginfo.data.access.generalinfo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class GeneralInfoReceiver(private val mCallbacks: Callbacks) : BroadcastReceiver() {
    val intentFilter: IntentFilter
        get() {
            val f = IntentFilter()
            f.addAction(ACTION_PLAY)
            f.addAction(ACTION_PAUSE)
            f.addAction(ACTION_CLEAR)
            f.addAction(ACTION_SHARE)
            return f
        }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        when {
            ACTION_PLAY == action -> mCallbacks.onLogResume()
            ACTION_PAUSE == action -> mCallbacks.onLogPause()
            ACTION_CLEAR == action -> mCallbacks.onLogClear()
            ACTION_SHARE == action -> mCallbacks.onLogShare()
        }
    }

    interface Callbacks {
        fun onLogPause()
        fun onLogResume()
        fun onLogClear()
        fun onLogShare()
    }

    companion object {
        @JvmField
        val ACTION_PLAY = GeneralInfoReceiver::class.java.name + ".ACTION_PLAY"

        @JvmField
        val ACTION_PAUSE = GeneralInfoReceiver::class.java.name + ".ACTION_PAUSE"

        @JvmField
        val ACTION_CLEAR = GeneralInfoReceiver::class.java.name + ".ACTION_CLEAR"

        @JvmField
        val ACTION_SHARE = GeneralInfoReceiver::class.java.name + ".ACTION_SHARE"
    }

}