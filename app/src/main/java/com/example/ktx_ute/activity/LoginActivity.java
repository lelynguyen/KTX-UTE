package com.example.ktx_ute.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ktx_ute.AdminData;
import com.example.ktx_ute.Global;
import com.example.ktx_ute.R;
import com.example.ktx_ute.StudentData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    private static final int USER_STUDENT = 0;
    private static final int USER_ADMIN = 1;

    private ImageView eyeImageView;
    private EditText passwordEditText, UserEditText;
    private boolean isPasswordVisible = false;
    private boolean isLoginInProcess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        checkLogged();


        eyeImageView = findViewById(R.id.imageView8);
        passwordEditText = findViewById(R.id.loginPassword);
        UserEditText = findViewById(R.id.loginName);

        // Ánh xạ các button và TextView
        View btnLoginSv = findViewById(R.id.btn_login_sv);
        View btnLoginAdmin = findViewById(R.id.btn_login_admin);
        View quenpass = findViewById(R.id.quenpass);

        // Thiết lập sự kiện click cho btnLoginSv
        btnLoginSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến layout activity_menu_options_sv.xml
                if (!isLoginInProcess) {
                    String tk = UserEditText.getText().toString();
                    String mk = passwordEditText.getText().toString();
                    Global.getInstance().makeToast("Đang kiểm tra");
                    LoginSVAsyncTask loginSVAsyncTask = new LoginSVAsyncTask(LoginActivity.this,tk,mk);
                    loginSVAsyncTask.execute();
                    isLoginInProcess = true;
                }
            }
        });

        // Thiết lập sự kiện click cho btnLoginAdmin
        btnLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến layout activity_menu_options_bql.xml
                if (!isLoginInProcess) {
                    String tk = UserEditText.getText().toString();
                    String mk = passwordEditText.getText().toString();
                    Global.getInstance().makeToast("Đang kiểm tra");
                    LoginAdminAsyncTask loginAdminAsyncTask = new LoginAdminAsyncTask(LoginActivity.this,tk,mk);
                    loginAdminAsyncTask.execute();
                    isLoginInProcess = true;
                }
            }
        });

        // Thiết lập sự kiện click cho quenpass (TextView)
        quenpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến layout activity_forget_password.xml
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        eyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    // Nếu mật khẩu đã hiển thị, ẩn mật khẩu và chuyển hình ảnh về img_hidden
                    passwordEditText.setInputType(129); // 129 tương đương với inputType "textPassword"
                    eyeImageView.setImageResource(R.drawable.img_hidden);
                    isPasswordVisible = false;
                } else {
                    // Nếu mật khẩu chưa hiển thị, hiển thị mật khẩu và chuyển hình ảnh về img_see
                    passwordEditText.setInputType(144); // 144 tương đương với inputType "text"
                    eyeImageView.setImageResource(R.drawable.img_see);
                    isPasswordVisible = true;
                }
            }
        });
    }

    public void startIntentSV() {
        Global.getInstance().makeToast("Thành công");
        isLoginInProcess = false;
        Global.setLoginStatus(true);
        Intent intent = new Intent(LoginActivity.this, MenuOptionsSvActivity.class);
        startActivity(intent);
    }

    public void startIntentAdmin() {
        Global.getInstance().makeToast("Thành công");
        isLoginInProcess = false;
        Global.setLoginStatus(true);
        Intent intent = new Intent(LoginActivity.this, MenuOptionsBqlActivity.class);
        startActivity(intent);
    }

    private void checkLogged() {
        if (Global.IsLogged()) {
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        boolean isLogged = sharedPreferences.getBoolean("isLogged", false);
        if (!isLogged) {
            return;
        }

        String result = sharedPreferences.getString("result", "");
        int userType = sharedPreferences.getInt("type", -1);
        switch (userType) {
            case USER_ADMIN:
                isLoginInProcess = true;
                Global.getService(AdminData.class).initData(result, LoginActivity.this);
                break;
            case USER_STUDENT:
                isLoginInProcess = true;
                Global.getService(StudentData.class).initData(result, LoginActivity.this);
                break;
        }
    }

    private void loginSuccessful(int type, String result) {
        SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogged", true);
        editor.putInt("type", type);
        editor.putString("result", result);
        editor.apply();
    }

    public class LoginSVAsyncTask extends AsyncTask<Void , Void , Void> {
        String url_API = "https://spaceofme.000webhostapp.com/api_ktx/login.php";
        Context context;
        String Tk,MK;
        String result;

        public LoginSVAsyncTask(Context context, String tk , String mk) {
            this.context = context;
            Tk = tk;
            MK = mk;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //1 lấy đường dẫn
                URL url  = new URL(url_API);
                //2 xử lý port
                String params ="tenDangNhap=" + URLEncoder.encode(Tk,"utf-8") +
                        "&matKhau=" + URLEncoder.encode(MK,"utf-8");
                //3 mờ kết nối
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //4 set tham so
                httpURLConnection.setDoOutput(true);//co lay output
                httpURLConnection.setRequestMethod("POST");// xac dinh method
                httpURLConnection.setFixedLengthStreamingMode(params.getBytes().length);
                httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(params.getBytes());
                outputStream.flush();
                outputStream.close();

                String line = "";
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                while ((line= bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
                result = stringBuilder.toString();
                httpURLConnection.disconnect();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            Log.e("HttpResult", result);
            if (!result.equals("[]")) {
                loginSuccessful(USER_STUDENT, result);
                Global.getService(StudentData.class).initData(result, LoginActivity.this);
            } else {
                Global.getInstance().makeToast("Sai thông tin");
                isLoginInProcess = false;
                Log.e("LoginResult", "FAILED");
            }
        }
    }


    public class LoginAdminAsyncTask extends AsyncTask<Void , Void , Void> {
        String url_API = "https://spaceofme.000webhostapp.com/api_ktx/login_admin.php";
        Context context;
        String Tk,MK;
        String result;

        public LoginAdminAsyncTask(Context context, String tk , String mk) {
            this.context = context;
            Tk = tk;
            MK = mk;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //1 lấy đường dẫn
                URL url  = new URL(url_API);
                //2 xử lý port
                String params ="tenDangNhap=" + URLEncoder.encode(Tk,"utf-8") +
                        "&matKhau=" + URLEncoder.encode(MK,"utf-8");
                //3 mờ kết nối
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //4 set tham so
                httpURLConnection.setDoOutput(true);//co lay output
                httpURLConnection.setRequestMethod("POST");// xac dinh method
                httpURLConnection.setFixedLengthStreamingMode(params.getBytes().length);
                httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(params.getBytes());
                outputStream.flush();
                outputStream.close();

                String line = "";
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                while ((line= bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }
                result = stringBuilder.toString();
                httpURLConnection.disconnect();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            Log.e("HttpResult", result);
            if (result.contains("admin")) {
                loginSuccessful(USER_ADMIN, result);
                Global.getService(AdminData.class).initData(result, LoginActivity.this);
            } else {
                Global.getInstance().makeToast("Sai thông tin");
                isLoginInProcess = false;
                Log.e("LoginResult", "FAILED");
            }
        }
    }
}