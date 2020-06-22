package com.njfu.yxcmc.net.converter.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.njfu.yxcmc.net.base.BaseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

public class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;

    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource buffer = Okio.buffer(value.source());
        String jsonString = buffer.readUtf8();
        try {
            JSONObject object = new JSONObject(jsonString);
            int errorCode = object.getInt("errorCode");
            if (errorCode != 0) {
                String errorMsg = object.optString("errorMsg","未知错误");
                //异常处理
                throw new BaseException(errorMsg, errorCode);
            }
            return adapter.fromJson(object.getString("data"));
        } catch (JSONException e) {
            e.printStackTrace();
            //数据解析异常
            throw new BaseException(BaseException.PARSE_ERROR_MSG, BaseException.PARSE_ERROR);
        } finally {
            value.close();
        }
    }

}
