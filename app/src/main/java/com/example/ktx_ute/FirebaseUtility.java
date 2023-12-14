package com.example.ktx_ute;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ktx_ute.model.DataMessage;
import com.example.ktx_ute.model.IFCMService;
import com.example.ktx_ute.model.PushNotification;
import com.example.ktx_ute.model.PushNotificationWrapper;
import com.example.ktx_ute.model.UserToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirebaseUtility {
    public class DataMessageType {
        public static final int NEW_MESSAGE = 0;
        public static final int STUDENT_JOIN = 1;
        public static final int STUDENT_LEAVE = 2;
        public static final int UPDATE_TOKEN = 3;
    }

    public static final String BASE_URL = "https://fcm.googleapis.com/";
    public static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    public static final String[] SCOPES = { MESSAGING_SCOPE };

    public static String getAccessToken(InputStream serviceAccount) throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(serviceAccount)
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private static DatabaseReference databaseReference;
    private static Retrofit retrofit = null;

    public static OkHttpClient getHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
                .build();
        return okHttpClient;
    }
    public static IFCMService getFCMService(OkHttpClient okHttpClient) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(FirebaseUtility.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(IFCMService.class);
    }

    public static DatabaseReference getDatabaseReference() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase
                    .getInstance("https://ql-ktx-ute-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference().child("students");
        }
        return databaseReference;
    }

//    public static void callUpdateMessage(Context context, String roomNumber, PushNotification pushNotification) {
//        List<String> tokens = new ArrayList<>();
//        getDatabaseReference().addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot data : snapshot.getChildren()) {
//                    UserToken userToken = data.getValue(UserToken.class);
//                    if (userToken.getRoomNumber().equals(roomNumber)) {
//                        String token = userToken.getToken();
//                        if (!token.isEmpty()) {
//                            tokens.add(token);
//                            sendNotification(context, tokens, pushNotification);
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private static void sendNotification(Context context, List<String> tokens, PushNotification pushNotification) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    InputStream serviceAccount = context.getAssets().open("service-account.json");
//                    String accessToken = FirebaseUtility.getAccessToken(serviceAccount);
////                    Log.e("FirebaseAccessToken", accessToken);
//                    sendNotification(accessToken, tokens, pushNotification);
//                } catch (Exception ex) {
//                    Log.e("FirebaseError", ex.toString());
//                }
//            }
//        }).start();
//    }
//
//    private static void sendNotification(String accessToken, List<String> tokens, PushNotification pushNotification) {
//        OkHttpClient okHttpClient = FirebaseUtility.getHttpClient();
//        IFCMService fcmService = FirebaseUtility.getFCMService(okHttpClient);
//
//        for (String token : tokens) {
//            if (token.isEmpty()) {
//                continue;
//            }
//            PushNotificationWrapper message = new PushNotificationWrapper(pushNotification);
//            fcmService.sendNotification(
//                    "Bearer " + accessToken,
//                    message
//            ).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                    if (response.isSuccessful()) {
//                        Log.e("FirebaseResponse", "OK");
//                    } else {
//                        Log.e("FirebaseResponse", response.body().toString());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Log.e("FirebaseError", t.getMessage());
//                }
//            });
//        }
//    }
}
