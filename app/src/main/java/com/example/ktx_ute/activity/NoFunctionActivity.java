package com.example.ktx_ute.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ktx_ute.R;

public class NoFunctionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_function);

        FrameLayout buttonBack = findViewById(R.id.buttonBack);
        Button guiButton = findViewById(R.id.btn_gui);

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
                finish();
            }
        });
    }
}