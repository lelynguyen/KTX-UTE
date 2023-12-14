package com.example.ktx_ute.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktx_ute.R;
import com.example.ktx_ute.adapter.SinhVienAdapter;
import com.example.ktx_ute.apiutils.ApiUtil;
import com.example.ktx_ute.interfacee.IOnClickSinhVien;
import com.example.ktx_ute.model.SinhVien;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BqlSinhVienActivity extends AppCompatActivity implements IOnClickSinhVien {
    List<SinhVien> sinhVienList;
    SinhVienAdapter sinhVienAdapter;
    RecyclerView recyclerView;
    SearchView search_view_bg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sinh_vien_bql_sv1);

        search_view_bg = findViewById(R.id.search_view);
        sinhVienList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        ImageView buttonBack = findViewById(R.id.buttonBack);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        search_view_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_view_bg.setIconified(false);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển về giao diện trước, ví dụ:
                onBackPressed();
            }
        });

//        ImageButton btnSearch = findViewById(R.id.btnSearch);
//
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Thực hiện hành động để chuyển sang giao diện activity_sinh_vien_bql_sv2.xml, ví dụ:
//                Intent intent = new Intent(BqlSinhVienActivity.this, BqlSinhVienShowActivity.class); // Thay thế CurrentActivity và SecondActivity bằng tên thích hợp của hoạt động của bạn.
//                startActivity(intent);
//            }
//        });
        callApi();

        search_view_bg.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sinhVienAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sinhVienAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void callApi() {
        ApiUtil.apiutil.getSinhVien().enqueue(new Callback<List<SinhVien>>() {
            @Override
            public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {
                sinhVienList = response.body();
                if (sinhVienList!=null) {
                    sinhVienAdapter = new SinhVienAdapter(sinhVienList, BqlSinhVienActivity.this::onClickSinhVien);
                    recyclerView.setAdapter(sinhVienAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<SinhVien>> call, Throwable t) {
                Toast.makeText(BqlSinhVienActivity.this, "Lỗi!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClickSinhVien(SinhVien sinhVien) {
        Intent intent = new Intent(BqlSinhVienActivity.this, BqlSinhVienShowActivity.class);
        intent.putExtra("sinhvien", sinhVien);
        startActivity(intent);
    }
}