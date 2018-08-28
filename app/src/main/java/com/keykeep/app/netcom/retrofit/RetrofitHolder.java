package com.keykeep.app.netcom.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.keykeep.app.BuildConfig;
import com.keykeep.app.netcom.retrofit.Config;
import com.keykeep.app.utils.AppUtils;
import com.keykeep.app.utils.LogUtils;
import com.keykeep.app.utils.Utils;

import org.json.JSONObject;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManagerFactory;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by akshaydashore on 28/8/18
 */
public class RetrofitHolder {

    public static final int TIMEOUT = 60;
    private Context context;
    private Retrofit retrofit;
    private static KeyKeepAPI service;


    public RetrofitHolder(Context context) {
        this.context = context;
        initService();
    }


    private void initService() {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(getOkHttpClient())
                    .build();

        }
        service = retrofit.create(KeyKeepAPI.class);

    }

    public OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

        okHttpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
//        okHttpClient.addInterceptor(chain -> chain.proceed(getRequestWithHeaders(chain)));

        // If you need an Interceptor to add some header
//        okHttpClient.addInterceptor();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.addInterceptor(interceptor);
        }
        return okHttpClient.build();
    }


    // creating a TrustManager that trusts the CAs in our KeyStore
    private TrustManagerFactory createTrustManagerCAs(KeyStore keyStore) {
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory managerFactory = null;
        try {
            managerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            assert managerFactory != null;
            managerFactory.init(keyStore);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return managerFactory;
    }

    public static KeyKeepAPI getService() {
        return service;
    }


    /**
     * set header in need
     */
    private Request getRequestWithHeaders(Interceptor.Chain chain) {
        Request.Builder builder = chain.request().newBuilder();
//        if (chain.request().url().toString().contains(Config.BASE_URL)) {
//            builder.addHeader(HEADER_API_KEY, Utils.getApiKey());
//            builder.addHeader(HEADER_DEVICE_ID, Utils.getDeviceID(context));
//            builder.addHeader(HEADER_DEVICE_TOKEN, Utils.getToken());
//            builder.addHeader(HEADER_DEVICE_TYPE, Utils.getDeviceType());

//            try {
//                JSONObject settingJsonObject = new JSONObject();
//                settingJsonObject.put("CurrentVersion", BuildConfig.VERSION_NAME);
//                if (Config.USE_BAIDU) {
//                    settingJsonObject.put(REGION_HEADER, ASIA_CHINA_REGION);
//                } else {
//                    settingJsonObject.put(REGION_HEADER, ASIA_REGION);
//                }
//                byte[] settingsByteArray = settingJsonObject.toString().getBytes("UTF-8");
//                builder.addHeader(SETTINGS_HEADER, Base64.encodeToString(settingsByteArray, Base64.NO_WRAP));
//            } catch (Exception e) {
//                LogUtils.e("RetrofitHolder", "Add header settings json" + e, e);
//            }
//        }
        return builder.build();
    }

}
