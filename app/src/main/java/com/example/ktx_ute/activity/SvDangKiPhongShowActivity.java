package com.example.ktx_ute.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.ImageView; // Thêm import này
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.ktx_ute.R;

public class SvDangKiPhongShowActivity extends AppCompatActivity {
    private LinearLayout frameXacNhan;
    private AppCompatButton btnXacNhan;
    private TextView btnDongY;
    private TextView btnHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sv_dang_ki_phong_show);

        ImageView buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển về giao diện trước, ví dụ:
                onBackPressed(); // Sử dụng phương thức onBackPressed() để quay lại màn hình trước đó.
            }
        });

        // Gán tham chiếu cho các phần tử giao diện
        frameXacNhan = findViewById(R.id.frame_xac_nhan);
        btnXacNhan = findViewById(R.id.btn_xac_nhan);
        btnDongY = findViewById(R.id.btn_dong_y);
        btnHuy = findViewById(R.id.btn_huy);

        // Ẩn LinearLayout frameXacNhan ban đầu
        frameXacNhan.setVisibility(View.GONE);

        // Gán sự kiện click cho nút "Đăng Kí"
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị LinearLayout frameXacNhan và ẩn nút Đăng Kí
                frameXacNhan.setVisibility(View.VISIBLE);
                btnXacNhan.setVisibility(View.GONE);
            }
        });

        // Gán sự kiện click cho nút "Đồng Ý"
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện các xử lý cần thiết khi nhấn Đồng Ý
                // Ví dụ: Quay lại giao diện MenuOptionsSvActivity
                Intent intent = new Intent(SvDangKiPhongShowActivity.this, MenuOptionsSvActivity.class);
                startActivity(intent);
            }
        });

        // Gán sự kiện click cho nút "Hủy"
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ẩn LinearLayout frameXacNhan và hiện nút Đăng Kí
                frameXacNhan.setVisibility(View.GONE);
                btnXacNhan.setVisibility(View.VISIBLE);
            }
        });
    }
}