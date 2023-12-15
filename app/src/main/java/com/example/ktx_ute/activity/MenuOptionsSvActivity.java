package com.example.ktx_ute.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ktx_ute.AdminData;
import com.example.ktx_ute.Global;
import com.example.ktx_ute.R;
import com.example.ktx_ute.StudentData;

public class MenuOptionsSvActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options_sv);

        TextView textViewName = findViewById(R.id.textViewName);
        textViewName.setText(Global.getService(StudentData.class).getFullname());

        // Tạo một ImageView và đặt sự kiện click cho nó
        ImageView imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Global.getService(StudentData.class).HasRoom()) {
                    Intent intent = new Intent(MenuOptionsSvActivity.this, ChatActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Chưa đăng ký phòng", Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageView imageView3 = findViewById(R.id.imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.getInstance().saveSharedPreferencesValue("Login", "isLogged", false);
                Global.setLoginStatus(false);
                Global.getService(StudentData.class).removeToken();

                Intent intent = new Intent(MenuOptionsSvActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView4 = findViewById(R.id.imageView4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuOptionsSvActivity.this, SvDangKiPhongActivity.class);
                intent.putExtra("sinhvien", Global.getService(StudentData.class).getSinhVien());
                startActivity(intent);
            }
        });

        ImageView imageView5 = findViewById(R.id.imageView5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuOptionsSvActivity.this, NoFunctionActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView7 = findViewById(R.id.imageView7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuOptionsSvActivity.this, NoFunctionActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView10 = findViewById(R.id.imageView10);
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuOptionsSvActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView9 = findViewById(R.id.imageView9);
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuOptionsSvActivity.this, NoFunctionActivity.class);
                startActivity(intent);
            }
        });
    }
}
