package com.example.ktx_ute;

import android.util.Log;

import com.example.ktx_ute.activity.LoginActivity;
import com.example.ktx_ute.interfacee.ITask;
import com.example.ktx_ute.model.SinhVien;
import com.example.ktx_ute.model.UserToken;

import org.json.JSONObject;

public class AdminData {
    private int userID;
    private String fullname = "";

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



    private LoginActivity loginActivity;

    public void initData(String result, LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        Global.getInstance().makeToast("Thiết lập dữ liệu");

        updateAdminVariables(result);
    }

    private void updateAdminVariables(String result) {
        Log.e("Login", "initData" + result);
        try {
            JSONObject jsonObject = new JSONObject(result).getJSONObject("admin");
            int userID = jsonObject.getInt("adminID");
            String fullname = jsonObject.getString("hoTenAdmin");

            setUserID(userID);
            setFullname(fullname);

            loginActivity.startIntentAdmin();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
