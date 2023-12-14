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
            }

            return;
        }

        boolean isEnable = (boolean) Global.getInstance().getSharedPreferencesValue("Settings", "Notification", true);
        if (!isEnable) {
            Log.e("Firebase", "Notification - OFF");
            return;
        }

        String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
        String body = "";
        switch (type) {
            case FirebaseUtility.DataMessageType.NEW_MESSAGE:
                body = "Tin nhắn mới";
                break;
            default:
                return;
        }

        Global.getInstance().showNotification(title, body);


        Log.e("Lifecycle", "Show notification");

//        if (message.getNotification() != null) {
//            Log.d("FirebaseMessage", "Message Notification Body: " + message.getNotification().getBody());
//            makeToast(message.getFrom(), message.getNotification().getBody());
//        }
    }

}
