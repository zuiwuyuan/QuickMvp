package com.njfu.yxcmc.net.converter.json;

import android.text.TextUtils;

import com.njfu.yxcmc.net.base.BaseException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    JsonResponseBodyConverter() {
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String jsonString = value.string();
        try {
            JSONObject dataObj = new JSONObject(jsonString);
            int resultCode = dataObj.optInt("resultCode", 0);
            if (resultCode != 0) {
                String resultMessage = dataObj.optString("resultMessage","未知错误");
                if (TextUtils.isEmpty(resultMessage)) {
                    resultMessage = BaseException.OTHER_MSG;
                }
                //异常处理
                throw new BaseException(resultMessage, resultCode);
            }

            JSONObject dataBeanJson = dataObj.optJSONObject("bean");
            if (dataBeanJson == null) {
                dataBeanJson = new JSONObject();
                dataBeanJson.put("resultMessage", dataObj.optString("resultMessage"));
            }
            return (T) dataBeanJson;

        } catch (JSONException e) {
            e.printStackTrace();
            //数据解析异常
            throw new BaseException(BaseException.PARSE_ERROR_MSG, BaseException.PARSE_ERROR);
        } finally {
            value.close();
        }
    }
}
