package com.apnitor.arete.pro.api;

import android.app.Application;
import android.util.Log;


import com.apnitor.arete.pro.application.MaidPickerApplication;
import com.apnitor.arete.pro.util.ConnectivityHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {

    private static volatile Retrofit retrofit = null;

    static String LOG_TAG = "ApiService";

    private ApiService() {
    }

    private static Retrofit getInstance(Application application) {
        if (retrofit == null) {
            synchronized (ApiService.class) {
                if (retrofit == null) {

//                    String ip = "192.168.1.3:3000";   // Local
//                    String ip = "18.225.33.228:3000";     // AWS
                    // String ip = "calm-basin-34367.herokuapp.com";     // Heroku

                   String baseUrl = "https://safe-depths-94053.herokuapp.com/maidPicker/"; // HAVE TO USE
                    // String baseUrl = "http://ec2-3-19-53-80.us-east-2.compute.amazonaws.com:8000/maidPicker/";


                 //   String baseUrl = "http://192.168.4.97:8020/maidPicker/"; // LOCAL


                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(msg ->

                            //Log.d("logger", msg));


                          Log.d("logger==", msg)


                    );




                    //  Constatnt.ERR_MSG=msg);



                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


//                    Interceptor authorizationInterceptor = chain -> {
//                        String authToken = ((MaidPickerApplication) application)
//                                .getSharedPreferenceHelper()
//                                .getAuthToken();
//
//                        Log.e("token", "" + authToken);
//                        return chain.proceed(
//                                authToken.isEmpty()
//                                        ? chain.request()
//                                        : chain.request().newBuilder()
//                                        .addHeader("access-token", authToken)
//                                        .build()
//
//                        );
//                    };


                    Interceptor authorizationInterceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            String authToken = ((MaidPickerApplication) application)
                                    .getSharedPreferenceHelper()
                                    .getAuthToken();
                            Log.d(" auth token", "" + authToken);
                            Request request =
                                    authToken.isEmpty()
                                            ? chain.request()
                                            : chain.request().newBuilder()
                                            .addHeader("access-token", authToken)
                                            .build();
                            Response response = null;
                            if (ConnectivityHelper.isConnectedToNetwork(application)) {
                             response = chain.proceed(request);}
//                            Log.d(LOG_TAG, " API response is " + response.toString());
                            if (response.code() == 403) {
//                                handleForbiddenResponse();
                            }
                            return response;
                        }
                    };
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(loggingInterceptor)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .addInterceptor(authorizationInterceptor)
                            .build();
                    retrofit = new Retrofit
                            .Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(client)
                            .build();

                }
            }
        }
        return retrofit;
    }

    public static ProfileApi getProfileApi(Application application) {
        return getInstance(application).create(ProfileApi.class);
    }

    public static JobSpecificationApi getJobSpecificationApi(Application application) {
        return getInstance(application).create(JobSpecificationApi.class);
    }


}

