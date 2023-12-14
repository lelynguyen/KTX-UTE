package com.example.ktx_ute.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.ktx_ute.R;
import com.example.ktx_ute.model.Phong;

public class BqlPhongShowActivity extends AppCompatActivity {
    TextView txtLoaiPhong, txtPhong, txtSoLuong, txtSoLuongToiDa;
    Intent intent;
    Phong phong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_bql_phong_o2);

        txtLoaiPhong = findViewById(R.id.txt_loai_phong);
        txtSoLuongToiDa = findViewById(R.id.txt_so_luong_toi_da);
        txtPhong = findViewById(R.id.txt_phong);
        txtSoLuong = findViewById(R.id.txt_so_luong);
        ImageView buttonBack = findViewById(R.id.buttonBack);
        intent = getIntent();
        phong = (Phong) intent.getSerializableExtra("phong");

        txtSoLuong.setText("Số lượng hiện tại: " + phong.getSoLuongHienTai().toString());
        txtSoLuongToiDa.setText("Số lượng tối đa: " + phong.getSoLuongToiDa().toString());
        txtPhong.setText("Phòng: " + phong.getSoPhong().toString());
        if (phong.getLoaiPhong().equals("0")) {
            txtLoaiPhong.setText("Loại phòng: Nam");
        } else {
            txtLoaiPhong.setText("Loại phòng: Nữ");
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageButton btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển sang giao diện activity_sinh_vien_bql_sv2.xml, ví dụ:
                Intent intent = new Intent(BqlPhongShowActivity.this, MenuOptionsBqlActivity.class); // Thay thế CurrentActivity và SecondActivity bằng tên thích hợp của hoạt động của bạn.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        AppCompatButton btnXacNhan = findViewById(R.id.btn_xac_nhan);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị LinearLayout frame_xac_nhan khi người dùng nhấn nút btn_xac_nhan
                intent = new Intent(BqlPhongShowActivity.this, QuanLiSinhVienActivity.class); // Thay thế CurrentActivity và SecondActivity bằng tên thích hợp của hoạt động của bạn.
                intent.putExtra("phong", phong);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}