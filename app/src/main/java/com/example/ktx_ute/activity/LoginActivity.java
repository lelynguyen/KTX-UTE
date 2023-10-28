package com.example.ktx_ute.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firstapp.ql_ktx.model.Message;
import com.firstapp.ql_ktx.adapter.MessageAdapter;
import com.example.ktx_ute.R;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private ImageView eyeImageView;
    private EditText passwordEditText;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eyeImageView = findViewById(R.id.imageView8);
        passwordEditText = findViewById(R.id.loginPassword);

        // Ánh xạ các button và TextView
        View btnLoginSv = findViewById(R.id.btn_login_sv);
        View btnLoginAdmin = findViewById(R.id.btn_login_admin);
        View quenpass = findViewById(R.id.quenpass);

        // Thiết lập sự kiện click cho btnLoginSv
        btnLoginSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến layout activity_menu_options_sv.xml
                Intent intent = new Intent(LoginActivity.this, MenuOptionsSvActivity.class);
                startActivity(intent);
            }
        });

        // Thiết lập sự kiện click cho btnLoginAdmin
        btnLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến layout activity_menu_options_bql.xml
                Intent intent = new Intent(LoginActivity.this, MenuOptionsBqlActivity.class);
                startActivity(intent);
            }
        });

        // Thiết lập sự kiện click cho quenpass (TextView)
        quenpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến layout activity_forget_password.xml
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        eyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    // Nếu mật khẩu đã hiển thị, ẩn mật khẩu và chuyển hình ảnh về img_hidden
                    passwordEditText.setInputType(129); // 129 tương đương với inputType "textPassword"
                    eyeImageView.setImageResource(R.drawable.img_hidden);
                    isPasswordVisible = false;
                } else {
                    // Nếu mật khẩu chưa hiển thị, hiển thị mật khẩu và chuyển hình ảnh về img_see
                    passwordEditText.setInputType(144); // 144 tương đương với inputType "text"
                    eyeImageView.setImageResource(R.drawable.img_see);
                    isPasswordVisible = true;
                }
            }
        });
    }
}