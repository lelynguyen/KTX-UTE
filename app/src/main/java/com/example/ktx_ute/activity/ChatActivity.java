package com.example.ktx_ute.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.ktx_ute.FirebaseUtility;
import com.example.ktx_ute.Global;
import com.example.ktx_ute.R;
import com.example.ktx_ute.RetrofitUtility;
import com.example.ktx_ute.StudentData;
import com.example.ktx_ute.adapter.MessageAdapter;
import com.example.ktx_ute.interfacee.ITask;
import com.example.ktx_ute.model.ChatModel;
import com.example.ktx_ute.model.DataMessage;
import com.example.ktx_ute.model.DateModel;
import com.example.ktx_ute.model.IFCMService;
import com.example.ktx_ute.model.Message;
import com.example.ktx_ute.model.PushNotification;
import com.example.ktx_ute.model.PushNotificationWrapper;
import com.example.ktx_ute.model.ResponseMessage;
import com.example.ktx_ute.model.UserToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;
import com.vdurmont.emoji.EmojiParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface IChatRoomService {
    @GET("get_sinhvientrongphong.php")
    Call<String> getStudent(@Query("phongID") int roomID);

    @GET("get_tinnhanmoi.php")
    Call<List<ResponseMessage>> getNewMessage(@Query("phongID") int roomID, @Query("lastUpdateID") int lastUpdateID);

    @FormUrlEncoded
    @POST("insert_tinnhan.php")
    Call<List<ResponseMessage>> insertMessage(
            @Field("sinhVienID") String studentID,
            @Field("phongID") String roomID,
            @Field("noiDung") String content,
            @Field("thoiGian") String createdTime
        );
}

public class ChatActivity extends AppCompatActivity {
    private final String SHARED_PREFS = "Settings";
    private final String SETTING_NOTIFICATION = "Notification";

    private final String MESSAGE_ID_COLUMN = "tinNhanID";
    private final String STUDENT_ID_COLUMN = "sinhVienID";
    private final String STUDENT_NAME_COLUMN = "hoTenSV";
    private final String ROOM_ID_COLUMN = "phongID";
    private final String CONTENT_COLUMN = "noiDung";
    private final String CREATED_TIME_COLUMN = "thoiGian";

//    private final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final String DATE_FORMAT = "dd/MM/yyyy";
    private final String TIME_FORMAT = "HH:mm";
    private final String DATETIME_FORMAT = "HH:mm dd/MM/yyyy";

    private TextView textViewRoomName;
    private RecyclerView recyclerView;
    private EditText messageInput;
    private FrameLayout emojiButton, sendButton, backButton, notificationButton;
    private ImageView notificationImageView;
    private MessageAdapter messageAdapter;
    private List<ChatModel> chatModels;

//    private RequestQueue requestQueue;
//    private Handler handler;
//    private Runnable runnable;

    private SimpleDateFormat sdf = new SimpleDateFormat();

    private boolean isNotificationEnable = true;
    private List<String> tokens;

    private DatabaseReference databaseReference;

    private int userID;
    private String username = "";
    private int roomID;
    private int roomNumber;

    private int lastUpdateChatModelIndex = 0;
    private int lastUpdateMessageID = 0;

    private HashMap<Integer, String> studentNames = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Chat", "Start chat room");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_chat);
        EmojiManager.install(new GoogleEmojiProvider());
        Global.registerService(this);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);
        isNotificationEnable = sharedPreferences.getBoolean(SETTING_NOTIFICATION, true);

        userID = Global.getService(StudentData.class).getUserID();
        username = Global.getService(StudentData.class).getFullname();
        roomID = Global.getService(StudentData.class).getRoomID();
        roomNumber = Global.getService(StudentData.class).getRoomNumber();

        findView();
        setViewAction();
        bindRecycleView();
        updateNotificationDisplay();

        updateRoomName();

//        Global.getInstance().makeToast("Đang tải");
        cacheStudentNames(new ITask() {
            @Override
            public void onComplete() {
                updateNewMessage();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.startChatRoom();
//        handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Global.stopChatRoom();
//        handler.removeCallbacks(runnable);
    }

    public void exit() {
        Intent intent = new Intent(this, MenuOptionsSvActivity.class);
        startActivity(intent);
        finish();
    }

    private void findView() {
        textViewRoomName = findViewById(R.id.textViewRoomName);
        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.editTextMessage);
        emojiButton = findViewById(R.id.buttonEmoji);
        sendButton = findViewById(R.id.buttonSend);
        backButton = findViewById(R.id.buttonBack);
        notificationButton = findViewById(R.id.buttonNotification);
        notificationImageView = findViewById(R.id.imageNotification);
    }

    private void setViewAction() {
        EmojiPopup emojiPopup = EmojiPopup.Builder.fromRootView(
                findViewById(R.id.root)
        ).build(messageInput);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        emojiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emojiPopup.toggle();
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleNotification();
            }
        });
    }

    private void bindRecycleView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatModels = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, userID, chatModels);
        recyclerView.setAdapter(messageAdapter);
    }

    private void toggleNotification() {
        isNotificationEnable = !isNotificationEnable;
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SETTING_NOTIFICATION, isNotificationEnable);
        editor.apply();
        updateNotificationDisplay();
    }

    private void updateNotificationDisplay() {
        if (isNotificationEnable) {
            notificationImageView.setImageResource(R.drawable.ic_action_notification_on);
        } else {
            notificationImageView.setImageResource(R.drawable.ic_action_notification_off);
        }
    }

    private void cacheStudentNames(ITask task) {
        IChatRoomService chatRoomService = RetrofitUtility.getService(IChatRoomService.class);
        chatRoomService.getStudent(roomID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                Log.e("RetrofitResponse", response.body().toString());
                try {
                    JSONArray jsonArray = new JSONArray(response.body().toString());

                    int id;
                    String name;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        id = jsonObject.getInt(STUDENT_ID_COLUMN);
                        name = jsonObject.getString(STUDENT_NAME_COLUMN);

                        studentNames.put(id, name);
                    }

                    if (task != null) {
                        task.onComplete();
                    }
                } catch (Exception ex) {
                    Log.e("RetrofitException", ex.toString());
                    Global.getInstance().makeToast("Lỗi");
                    exit();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Global.getInstance().makeToast("Lỗi");
                exit();
            }
        });
    }

    private void updateRoomName() {
        textViewRoomName.setText("Phòng " + roomNumber);
    }

//    private void initPolling() {
//        handler = new Handler();
//
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                updateNewMessage();
//                handler.postDelayed(this, 5000);
//                Log.e("VolleyPolling", "Update");
//            }
//        };
//
//        handler.post(runnable);
//    }

    private String getStudentName(int id) {
        return studentNames.get(id);
    }

    private Date getDateObject(String value) throws ParseException {
        sdf.applyPattern(DEFAULT_FORMAT);
        return sdf.parse(value);
    }

    private String getDateTimeString(Date dateObject, String format) {
        sdf.applyPattern(format);
        return sdf.format(dateObject);
    }

    private void sendMessage() {
        String content = messageInput.getText().toString().trim();

        if (content.equals("")) {
            return;
        }

        Date dateObject = new Date();
        String time = getDateTimeString(dateObject, TIME_FORMAT);
        String date = getDateTimeString(dateObject, DATE_FORMAT);;
        String datetime = getDateTimeString(dateObject, DATETIME_FORMAT);;

        tryAddDateModel(datetime, date);

        Message message = new Message(userID, username, content, time);
        message.setDate(date);
        message.setType(MessageAdapter.TYPE_RIGHT);
        chatModels.add(message);
        messageAdapter.notifyItemInserted(chatModels.size() - 1);

        insertData(content, dateObject);
        FirebaseUtility.sendNotification(
                ChatActivity.this,
                Global.getService(StudentData.class).getTokens(),
                new PushNotification(new DataMessage(FirebaseUtility.DataMessageType.NEW_MESSAGE))
        );

        messageInput.setText("");
        recyclerView.scrollToPosition(chatModels.size() - 1);
    }


    public void updateNewMessage() {
        Log.e("Chat", "UpdateMessage");
        IChatRoomService chatRoomService = RetrofitUtility.getService(IChatRoomService.class);
        chatRoomService.getNewMessage(roomID, lastUpdateMessageID).enqueue(new Callback<List<ResponseMessage>>() {
            @Override
            public void onResponse(Call<List<ResponseMessage>> call, Response<List<ResponseMessage>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<ResponseMessage> responseMessages = response.body();

                Collections.sort(responseMessages, new Comparator<ResponseMessage>() {
                    @Override
                    public int compare(ResponseMessage message1, ResponseMessage message2) {
                        return Integer.valueOf(message1.getTinNhanID()).compareTo(Integer.valueOf(message2.getTinNhanID()));
                    }
                });

                if (responseMessages.size() == 0) {
                    return;
                }

//                for (int i = chatModels.size() - 1; i > lastUpdateChatModelIndex; i--) {
//                    chatModels.remove(i);
//                    messageAdapter.notifyItemRemoved(i);
//                }

                try {
                    addResultToChatModels(responseMessages);
                } catch (ParseException e) {
                    Log.e("ChatException", e.toString());
                }
            }

            @Override
            public void onFailure(Call<List<ResponseMessage>> call, Throwable t) {
                Global.getInstance().makeToast("Lỗi");
            }
        });
    }

    private void addResultToChatModels(List<ResponseMessage> responseMessages) throws ParseException {
        int studentID = 0;
        String studentName = null;
        String content = null;
        String time = null, date = null, datetime = null;
        Date dateObject;

        int startIndex = chatModels.size();

        for (int i = 0; i < responseMessages.size(); i++) {
            ResponseMessage responseMessage = responseMessages.get(i);

            studentID = Integer.parseInt(responseMessage.getSinhVienID());
            studentName = getStudentName(studentID);
            content = decodeMessage(responseMessage.getNoiDung());

            date = "";
            time = "";
            if (responseMessage.getThoiGian() != null) {
                dateObject = getDateObject(responseMessage.getThoiGian());
                time = getDateTimeString(dateObject, TIME_FORMAT);
                date = getDateTimeString(dateObject, DATE_FORMAT);;
                datetime = getDateTimeString(dateObject, DATETIME_FORMAT);

                tryAddDateModel(datetime, date);
            }

            Message message = new Message(studentID, studentName, content, time);
            message.setDate(date);
            message.setType(getMessageModelType(studentID, studentName));
            chatModels.add(message);
        }

        messageAdapter.notifyItemRangeInserted(startIndex, responseMessages.size());

        recyclerView.scrollToPosition(chatModels.size() - 1);
        lastUpdateChatModelIndex = chatModels.size() - 1;
        ResponseMessage responseMessage = responseMessages.get(responseMessages.size() - 1);
        lastUpdateMessageID = Integer.parseInt(responseMessage.getTinNhanID());
    }

    private int getMessageModelType(int senderID, String senderName) {
        if (senderID == userID) {
            return MessageAdapter.TYPE_RIGHT;
        }
        if (chatModels.size() == 0) {
            return MessageAdapter.TYPE_LEFT_START;
        }
        if (chatModels.get(chatModels.size() - 1).getType() == MessageAdapter.TYPE_DATE) {
            return MessageAdapter.TYPE_LEFT_START;
        }
        if (((Message)chatModels.get(chatModels.size() - 1)).getUsername() != null
                && senderName.equals( ((Message)chatModels.get(chatModels.size() - 1)).getUsername() )) {
            return MessageAdapter.TYPE_LEFT;
        }
        return MessageAdapter.TYPE_LEFT_START;
    }

    private void tryAddDateModel(String datetime, String date) {
        if (chatModels.size() == 0) {
            addNewDateModel(datetime, date);
        } else {
            String preDate = chatModels.get(chatModels.size() - 1).getDate();
            if (!preDate.equals(date)) {
                addNewDateModel(datetime, date);
            }
        }
    }

    private void addNewDateModel(String datetime, String date) {
        DateModel dateModel = new DateModel(datetime);
        dateModel.setDate(date);
        dateModel.setType(MessageAdapter.TYPE_DATE);
        chatModels.add(dateModel);
//        messageAdapter.notifyItemInserted(chatModels.size() - 1);
    }

    private void insertData(String content, Date date) {
        IChatRoomService chatRoomService = RetrofitUtility.getService(IChatRoomService.class);
        sdf.applyPattern(DEFAULT_FORMAT);
        chatRoomService.insertMessage(
                String.valueOf(userID),
                String.valueOf(roomID),
                encodeMessage(content),
                sdf.format(date)
        ).enqueue(new Callback<List<ResponseMessage>>() {
            @Override
            public void onResponse(Call<List<ResponseMessage>> call, Response<List<ResponseMessage>> response) {
                Log.e("RetrofitResponse", response.body().get(0).getTinNhanID());
                lastUpdateMessageID = Integer.parseInt(response.body().get(0).getTinNhanID());
            }

            @Override
            public void onFailure(Call<List<ResponseMessage>> call, Throwable t) {
                Log.e("RetrofitFail", t.toString());
            }
        });
    }

    private String encodeMessage(String message) {
        return EmojiParser.parseToAliases(message);
    }

    private String decodeMessage(String message) {
        return EmojiParser.parseToUnicode(message);
    }
}