package com.example.ktx_ute.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ktx_ute.AdminData;
import com.example.ktx_ute.Global;
import com.example.ktx_ute.R;

public class MenuOptionsBqlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_options_bql);

        TextView textViewName = findViewById(R.id.textViewName);
        textViewName.setText(Global.getService(AdminData.class).getFullname());

        ImageView imageView3 = findViewById(R.id.imageView3);
        ImageView imageView4 = findViewById(R.id.imageView4);
        ImageView imageView5 = findViewById(R.id.imageView5);

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.getInstance().saveSharedPreferencesValue("Login", "isLogged", false);
                Global.setLoginStatus(false);
                Intent intent = new Intent(MenuOptionsBqlActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent intent = new Intent(MenuOptionsBqlActivity.this, BqlSinhVienActivity.class);
                startActivity(intent);
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                Intent intent = new Intent(MenuOptionsBqlActivity.this, BqlPhongActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView6 = findViewById(R.id.imageView6);
        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuOptionsBqlActivity.this, NoFunctionActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView7 = findViewById(R.id.imageView7);
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuOptionsBqlActivity.this, NoFunctionActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView10 = findViewById(R.id.imageView10);
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuOptionsBqlActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageView9 = findViewById(R.id.imageView9);
        imageView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuOptionsBqlActivity.this, NoFunctionActivity.class);
                startActivity(intent);
            }
        });
    }
}