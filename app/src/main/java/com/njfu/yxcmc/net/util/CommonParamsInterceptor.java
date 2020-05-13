package com.njfu.yxcmc.net.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.njfu.yxcmc.R;
import com.njfu.yxcmc.cache.ACache;
import com.njfu.yxcmc.util.SystemUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 公共参数的封装
 */

public class CommonParamsInterceptor implements Interceptor {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final String PARAM = "";

    private Gson mGson;
    private Context mContext;

    public CommonParamsInterceptor(Context context, Gson gson) {
        this.mContext = context;
        this.mGson = gson;

    }


    @Override
    public Response intercept(Chain chain) throws IOException {

        //拦截请求，获取到原始请求的request
        Request request = chain.request(); // 112.124.22.238:8081/course_api/cniaoplay/featured?p={'page':0}

        try {
            String method = request.method();

            //公共参数
            HashMap<String, Object> commomParamsMap = new HashMap<>();

            commomParamsMap.put("mobileType", "android-phone");
            commomParamsMap.put("locales", Locale.CHINA.getLanguage());
            commomParamsMap.put("mobileBrand", "mobileBrand");
            commomParamsMap.put("mobileSys", "mobileSys");
            commomParamsMap.put("systemVersion", "systemVersion");
            commomParamsMap.put("mobileModel", "mobileModel");
            commomParamsMap.put("ip", SystemUtils.getHostIP());

            if (method.equals("GET")) {

                HttpUrl httpUrl = request.url();

                HashMap<String, Object> rootMap = new HashMap<>();

                Set<String> paramNames = httpUrl.queryParameterNames();

                for (String key : paramNames) {

                    if (PARAM.equals(key)) {

                        //{'page':0}
                        String oldParamJson = httpUrl.queryParameter(PARAM);
                        if (oldParamJson != null) {
                            HashMap<String, Object> p = mGson.fromJson(oldParamJson, HashMap.class); // 原始参数

                            if (p != null) {
                                for (Map.Entry<String, Object> entry : p.entrySet()) {

                                    rootMap.put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    } else {
                        rootMap.put(key, httpUrl.queryParameter(key));
                    }
                }

                //{"publicParams":{"resolution":"1080*1776","sdk":"23","la":"zh","densityScaleFactor":"3.0","imei":"A0000059953B34","os":"C92B437","model":"KIW-CL00"}}

                rootMap.put("publicParams", commomParamsMap); // 重新组装
                String newJsonParams = mGson.toJson(rootMap); // {"page":0,"publicParams":{"imei":'xxxxx',"sdk":14,.....}}


                String url = httpUrl.toString();

                int index = url.indexOf("?");
                if (index > 0) {
                    url = url.substring(0, index);
                }
                url = url + "?" + PARAM + "=" + newJsonParams; //  http://112.124.22.238:8081/course_api/cniaoplay/featured?p= {"page":0,"publicParams":{"imei":'xxxxx',"sdk":14,.....}}

                Log.e("xyh", "intercept: " + url);

                request = request.newBuilder().url(url).build();

            } else if (method.equals("POST")) {

                RequestBody body = request.body();

                HashMap<String, Object> rootMap = new HashMap<>();
                if (body instanceof FormBody) { // form 表单

                    for (int i = 0; i < ((FormBody) body).size(); i++) {

                        rootMap.put(((FormBody) body).encodedName(i), ((FormBody) body).encodedValue(i));
                    }

                } else {  //提交json

                    Buffer buffer = new Buffer();

                    body.writeTo(buffer);

                    String oldJsonParams = buffer.readUtf8();

                    if (!TextUtils.isEmpty(oldJsonParams)) {

                        rootMap = mGson.fromJson(oldJsonParams, HashMap.class); // 原始参数

                        if (rootMap != null) {
                            rootMap.put("publicParams", commomParamsMap); // 重新组装
                            String newJsonParams = mGson.toJson(rootMap); // {"page":0,"publicParams":{"imei":'xxxxx',"sdk":14,.....}}
                            Log.e("xyh", "newJsonParams: " + newJsonParams);
                            request = request.newBuilder().post(RequestBody.create(JSON, newJsonParams)).build();
                        }
                    }

                }

            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return chain.proceed(request);
    }
}

