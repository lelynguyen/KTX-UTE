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
import com.example.ktx_ute.model.SinhVien;

public class BqlSinhVienShowActivity extends AppCompatActivity {
    TextView txtHoTen, txtMasv;
    SinhVien sinhVien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_bql_sv2);

        ImageView buttonBack = findViewById(R.id.buttonBack);
        sinhVien = (SinhVien) getIntent().getSerializableExtra("sinhvien");
        txtHoTen = findViewById(R.id.txt_hvt);
        txtMasv = findViewById(R.id.txt_mssv);
        txtHoTen.setText("Họ và tên: " + sinhVien.getHoTenSV());
        txtMasv.setText("MSSV: " + sinhVien.getMaSV());

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển về giao diện trước, ví dụ:
                onBackPressed(); // Sử dụng phương thức onBackPressed() để quay lại màn hình trước đó.
            }
        });

        ImageButton btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển sang giao diện activity_sinh_vien_bql_sv2.xml, ví dụ:
                Intent intent = new Intent(BqlSinhVienShowActivity.this, MenuOptionsBqlActivity.class); // Thay thế CurrentActivity và SecondActivity bằng tên thích hợp của hoạt động của bạn.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        AppCompatButton btnThemKTX = findViewById(R.id.btn_them_ktx);

        btnThemKTX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển đến hoạt động mới (activity_bql_them_sv_tim_phong.xml)
                Intent intent = new Intent(BqlSinhVienShowActivity.this, BqlThemSvTimPhongActivity.class); // Thay thế NewActivity bằng tên thích hợp của hoạt động bạn muốn chuyển đến.
                intent.putExtra("sinhvien", sinhVien);
                // Bắt đầu hoạt động mới
                startActivity(intent);
            }
        });
    }
}