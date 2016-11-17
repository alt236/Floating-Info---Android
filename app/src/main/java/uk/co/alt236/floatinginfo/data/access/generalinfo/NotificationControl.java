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

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.ui.activity.main.MainActivity;
import uk.co.alt236.floatinginfo.ui.activity.share.ShareActivity;

/*package*/ class NotificationControl {
    private static final int NOTIFICATION_ID = 1138;
    private final NotificationManager mNotificationManager;
    private final Service mContext;

    public NotificationControl(final Service service) {
        mNotificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        mContext = service;
    }

    public void dismiss() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public void show(boolean loggingPaused) {
        final NotificationCompat.Builder mBuilder = new NotificationCompat
                .Builder(mContext)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(mContext.getString(R.string.notification_big_text)))
                .setSmallIcon(R.drawable.ic_stat_main)
                .setOngoing(true)
                .setContentTitle(mContext.getString(R.string.notification_title))
                .setContentText(mContext.getString(R.string.notification_small_text))
                .setContentIntent(getNotificationIntent(null));

        if (loggingPaused) {
            mBuilder.addAction(
                    R.drawable.ic_stat_play,
                    mContext.getString(R.string.statusbar_play),
                    getNotificationIntent(GeneralInfoReceiver.ACTION_PLAY));
        } else {
            mBuilder.addAction(
                    R.drawable.ic_stat_pause,
                    mContext.getString(R.string.statusbar_pause),
                    getNotificationIntent(GeneralInfoReceiver.ACTION_PAUSE));
        }

//		mBuilder.addAction(
//				R.drawable.ic_stat_clear,
//				getString(R.string.statusbar_clear),
//				getNotificationIntent(GeneralInfoReceiver.ACTION_CLEAR));

        mBuilder.addAction(
                R.drawable.ic_stat_share,
                mContext.getString(R.string.statusbar_share),
                getNotificationIntent(GeneralInfoReceiver.ACTION_SHARE));

        mContext.startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    private PendingIntent getNotificationIntent(final String action) {
        if (action == null) {
            final Intent intent = new Intent(mContext, MainActivity.class);
            return PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        } else if (GeneralInfoReceiver.ACTION_SHARE.equals(action)) {
            final Intent intent = new Intent(
                    mContext,
                    ShareActivity.class);
            return PendingIntent.getActivity(
                    mContext,
                    0,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);
        } else {
            final Intent intent = new Intent(action);
            return PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
    }
}
