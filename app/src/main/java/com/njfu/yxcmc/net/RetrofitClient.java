package com.njfu.yxcmc.net;

import com.njfu.yxcmc.net.converter_gson.CustomGsonConverterFactory;
import com.njfu.yxcmc.net.converter_json.JsonConverterFactory;
import com.njfu.yxcmc.net.util.HttpLogger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final int CONNECTION_TIME_OUT = 60000;//连接超时时间
    private static final int SOCKET_TIME_OUT = 60000;//读写超时时间
    private static final int MAX_IDLE_CONNECTIONS = 30;// 空闲连接数
    private static final long KEEP_ALLIVE_TIME = 60000L;//保持连接时间

    private static volatile RetrofitClient instance;
    private static String authorization = "";
    private Retrofit mRetrofit, mRetrofitGson, mRetrofitRx, mRetrofitRxGson, mRetrofitRxCustomGson, mRetrofitRxJson;
    private OkHttpClient okHttpClient;

    private RetrofitClient() {

        ConnectionPool connectionPool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALLIVE_TIME, TimeUnit.MILLISECONDS);

        //初始化一个client,不然retrofit会自己默认添加一个
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS) //设置连接超时时间
                .readTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS) //设置读取超时时间
                .writeTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectionPool(connectionPool)
                //设置Header
                .addInterceptor(getHeaderInterceptor())
                //设置拦截器
                .addInterceptor(getLogInterceptor())
//                .retryOnConnectionFailure(false) //自动重连设置为false
//                .addInterceptor(new RetryIntercepter(2)) //重试拦截器2次
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RemoteApi.getHostBasePathUrl())
                .build();

        mRetrofitGson = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RemoteApi.getHostBasePathUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRetrofitRx = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RemoteApi.getHostBasePathUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //RxJava
                .build();

        mRetrofitRxGson = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RemoteApi.getHostBasePathUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //RxJava
                .build();

        mRetrofitRxCustomGson = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RemoteApi.getHostBasePathUrl())
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //RxJava
                .build();

        mRetrofitRxJson = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(RemoteApi.getHostBasePathUrl())
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //RxJava
                .build();
    }

    public static String getAuthorization() {
        return authorization;
    }

    /**
     * 设置Authorization,登录时后台返回
     *
     * @param authorization authorization
     */
    public static void setAuthorization(String authorization) {
        RetrofitClient.authorization = authorization;
    }

    public static OkHttpClient getAndroidNetworkingClient() {
        ConnectionPool connectionPool = new ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALLIVE_TIME, TimeUnit.MILLISECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //显示日志
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //初始化一个client,不然retrofit会自己默认添加一个
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS) //设置连接超时时间
                .readTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS) //设置读取超时时间
                .writeTimeout(SOCKET_TIME_OUT, TimeUnit.MILLISECONDS)
                .connectionPool(connectionPool)
                .addInterceptor(interceptor)
                .build();

        return okHttpClient;
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 设置Header
     *
     * @return
     */
    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        //添加Token
                        .addHeader("Authorization", authorization)
                        .addHeader("Content-Type", "application/json;charset=UTF-8");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
    }

    /**
     * 设置拦截器
     *
     * @return
     */
    private Interceptor getLogInterceptor() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLogger());
        //显示日志
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    public APIService getService() {
        return mRetrofit.create(APIService.class);
    }

    public APIService getServiceGson() {
        return mRetrofitGson.create(APIService.class);
    }

    public APIService getServiceRx() {
        return mRetrofitRx.create(APIService.class);
    }

    public APIService getServiceRxCustomGson() {
        return mRetrofitRxCustomGson.create(APIService.class);
    }

    public APIService getServiceRxGson() {
        return mRetrofitRxGson.create(APIService.class);
    }

    public APIService getServiceRxJson() {
        return mRetrofitRxJson.create(APIService.class);
    }

    class RetryIntercepter implements Interceptor {

        public int maxRetryCount;
        private int count = 0;

        public RetryIntercepter(int maxRetryCount) {
            this.maxRetryCount = maxRetryCount;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {

            return retry(chain);
        }

        public Response retry(Chain chain) {
            Response response = null;
            Request request = chain.request();
            try {
                response = chain.proceed(request);
                while (!response.isSuccessful() && count < maxRetryCount) {
                    count++;
                    response = retry(chain);
                }
            } catch (Exception e) {
                while (count < maxRetryCount) {
                    count++;
                    response = retry(chain);
                }
            }
            return response;
        }
    }
}
