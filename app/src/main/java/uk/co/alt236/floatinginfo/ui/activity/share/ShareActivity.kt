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
package uk.co.alt236.floatinginfo.ui.activity.share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import uk.co.alt236.floatinginfo.data.access.generalinfo.GeneralInfoReceiver

class ShareActivity : Activity() {
    // this activity exists so we can launch the share chooser
    // from a notification action - see LogService.onLogShare()
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        // simply post the share event and finish
        val intent = Intent(GeneralInfoReceiver.ACTION_SHARE)
        sendBroadcast(intent)
        finish()
    }

}