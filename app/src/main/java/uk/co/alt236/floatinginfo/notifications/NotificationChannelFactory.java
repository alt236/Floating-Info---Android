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

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import uk.co.alt236.floatinginfo.R;

/*package*/ class NotificationChannelFactory {
    private static final String CHANNEL_ID = "floating_info_control";

    @NonNull
    public NotificationChannelWrapper create(final Context context) {
        final NotificationChannel channel = createChannel(context, CHANNEL_ID);
        return new NotificationChannelWrapper(channel, CHANNEL_ID);
    }

    private NotificationChannel createChannel(final Context context,
                                              final String id) {
        final String channelName = context.getString(R.string.notification_channel_name);
        final String description = context.getString(R.string.notification_channel_description);

        final NotificationChannel channel;
        if (isAtLeastOreo()) {
            final int importance = NotificationManager.IMPORTANCE_LOW;
            channel = new NotificationChannel(id, channelName, importance);
            channel.setName(channelName);
            channel.setDescription(description);
            channel.setImportance(importance);
            channel.setSound(null, null);
            channel.enableVibration(false);
        } else {
            channel = null;
        }

        return channel;
    }

    private static boolean isAtLeastOreo() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O;
    }

    public class NotificationChannelWrapper {
        private final NotificationChannel mChannel;
        private final String mChannelName;

        public NotificationChannelWrapper(final NotificationChannel mChannel, final String mChannelName) {
            this.mChannel = mChannel;
            this.mChannelName = mChannelName;
        }

        public NotificationChannel getChannel() {
            return mChannel;
        }

        public String getChannelName() {
            return mChannelName;
        }

        public boolean hasChannel() {
            return mChannel != null;
        }

        public void register(final NotificationManager manager) {
            if (isAtLeastOreo()) {
                manager.createNotificationChannel(mChannel);
            }
        }
    }
}
