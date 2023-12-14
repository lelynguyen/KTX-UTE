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

import com.example.ktx_ute.FirebaseUtility;
import com.example.ktx_ute.R;
import com.example.ktx_ute.apiutils.ApiUtil;
import com.example.ktx_ute.model.DataMessage;
import com.example.ktx_ute.model.Phong;
import com.example.ktx_ute.model.PushNotification;
import com.example.ktx_ute.model.SinhVien;
import com.example.ktx_ute.model.UserToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XoaSinhVienActivity extends AppCompatActivity {
    private LinearLayout frameXacNhan;
    private LinearLayout linearSelect;
    TextView txtHoTen, txtMasv;
    SinhVien sinhVien;
    Phong phong;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_bql_xoa_khoi_ktx);


        txtHoTen = findViewById(R.id.txt_hvt);
        progressBar = findViewById(R.id.progress);
        txtMasv = findViewById(R.id.txt_mssv);
        ImageView buttonBack = findViewById(R.id.buttonBack);
        sinhVien = (SinhVien) getIntent().getSerializableExtra("sinhvien");
        phong = (Phong) getIntent().getSerializableExtra("phong");
        txtHoTen.setText("Họ và tên: " + sinhVien.getHoTenSV());
        txtMasv.setText("MSSV: " + sinhVien.getMaSV());
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
                linearSelect.setVisibility(View.GONE);
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
                intent.putExtra("phong", phong);
                intent.putExtra("sinhvien", sinhVien);
                startActivity(intent);
            }
        });

        // Bắt sự kiện click cho nút txt_button_dong_y
        txtButtonDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến layout QuanLiSinhVienActivity
                progressBar.setVisibility(View.VISIBLE);
                frameXacNhan.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callApi();
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(XoaSinhVienActivity.this, QuanLiSinhVienActivity.class);
                        intent.putExtra("phong", phong);
                        startActivity(intent);
                    }
                }, 3000);

            }
        });
    }

    private List<String> updateUserTokens = new ArrayList<>();
    private List<String> roomUserTokens = new ArrayList<>();
    private void callApi() {
        ApiUtil.apiutil.deleteSV(sinhVien.getSinhVienID(), phong.getPhongID()).enqueue(new Callback<SinhVien>() {
            @Override
            public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                Toast.makeText(XoaSinhVienActivity.this, "Xóa thành công!!", Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<SinhVien> call, Throwable t) {
                Toast.makeText(XoaSinhVienActivity.this, "Lôi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification() {
        if (updateUserTokens.size() != 0) {
            PushNotification pushNotification = new PushNotification(
                    new DataMessage(FirebaseUtility.DataMessageType.STUDENT_LEAVE)
            );
            FirebaseUtility.sendNotification(this, updateUserTokens, pushNotification);
        }

        if (roomUserTokens.size() != 0) {
            PushNotification pushNotification = new PushNotification(
                    new DataMessage(FirebaseUtility.DataMessageType.CHAT_MEMBER_LEAVE)
            );
            FirebaseUtility.sendNotification(this, roomUserTokens, pushNotification);
        }
    }
}