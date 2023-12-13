package com.example.ktx_ute.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type: application/json"
    })
    @POST("v1/projects/ql-ktx-ute/messages:send")
    Call<ResponseBody> sendNotification(@Header("Authorization") String accessToken, @Body PushNotificationWrapper message);
}
