package com.example.ktx_ute.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
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

public class BqlThemSvActivity extends AppCompatActivity {
    private LinearLayout frameXacNhan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_bql_them_vao_phong);

        ImageView buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển về giao diện trước, ví dụ:
                onBackPressed(); // Sử dụng phương thức onBackPressed() để quay lại màn hình trước đó.
            }
        });

        frameXacNhan = findViewById(R.id.frame_xac_nhan);

        AppCompatButton btnXacNhan = findViewById(R.id.btn_xac_nhan);
        TextView btnDongY = findViewById(R.id.btn_dong_y);
        TextView btnHuy = findViewById(R.id.btn_huy);

        // Ẩn LinearLayout frame_xac_nhan mặc định
        frameXacNhan.setVisibility(View.GONE);

        // Bắt sự kiện click cho nút btn_xac_nhan
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị LinearLayout frame_xac_nhan khi người dùng nhấn nút btn_xac_nhan
                frameXacNhan.setVisibility(View.VISIBLE);
            }
        });

        // Bắt sự kiện click cho nút btn_dong_y
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Trở về layout activity_sinh_vien_bql_sv1.xml khi người dùng nhấn nút btn_dong_y
                Intent intent = new Intent(BqlThemSvActivity.this, BqlSinhVienActivity.class);
                startActivity(intent);
            }
        });

        // Bắt sự kiện click cho nút btn_huy
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ẩn LinearLayout frame_xac_nhan khi người dùng nhấn nút btn_huy
                frameXacNhan.setVisibility(View.GONE);
            }
        });
    }
}