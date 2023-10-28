package com.example.ktx_ute.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class NewPasswordActivity extends AppCompatActivity {

    private ImageView  eyeImageView1, eyeImageView2 ;
    private EditText passwordEditText1, passwordEditText2;
    private boolean isPasswordVisible1 = false, isPasswordVisible2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        eyeImageView1 = findViewById(R.id.imageView8);
        passwordEditText1 = findViewById(R.id.newPassword);
        eyeImageView2 = findViewById(R.id.imageView9);
        passwordEditText2 = findViewById(R.id.confirmPassword);
        FrameLayout buttonBack = findViewById(R.id.buttonBack);
        Button guiButton = findViewById(R.id.btn_gui);

        eyeImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible1) {
                    // Nếu mật khẩu đã hiển thị, ẩn mật khẩu và chuyển hình ảnh về img_hidden
                    passwordEditText1.setInputType(129); // 129 tương đương với inputType "textPassword"
                    eyeImageView1.setImageResource(R.drawable.img_hidden);
                    isPasswordVisible1 = false;
                } else {
                    // Nếu mật khẩu chưa hiển thị, hiển thị mật khẩu và chuyển hình ảnh về img_see
                    passwordEditText1.setInputType(144); // 144 tương đương với inputType "text"
                    eyeImageView1.setImageResource(R.drawable.img_see);
                    isPasswordVisible1 = true;
                }
            }
        });

        eyeImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible2) {
                    // Nếu mật khẩu đã hiển thị, ẩn mật khẩu và chuyển hình ảnh về img_hidden
                    passwordEditText2.setInputType(129); // 129 tương đương với inputType "textPassword"
                    eyeImageView2.setImageResource(R.drawable.img_hidden);
                    isPasswordVisible2 = false;
                } else {
                    // Nếu mật khẩu chưa hiển thị, hiển thị mật khẩu và chuyển hình ảnh về img_see
                    passwordEditText2.setInputType(144); // 144 tương đương với inputType "text"
                    eyeImageView2.setImageResource(R.drawable.img_see);
                    isPasswordVisible2 = true;
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện hành động quay về layout trước đó ở đây
                finish(); // Đóng hoạt động hiện tại
            }
        });

        guiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện hành động chuyển sang layout activity_confirm.xml ở đây
                Intent confirmIntent = new Intent(NewPasswordActivity.this, LoginActivity.class);
                startActivity(confirmIntent);
            }
        });

    }
}