package com.example.ktx_ute.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.ktx_ute.FirebaseUtility;
import com.example.ktx_ute.Global;
import com.example.ktx_ute.R;
import com.example.ktx_ute.StudentData;
import com.example.ktx_ute.activity.ChatActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class ReceiveMessageService extends FirebaseMessagingService {
    private final String SHARED_PREFS = "Settings";
    private final String SETTING_NOTIFICATION = "Notification";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if (message.getData() == null) {
            return;
        }

        Map<String, String> data = message.getData();
        int type = Integer.parseInt(data.get("type"));

        if (Global.isInForeground() && Global.isChatRoomVisible()) {
            Log.e("Lifecycle", "Foreground - In Chat Room");

            String title = "";
            String body = "";
            switch (type) {
                case FirebaseUtility.DataMessageType.NEW_MESSAGE:
                    Log.e("FirebaseFCM", "Receive Mesage");
                    Global.getService(ChatActivity.class).updateNewMessage();
                    break;
                case FirebaseUtility.DataMessageType.STUDENT_JOIN:
                    Global.getService(StudentData.class).generateTokenList();
                    title = "Phòng " + Global.getService(StudentData.class).getRoomNumber();
                    body = "Một Sinh viên mới vào phòng";
                    showNotification(title, body);
                    break;
                case FirebaseUtility.DataMessageType.STUDENT_LEAVE:
                    Global.getService(StudentData.class).generateTokenList();
                    title = "Phòng " + Global.getService(StudentData.class).getRoomNumber();
                    body = "Một sinh viên rời phòng";
                    showNotification(title, body);
                    break;
//                case FirebaseUtility.DataMessageType.UPDATE_TOKEN:
//                    Global.getService(StudentData.class).generateTokenList();
//                    break;
            }

            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean isEnable = sharedPreferences.getBoolean(SETTING_NOTIFICATION, true);
        if (!isEnable) {
            Log.e("Firebase", "Notification - OFF");
            return;
        }

        String title = "Phòng " + Global.getService(StudentData.class).getRoomNumber();
        String body = "";
        switch (type) {
            case FirebaseUtility.DataMessageType.NEW_MESSAGE:
                body = "Tin nhắn mới";
                break;
            case FirebaseUtility.DataMessageType.STUDENT_JOIN:
                body = "Một Sinh viên mới vào phòng";
//                Global.getService(StudentData.class).generateTokenList();
                break;
            case FirebaseUtility.DataMessageType.STUDENT_LEAVE:
                body = "Một sinh viên rời phòng";
//                Global.getService(StudentData.class).generateTokenList();
                break;
            default:
                return;
        }

        showNotification(title, body);


        Log.e("Lifecycle", "Show notification");

//        if (message.getNotification() != null) {
//            Log.d("FirebaseMessage", "Message Notification Body: " + message.getNotification().getBody());
//            makeToast(message.getFrom(), message.getNotification().getBody());
//        }
    }

    private void showNotification(String title, String body) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        String channelId = "fcm_default_channel";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

}
