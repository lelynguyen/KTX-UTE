package com.example.ktx_ute;

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
    private String currentToken;

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

    public void cacheStudentTokens() {
        if (tokens.size() == 0) {
            generateTokenList();
        }
    }

    public void generateTokenList() {
        tokens.clear();
//        Log.e("CacheToken", "mine " + roomNumber + " | " + userID);
        FirebaseUtility.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    UserToken userToken = data.getValue(UserToken.class);
                    if (Integer.parseInt(userToken.getRoomNumber()) == roomNumber && Integer.parseInt(userToken.getUserID()) != userID) {
                        String token = userToken.getToken();
//                        Log.e("CacheToken", Integer.parseInt(userToken.getRoomNumber()) + " | " + Integer.parseInt(userToken.getUserID()) + " | " + userToken.getToken());
                        tokens.add(userToken.getToken());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.e("CacheToken", tokens.size() + "");
    }

    public List<String> getTokens() {
        return tokens;
    }


    // Init after login
    private LoginActivity loginActivity;

    public void removeToken() {
        FirebaseUtility.getDatabaseReference()
                .child(String.valueOf(userID))
                .setValue(new UserToken(String.valueOf(userID), String.valueOf(roomID), ""));
    }

    public void initData(String result, LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
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
                                    loginActivity.startIntentSV();
                                }
                            }
                    );
                }
            });
        } catch (Exception ex) {
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
        setRoom(roomID, roomNumber);
        FirebaseUtility.getDatabaseReference()
                .child(String.valueOf(userID))
                .setValue(new UserToken(String.valueOf(userID), String.valueOf(roomNumber), currentToken));
    }

    public void updateUserToken(ITask completedTask) {
        Log.e("Login", "updateUserToken");
        generateTokenList();
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w("Firebase", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    currentToken = token;

                    FirebaseUtility.getDatabaseReference()
                            .child(String.valueOf(userID))
                            .setValue(new UserToken(String.valueOf(userID), String.valueOf(roomNumber), token));

                    if (completedTask != null) {
                        completedTask.onComplete();
                    }
                }
            });
    }
}
