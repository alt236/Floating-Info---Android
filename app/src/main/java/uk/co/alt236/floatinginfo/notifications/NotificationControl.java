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

package uk.co.alt236.floatinginfo.notifications;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import uk.co.alt236.floatinginfo.R;
import uk.co.alt236.floatinginfo.data.access.generalinfo.GeneralInfoReceiver;
import uk.co.alt236.floatinginfo.ui.activity.main.MainActivity;
import uk.co.alt236.floatinginfo.ui.activity.share.ShareActivity;

public class NotificationControl {
    private static final int NOTIFICATION_ID = 1138;
    private final NotificationManagerCompat mNotificationManagerCompat;
    private final NotificationChannelFactory.NotificationChannelWrapper mChannelWrapper;
    private final Service mContext;

    public NotificationControl(final Service service) {
        Log.d(getClass().getSimpleName(), "new");
        final NotificationManager notificationManager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManagerCompat = NotificationManagerCompat.from(service);
        mChannelWrapper = new NotificationChannelFactory().create(service);
        mChannelWrapper.register(notificationManager);
        mContext = service;
    }

    public void dismiss() {
        Log.d(getClass().getSimpleName(), "dismiss");
        mNotificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    public void show(boolean loggingPaused) {
        final Notification notification = createNotification(loggingPaused);

        if (isServiceRunningInForeground(mContext.getClass())) {
            Log.d(getClass().getSimpleName(), "Updating!");
            mNotificationManagerCompat.notify(NOTIFICATION_ID, notification);
        } else {
            Log.d(getClass().getSimpleName(), "Showing notification!");
            // This DOES NOT start the service. It only shows the notification.
            // Thse service should have already been started
            mContext.startForeground(NOTIFICATION_ID, notification);
        }
    }

    private Notification createNotification(boolean loggingPaused) {
        final NotificationCompat.Style style = new NotificationCompat.BigTextStyle()
                .bigText(mContext.getString(R.string.notification_big_text));

        final NotificationCompat.Builder builder = new NotificationCompat
                .Builder(mContext, mChannelWrapper.getChannelName())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(style)
                .setSmallIcon(R.drawable.ic_stat_main)
                .setContentTitle(mContext.getString(R.string.notification_title))
                .setContentText(mContext.getString(R.string.notification_small_text))
                .setContentIntent(getNotificationIntent(null))
                .setOnlyAlertOnce(true)
                .setLocalOnly(true)
                .setSound(null)
                .setVibrate(null)
                .setOngoing(true);

        if (loggingPaused) {
            builder.addAction(
                    R.drawable.ic_stat_play,
                    mContext.getString(R.string.statusbar_play),
                    getNotificationIntent(GeneralInfoReceiver.ACTION_PLAY));
        } else {
            builder.addAction(
                    R.drawable.ic_stat_pause,
                    mContext.getString(R.string.statusbar_pause),
                    getNotificationIntent(GeneralInfoReceiver.ACTION_PAUSE));
        }

//		mBuilder.addAction(
//				R.drawable.ic_stat_clear,
//				getString(R.string.statusbar_clear),
//				getNotificationIntent(GeneralInfoReceiver.ACTION_CLEAR));

        builder.addAction(
                R.drawable.ic_stat_share,
                mContext.getString(R.string.statusbar_share),
                getNotificationIntent(GeneralInfoReceiver.ACTION_SHARE));

        return builder.build();
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

    private boolean isServiceRunningInForeground(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }
}
