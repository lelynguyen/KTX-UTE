package com.example.ktx_ute.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktx_ute.R;
import com.example.ktx_ute.adapter.SinhVienAdapter;
import com.example.ktx_ute.apiutils.ApiUtil;
import com.example.ktx_ute.interfacee.IOnClickSinhVien;
import com.example.ktx_ute.model.Phong;
import com.example.ktx_ute.model.SinhVien;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLiSinhVienActivity extends AppCompatActivity implements IOnClickSinhVien {
    List<SinhVien> sinhVienList;
    SinhVienAdapter sinhVienAdapter;
    RecyclerView recyclerView;
    Phong phong;

    TextView txthoso, txtphong;
    LinearLayout imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_li_sinh_vien);

        sinhVienList = new ArrayList<>();
        imageButton = findViewById(R.id.btnSearch);
        ImageView buttonBack = findViewById(R.id.buttonBack);
        txthoso = findViewById(R.id.ho_so);
        txtphong = findViewById(R.id.phong_trong);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        phong = (Phong) getIntent().getSerializableExtra("phong");
        txthoso.setText("HỒ SƠ PHÒNG " + phong.getSoPhong().toString().trim());
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển về giao diện trước, ví dụ:
                onBackPressed(); // Sử dụng phương thức onBackPressed() để quay lại màn hình trước đó.
            }
        });
        callApi();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển sang giao diện activity_sinh_vien_bql_sv2.xml, ví dụ:
                Intent intent = new Intent(QuanLiSinhVienActivity.this, MenuOptionsBqlActivity.class); // Thay thế CurrentActivity và SecondActivity bằng tên thích hợp của hoạt động của bạn.
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void callApi() {
        ApiUtil.apiutil.getSinhVienTrongPhong(phong.getPhongID()).enqueue(new Callback<List<SinhVien>>() {
            @Override
            public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {
                sinhVienList = response.body();
                if (sinhVienList.isEmpty()) {
                    txthoso.setVisibility(View.GONE);
                    txtphong.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    sinhVienAdapter = new SinhVienAdapter(sinhVienList, QuanLiSinhVienActivity.this::onClickSinhVien);
                    recyclerView.setAdapter(sinhVienAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SinhVien>> call, Throwable t) {
                Toast.makeText(QuanLiSinhVienActivity.this, "Loi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickSinhVien(SinhVien sinhVien) {
        Intent intent = new Intent(QuanLiSinhVienActivity.this, XoaSinhVienActivity.class);
        intent.putExtra("sinhvien", sinhVien);
        intent.putExtra("phong", phong);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sinhVienAdapter != null) {
            sinhVienAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sinhVienAdapter != null) {
            sinhVienAdapter.notifyDataSetChanged();
        }
    }
}