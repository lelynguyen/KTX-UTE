package com.example.ktx_ute.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
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

public class QuanLiSinhVienActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_sinh_vien);

        ImageView buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển về giao diện trước, ví dụ:
                onBackPressed(); // Sử dụng phương thức onBackPressed() để quay lại màn hình trước đó.
            }
        });

        AppCompatButton btnThemKTX1 = findViewById(R.id.btn_them_ktx1);

        btnThemKTX1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển đến hoạt động mới (activity_bql_them_sv_tim_phong.xml)
                Intent intent = new Intent(QuanLiSinhVienActivity.this, XoaSinhVienActivity.class); // Thay thế NewActivity bằng tên thích hợp của hoạt động bạn muốn chuyển đến.

                // Bắt đầu hoạt động mới
                startActivity(intent);
            }
        });

        AppCompatButton btnThemKTX2 = findViewById(R.id.btn_them_ktx2);

        btnThemKTX2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển đến hoạt động mới (activity_bql_them_sv_tim_phong.xml)
                Intent intent = new Intent(QuanLiSinhVienActivity.this, XoaSinhVienActivity.class); // Thay thế NewActivity bằng tên thích hợp của hoạt động bạn muốn chuyển đến.

                // Bắt đầu hoạt động mới
                startActivity(intent);
            }
        });

        AppCompatButton btnThemKTX3 = findViewById(R.id.btn_them_ktx3);

        btnThemKTX3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển đến hoạt động mới (activity_bql_them_sv_tim_phong.xml)
                Intent intent = new Intent(QuanLiSinhVienActivity.this, XoaSinhVienActivity.class); // Thay thế NewActivity bằng tên thích hợp của hoạt động bạn muốn chuyển đến.

                // Bắt đầu hoạt động mới
                startActivity(intent);
            }
        });
    }
}