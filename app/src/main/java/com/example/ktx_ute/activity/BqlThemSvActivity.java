package com.example.ktx_ute.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.example.ktx_ute.model.IFCMService;
import com.example.ktx_ute.model.Phong;
import com.example.ktx_ute.model.PushNotification;
import com.example.ktx_ute.model.SinhVien;
import com.example.ktx_ute.model.UserToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BqlThemSvActivity extends AppCompatActivity {
    private LinearLayout frameXacNhan;
    TextView txtLoaiPhong, txtPhong, txtSoLuong, txtSoLuongToiDa;
    Phong phong;
    SinhVien sinhVien;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_bql_them_vao_phong);

        progressBar = findViewById(R.id.progress);
        ImageView buttonBack = findViewById(R.id.buttonBack);
        txtLoaiPhong = findViewById(R.id.txt_loai_phong);
        txtSoLuongToiDa = findViewById(R.id.txt_so_luong_toi_da);
        txtPhong = findViewById(R.id.txt_phong);
        txtSoLuong = findViewById(R.id.txt_so_luong);
        phong = (Phong) getIntent().getSerializableExtra("phong");
        sinhVien = (SinhVien) getIntent().getSerializableExtra("sinhvien");

        txtSoLuong.setText("Số lượng hiện tại: " + phong.getSoLuongHienTai().toString());
        txtSoLuongToiDa.setText("Số lượng tối đa: " + phong.getSoLuongToiDa().toString());
        txtPhong.setText("Phòng: " + phong.getSoPhong().toString());
        txtLoaiPhong.setText("Loại phòng: " + phong.getLoaiPhong().toString());

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
                btnXacNhan.setVisibility(View.GONE);
            }
        });

        // Bắt sự kiện click cho nút btn_dong_y
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameXacNhan.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callApi();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(BqlThemSvActivity.this, QuanLiSinhVienActivity.class);
                        intent.putExtra("phong", phong);
                        startActivity(intent);
                    }
                }, 4000);
                // Trở về layout activity_sinh_vien_bql_sv1.xml khi người dùng nhấn nút btn_dong_y
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

    private List<String> updateUserTokens = new ArrayList<>();
    private List<String> roomUserTokens = new ArrayList<>();
    private void callApi() {
        ApiUtil.apiutil.dangkyphong(sinhVien.getSinhVienID(), phong.getPhongID()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(BqlThemSvActivity.this, "Thay đổi thành công!!", Toast.LENGTH_SHORT).show();

                FirebaseUtility.getDatabaseReference().get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        DataSnapshot dataSnapshot = task.getResult();

                        updateUserTokens.clear();
                        roomUserTokens.clear();
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            UserToken userToken = data.getValue(UserToken.class);
                            Log.e("CacheToken", Integer.parseInt(userToken.getRoomNumber()) + " | " + Integer.parseInt(userToken.getUserID()) + " | " + userToken.getToken());
                            if (userToken.getUserID().equals(sinhVien.getSinhVienID())) {
                                updateUserTokens.add(userToken.getToken());
                                continue;
                            }
                            if (userToken.getRoomNumber().equals(phong.getSoPhong())) {
                                roomUserTokens.add(userToken.getToken());
                            }
                        }

                        sendNotification();
                    }
                });

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(BqlThemSvActivity.this, "Lỗi!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification() {
        if (updateUserTokens.size() != 0) {
            PushNotification pushNotification = new PushNotification(
                    new DataMessage(FirebaseUtility.DataMessageType.STUDENT_JOIN)
            );
            FirebaseUtility.sendNotification(this, updateUserTokens, pushNotification);
        }

        if (roomUserTokens.size() != 0) {
            PushNotification pushNotification = new PushNotification(
                    new DataMessage(FirebaseUtility.DataMessageType.CHAT_MEMBER_JOIN)
            );
            FirebaseUtility.sendNotification(this, roomUserTokens, pushNotification);
        }
    }
}