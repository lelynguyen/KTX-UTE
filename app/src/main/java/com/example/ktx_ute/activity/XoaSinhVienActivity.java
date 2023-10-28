package com.example.ktx_ute.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

public class XoaSinhVienActivity extends AppCompatActivity {
    private LinearLayout frameXacNhan;
    private LinearLayout linearSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_bql_xoa_khoi_ktx);

        ImageView buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển về giao diện trước, ví dụ:
                onBackPressed(); // Sử dụng phương thức onBackPressed() để quay lại màn hình trước đó.
            }
        });

        frameXacNhan = findViewById(R.id.frame_xac_nhan);
        linearSelect = findViewById(R.id.linearSelect);

        TextView txtButtonDongY = findViewById(R.id.txt_button_dong_y);
        TextView txtButtonHuy = findViewById(R.id.txt_button_huy);
        TextView txtDoiPhong = findViewById(R.id.txt_doi_phong);
        TextView btnXoa = findViewById(R.id.btn_xoa);

        // Ẩn LinearLayout frame_xac_nhan mặc định
        frameXacNhan.setVisibility(View.GONE);

        // Bắt sự kiện click cho nút btn_xoa
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ẩn LinearLayout linearSelect
                linearSelect.setVisibility(View.GONE);
                // Hiển thị LinearLayout frame_xac_nhan
                frameXacNhan.setVisibility(View.VISIBLE);
            }
        });

        // Bắt sự kiện click cho nút txt_button_huy
        txtButtonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ẩn LinearLayout frame_xac_nhan
                frameXacNhan.setVisibility(View.GONE);
                // Hiển thị LinearLayout linearSelect
                linearSelect.setVisibility(View.VISIBLE);
            }
        });

        // Bắt sự kiện click cho nút txt_doi_phong
        txtDoiPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến layout BqlThemSvTimPhongActivity
                Intent intent = new Intent(XoaSinhVienActivity.this, BqlThemSvTimPhongActivity.class);
                startActivity(intent);
            }
        });

        // Bắt sự kiện click cho nút txt_button_dong_y
        txtButtonDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến layout QuanLiSinhVienActivity
                Intent intent = new Intent(XoaSinhVienActivity.this, QuanLiSinhVienActivity.class);
                startActivity(intent);
            }
        });
    }
}