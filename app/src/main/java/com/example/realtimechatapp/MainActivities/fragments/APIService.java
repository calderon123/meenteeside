package com.example.realtimechatapp.MainActivities.fragments;

import com.example.realtimechatapp.MainActivities.Notification.MyResponse;
import com.example.realtimechatapp.MainActivities.Notification.Sender;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers (
        {
            "Content-Type:application/json",
                    "Authorization:key=AAAANR65Zgc:APA91bHkMTvAmmVYOqN1hsGnYD_UiL2Lr4ZryLn7xQ_EX7F3a5ApmQPwQFZvppth0FQlaup9CUDfM7hcmeMchq45iFSe71cDbBDaNP6A4EIQWzRLiJa5ZQm3vrHI7fSWuzug_Tmzx6jw"

        }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
