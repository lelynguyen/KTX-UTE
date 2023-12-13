package com.example.ktx_ute.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.ktx_ute.FirebaseUtility;
import com.example.ktx_ute.Global;
import com.example.ktx_ute.R;
import com.example.ktx_ute.StudentData;
import com.example.ktx_ute.apiutils.ApiUtil;
import com.example.ktx_ute.model.DataMessage;
import com.example.ktx_ute.model.Phong;
import com.example.ktx_ute.model.PushNotification;
import com.example.ktx_ute.model.SinhVien;
import com.example.ktx_ute.model.UserToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SvDangKiPhongShowActivity extends AppCompatActivity {
    TextView txtLoaiPhong, txtPhong, txtSoLuong, txtSoLuongToiDa;
    Intent intent;
    Phong phong;
    SinhVien sinhVien;
    private LinearLayout frameXacNhan;
    private AppCompatButton btnXacNhan;
    private TextView btnDongY;
    private TextView btnHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sv_dang_ki_phong_show);


        txtLoaiPhong = findViewById(R.id.txt_loai_phong);
        txtSoLuongToiDa = findViewById(R.id.txt_so_luong_toi_da);
        txtPhong = findViewById(R.id.txt_phong);
        txtSoLuong = findViewById(R.id.txt_so_luong);
        intent = getIntent();
        phong = (Phong) intent.getSerializableExtra("phong");
        sinhVien = (SinhVien) intent.getSerializableExtra("sinhvien");

        txtSoLuong.setText("Số lượng hiện tại: " + phong.getSoLuongHienTai().toString());
        txtSoLuongToiDa.setText("Số lượng tối đa: " + phong.getSoLuongToiDa().toString());
        txtPhong.setText("Phòng: " + phong.getSoPhong().toString());
        txtLoaiPhong.setText("Loại phòng: " + phong.getLoaiPhong().toString());
        ImageView buttonBack = findViewById(R.id.buttonBack);

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
                Intent intent = new Intent(SvDangKiPhongShowActivity.this, MenuOptionsSvActivity.class); // Thay thế CurrentActivity và SecondActivity bằng tên thích hợp của hoạt động của bạn.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
                callApi();
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

    private void callApi() {
        ApiUtil.apiutil.dangkyphong(sinhVien.getSinhVienID(), phong.getPhongID()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                callUpdateToken();
                Toast.makeText(SvDangKiPhongShowActivity.this, "Đăng ký thành công!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SvDangKiPhongShowActivity.this, "Lỗi!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void callUpdateToken() {
        Global.getService(StudentData.class).updateStudentRoom(
                phong.getPhongID(),
                phong.getSoPhong()
        );

        if (Global.getService(StudentData.class).HasRoom()) {
            PushNotification leaveNotification = new PushNotification(
                    new DataMessage(FirebaseUtility.DataMessageType.STUDENT_LEAVE)
            );
            FirebaseUtility.callUpdateMessage(
                    SvDangKiPhongShowActivity.this,
                    String.valueOf(Global.getService(StudentData.class).getRoomNumber()),
                    leaveNotification);
        }

        PushNotification joinNotification = new PushNotification(
                new DataMessage(FirebaseUtility.DataMessageType.STUDENT_JOIN)
        );
        FirebaseUtility.callUpdateMessage(SvDangKiPhongShowActivity.this, phong.getSoPhong(), joinNotification);
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