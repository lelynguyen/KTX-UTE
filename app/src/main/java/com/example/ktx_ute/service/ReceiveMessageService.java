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
import com.example.ktx_ute.interfacee.ITask;
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

            switch (type) {
                case FirebaseUtility.DataMessageType.NEW_MESSAGE: {
                    Log.e("FirebaseFCM", "Receive Mesage");
                    Global.getService(ChatActivity.class).updateNewMessage();
                    break;
                }
                case FirebaseUtility.DataMessageType.CHAT_MEMBER_JOIN: {
                    String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
                    String body = "Một sinh viên mới vào phòng";
                    Global.getInstance().showNotification(title, body);
                    break;
                }
                case FirebaseUtility.DataMessageType.CHAT_MEMBER_LEAVE: {
                    String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
                    String body = "Một sinh viên rời phòng";
                    Global.getInstance().showNotification(title, body);
                    break;
                }
                case FirebaseUtility.DataMessageType.STUDENT_JOIN: {
                    Global.getService(StudentData.class).updateStudentRoom(new ITask() {
                        @Override
                        public void onComplete() {
                            String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
                            String body = "Bạn được thêm vào phòng";
                            Global.getService(StudentData.class).updateRealtimeDatabase();
                            Global.getInstance().showNotification(title, body);
                        }
                    });
                    Global.getService(ChatActivity.class).exit();
                    break;
                }
                case FirebaseUtility.DataMessageType.STUDENT_LEAVE: {
                    String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
                    Global.getService(StudentData.class).updateStudentRoom(new ITask() {
                        @Override
                        public void onComplete() {
                            String body = "Bạn bị xóa khỏi phòng";
                            Global.getService(StudentData.class).updateRealtimeDatabase();
                            Global.getInstance().showNotification(title, body);
                        }
                    });
                    Global.getService(ChatActivity.class).exit();
                    break;
                }
            }

            return;
        }

        switch (type) {
            case FirebaseUtility.DataMessageType.NEW_MESSAGE: {
                String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
                String body = "Tin nhắn mới";
                Global.getInstance().showNotification(title, body);
                break;
            }
            case FirebaseUtility.DataMessageType.CHAT_MEMBER_JOIN: {
                String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
                String body = "Một sinh viên mới vào phòng";
                Global.getInstance().showNotification(title, body);
                break;
            }
            case FirebaseUtility.DataMessageType.CHAT_MEMBER_LEAVE: {
                String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
                String body = "Một sinh viên rời phòng";
                Global.getInstance().showNotification(title, body);
                break;
            }
            case FirebaseUtility.DataMessageType.STUDENT_JOIN: {
                Global.getService(StudentData.class).updateStudentRoom(new ITask() {
                    @Override
                    public void onComplete() {
                        String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
                        String body = "Bạn được thêm vào phòng";
                        Global.getService(StudentData.class).updateRealtimeDatabase();
                        Global.getInstance().showNotification(title, body);
                    }
                });
                break;
            }
            case FirebaseUtility.DataMessageType.STUDENT_LEAVE: {
                String title = "Phòng " + Global.getInstance().getSharedPreferencesValue("Student", "roomNumber", "0");
                Global.getService(StudentData.class).updateStudentRoom(new ITask() {
                    @Override
                    public void onComplete() {
                        String body = "Bạn bị xóa khỏi phòng";
                        Global.getService(StudentData.class).updateRealtimeDatabase();
                        Global.getInstance().showNotification(title, body);
                    }
                });
                break;
            }
        }

//        if (message.getNotification() != null) {
//            Log.d("FirebaseMessage", "Message Notification Body: " + message.getNotification().getBody());
//            makeToast(message.getFrom(), message.getNotification().getBody());
//        }
    }

}
