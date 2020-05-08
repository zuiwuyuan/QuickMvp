package com.njfu.yxcmc.net;

import com.njfu.yxcmc.bean.UserModel;

import org.json.JSONObject;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {

    String HOME_INSERT_VISIT_LOG = "home/insertVisitLog";

    @POST("user/login")
    @FormUrlEncoded
    Observable<UserModel> login(@Field("username") String username, @Field("password") String password);// 登陆

    @POST("user/register")
    @FormUrlEncoded
    Observable<JSONObject> register(@Body RequestBody requestParams);// 注册

    @GET("user/logout/json")
    @FormUrlEncoded
    Observable<JSONObject> logout(@Body RequestBody requestParams);// 退出

}
