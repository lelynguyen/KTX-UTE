package com.example.ktx_ute.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.ktx_ute.AdminData;
import com.example.ktx_ute.Global;
import com.example.ktx_ute.R;
import com.example.ktx_ute.StudentData;
import com.example.ktx_ute.interfacee.ITask;

public class MainActivity extends AppCompatActivity {
    private static final int USER_STUDENT = 0;
    private static final int USER_ADMIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLogged();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void checkLogged() {
        if (Global.IsLogged()) {
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        boolean isLogged = sharedPreferences.getBoolean("isLogged", false);
        if (!isLogged) {
            toLoginActivity();
            return;
        }

        String result = sharedPreferences.getString("result", "");
        int userType = sharedPreferences.getInt("type", -1);
        switch (userType) {
            case USER_ADMIN:
                Global.getService(AdminData.class).initData(
                        result,
                        new ITask() {
                            @Override
                            public void onComplete() {
                                startIntentAdmin();
                            }
                        },
                        new ITask() {
                            @Override
                            public void onComplete() {
                                loginFailed();
                            }
                        });
                break;
            case USER_STUDENT:
                Global.getService(StudentData.class).initData(
                        result,
                        new ITask() {
                            @Override
                            public void onComplete() {
                                startIntentSV();
                            }
                        }, new ITask() {
                            @Override
                            public void onComplete() {
                                loginFailed();
                            }
                        });
                break;
        }
    }

    private void toLoginActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, 2000);
    }

    private void startIntentSV() {
        Global.getInstance().makeToast("Thành công");
        Global.setLoginStatus(true);
        Intent intent = new Intent(MainActivity.this, MenuOptionsSvActivity.class);
        startActivity(intent);
        finish();
    }

    private void startIntentAdmin() {
        Global.getInstance().makeToast("Thành công");
        Global.setLoginStatus(true);
        Intent intent = new Intent(MainActivity.this, MenuOptionsBqlActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginFailed() {
        Global.getInstance().makeToast("Lỗi");
        Global.getInstance().saveSharedPreferencesValue("Login", "isLogged", false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}