package com.idea.church.commons;

import com.idea.church.models.AudioResponse;
import com.idea.church.models.DevotionResponse;
import com.idea.church.models.FeedbackResponse;
import com.idea.church.models.LeaderResponse;
import com.idea.church.models.LoginResponse;
import com.idea.church.models.MaterialResponse;
import com.idea.church.models.NotificationResponse;
import com.idea.church.models.PrayerRequestResponse;
import com.idea.church.models.ProfileResponse;
import com.idea.church.models.TestimonyResponse;
import com.idea.church.models.VideoResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {
    @GET("/api/audios")
    Call<AudioResponse> getPreachings(@Query("query") String query);

    @GET("/api/videos")
    Call<VideoResponse> getVideos(@Query("query") String query);

    @GET("/api/materials")
    Call<MaterialResponse> getMaterials(@Query("query") String query);

    @GET("/api/devotion")
    Call<DevotionResponse> getDevotions();

    @GET("/api/leaders")
    Call<LeaderResponse> getLeaders();

    @GET("/api/notifications")
    Call<NotificationResponse> getNotifications();

    @GET("/api/profile")
    Call<ProfileResponse> getProfile();

    @FormUrlEncoded
    @POST("/api/profile")
    Call<ProfileResponse> updateProfile(@Field("email") String email,
                                        @Field("full_name") String fullName,
                                        @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("/api/auth/login")
    Call<LoginResponse> logIn(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/auth/register")
    Call<LoginResponse> register(@Field("username") String username,
                                 @Field("password") String password,
                                 @Field("church_id") String churchId,
                                 @Field("full_name") String fullName,
                                 @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("/api/testimonies/")
    Call<TestimonyResponse> postTestimony(@Field("testimony") String testimony);

    @FormUrlEncoded
    @POST("/api/prayer-requests/")
    Call<PrayerRequestResponse> postPrayerRequest(@Field("request") String request);

    @FormUrlEncoded
    @POST("/api/feedback/")
    Call<FeedbackResponse> postFeedback(@Field("feedback") String feedback);
}
