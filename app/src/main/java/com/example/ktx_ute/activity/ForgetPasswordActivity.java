package com.example.ktx_ute.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ktx_ute.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        FrameLayout buttonBack = findViewById(R.id.buttonBack);
        TextView loginTextView = findViewById(R.id.login);
        Button guiButton = findViewById(R.id.btn_gui);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện hành động quay về layout trước đó ở đây
                finish(); // Đóng hoạt động hiện tại
            }
        });

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện hành động chuyển sang layout activity_login.xml ở đây
                Intent loginIntent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        guiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện hành động chuyển sang layout activity_confirm.xml ở đây
                Intent confirmIntent = new Intent(ForgetPasswordActivity.this, ConfirmActivity.class);
                startActivity(confirmIntent);
            }
        });
    }
}
