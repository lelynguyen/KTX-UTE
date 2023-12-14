package com.example.ktx_ute.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ktx_ute.R;
import com.example.ktx_ute.adapter.PhongAdapter;
import com.example.ktx_ute.apiutils.ApiUtil;
import com.example.ktx_ute.interfacee.IOnClick;
import com.example.ktx_ute.model.Phong;
import com.example.ktx_ute.model.SinhVien;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BqlThemSvTimPhongActivity extends AppCompatActivity implements IOnClick {
    private List<Phong> phongList;
    private RecyclerView recyclerView;
    PhongAdapter phongAdapter;
    Phong phong;
    SinhVien sinhVien;

    androidx.appcompat.widget.SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bql_them_sv_tim_phong);

        phong = (Phong) getIntent().getSerializableExtra("phong");
        sinhVien = (SinhVien) getIntent().getSerializableExtra("sinhvien");
        phongList = new ArrayList<>();
        phongAdapter = new PhongAdapter(phongList);
        searchView = findViewById(R.id.search_view);
        ImageView buttonBack = findViewById(R.id.buttonBack);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thực hiện hành động để chuyển về giao diện trước, ví dụ:
                onBackPressed(); // Sử dụng phương thức onBackPressed() để quay lại màn hình trước đó.
            }
        });



        callApi();

        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                phongAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                phongAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    public void callApi() {
        ApiUtil.apiutil.getPhong().enqueue(new Callback<List<Phong>>() {
            @Override
            public void onResponse(Call<List<Phong>> call, Response<List<Phong>> response) {
                phongList = response.body();
                phongAdapter = new PhongAdapter(phongList);
                recyclerView.setAdapter(phongAdapter);
                phongAdapter.onClickItem(BqlThemSvTimPhongActivity.this::onClickPhong);
            }

            @Override
            public void onFailure(Call<List<Phong>> call, Throwable t) {
                Toast.makeText(BqlThemSvTimPhongActivity.this, "Loi!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClickPhong(Phong phong) {
        Intent intent = new Intent(BqlThemSvTimPhongActivity.this, BqlThemSvActivity.class);
        intent.putExtra("phong", phong);
        intent.putExtra("sinhvien", sinhVien);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (phongAdapter != null) {
            phongAdapter.notifyDataSetChanged();
        }
    }
}