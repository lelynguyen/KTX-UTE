package com.example.ktx_ute;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ktx_ute.activity.LoginActivity;
import com.example.ktx_ute.interfacee.ITask;
import com.example.ktx_ute.model.SinhVien;
import com.example.ktx_ute.model.UserToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface IRoomService {
    @GET("get_sinhvienvaphong.php")
    Call<String> getStudentRoom(@Query("sinhVienID") int id);
}

public class StudentData {
    private int userID;
    private String fullname = "";
    private int roomID;
    private int roomNumber;
    private String currentToken = "";

    private SinhVien sinhVien;

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    private List<String> tokens = new ArrayList<>();

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean HasRoom() {
        return roomID != 0;
    }

    public void setNoRoom() {
        roomID = 0;
        roomNumber = 0;
    }

    public void setRoom(String roomID, String roomNumber) {
        this.roomID = Integer.parseInt(roomID);
        this.roomNumber = Integer.parseInt(roomNumber);
        Global.getInstance().saveSharedPreferencesValue("Student", "roomNumber", roomNumber);
    }

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }


    private ValueEventListener tokenListener;
    public void addTokenListener() {
        Log.e("Login", "add realtime database listener");
        if (tokenListener == null) {
            tokenListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!HasRoom()) return;

                    tokens.clear();
                    String deviceID = Global.getInstance().getDeviceID();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        UserToken userToken = data.getValue(UserToken.class);
                        Log.e("CacheToken", Integer.parseInt(userToken.getRoomNumber()) + " | " + Integer.parseInt(userToken.getUserID()) + " | " + userToken.getToken());

                        if (Integer.parseInt(userToken.getRoomNumber()) == roomNumber && !userToken.getDeviceID().equals(deviceID)) {
                            String token = userToken.getToken();
                            tokens.add(userToken.getToken());
                        }
                    }
                    Log.e("CacheToken", tokens.size() + "");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
        }
        FirebaseUtility.getDatabaseReference().addValueEventListener(tokenListener);
    }

    public void removeTokenListener() {
        if (tokenListener != null) {
            FirebaseUtility.getDatabaseReference().removeEventListener(tokenListener);
        }
    }

    public List<String> getTokens() {
        return tokens;
    }

    // Init after login
    private ITask iSuccessedTask;
    private ITask iFailedTask;

    public void removeToken() {
        removeTokenListener();
        String deviceID = Global.getInstance().getDeviceID();
        FirebaseUtility.getDatabaseReference().child(deviceID).removeValue();
        Global.getInstance().removeSharedPreferencesKey("FCM", "deviceID");
    }

    public void initData(String result, ITask iSuccessedTask, ITask iFailedTask) {
        this.iSuccessedTask = iSuccessedTask;
        this.iFailedTask = iFailedTask;
        Global.getInstance().makeToast("Thiết lập dữ liệu");
        Log.e("Login", "Start initData");
        updateStudentVariables(result);
    }

    private void updateStudentVariables(String result) {
        Log.e("Login", "initData" + result);
        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("sinhVien");
            int userID = jsonObject.getInt("sinhVienID");
            String fullname = jsonObject.getString("hoTenSV");
            String code = jsonObject.getString("maSV");
            String username = jsonObject.getString("tenDangNhap");
            String password = jsonObject.getString("matKhau");
            String classID = jsonObject.getString("lopID");

            setUserID(userID);
            setFullname(fullname);
            setSinhVien(new SinhVien(String.valueOf(userID), fullname, code, username, password, classID));

            updateStudentRoom(new ITask() {
                @Override
                public void onComplete() {
                    updateUserToken(
                            new ITask() {
                                @Override
                                public void onComplete() {
                                    iSuccessedTask.onComplete();
                                }
                            },
                            new ITask() {
                                @Override
                                public void onComplete() {
                                    iFailedTask.onComplete();
                                }
                            }
                    );
                }
            });
        } catch (Exception ex) {
            iFailedTask.onComplete();
            ex.printStackTrace();
        }
    }

    public void updateStudentRoom(ITask completedTask) {
        Log.e("Login", "updateStudentRoom");
        IRoomService roomService = RetrofitUtility.getService(IRoomService.class);
        Log.e("RetrofitQuery", "" + userID);
        roomService.getStudentRoom(userID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("Retrofit", response.body().toString());
                try {
                    if (response.body().toString().contains("message")) {
                        Log.e("Retrofit", "No ROom");
                        setNoRoom();
                    } else {
                        JSONArray jsonArray = new JSONArray(response.body().toString());
                        setRoom(
                            jsonArray.getJSONObject(0).getString("phongID"),
                            jsonArray.getJSONObject(0).getString("soPhong")
                        );
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (completedTask != null) {
                    completedTask.onComplete();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    public void updateStudentRoom(String roomID, String roomNumber) {
        String deviceID = Global.getInstance().getDeviceID();
        setRoom(roomID, roomNumber);
        FirebaseUtility.getDatabaseReference()
                .child(deviceID)
                .setValue(new UserToken(String.valueOf(userID), String.valueOf(roomNumber), currentToken, deviceID));
    }

    public void updateUserToken(ITask completedTask, ITask failedTask) {
        Log.e("Login", "updateUserToken");
        addTokenListener();
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w("Firebase", "Fetching FCM registration token failed", task.getException());
                        failedTask.onComplete();
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    currentToken = token;

                    updateRealtimeDatabase();

                    if (completedTask != null) {
                        completedTask.onComplete();
                    }
                }
            });
    }

    public void updateRealtimeDatabase() {
        String deviceID = Global.getInstance().getDeviceID();
        FirebaseUtility.getDatabaseReference()
                .child(deviceID)
                .setValue(new UserToken(String.valueOf(userID), String.valueOf(roomNumber), currentToken, deviceID));

    }
}
