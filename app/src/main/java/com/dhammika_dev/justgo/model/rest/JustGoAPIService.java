package com.dhammika_dev.justgo.model.rest;

import com.dhammika_dev.justgo.common.constants.DomainConstants;
import com.dhammika_dev.justgo.model.rest.exception.RxErrorHandlingCallAdapterFactory;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JustGoAPIService {

    private JustGoAPI mJustGoAPI;

    public JustGoAPIService() {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(DomainConstants.SERVER_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().disableHtmlEscaping().create()))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .build();

        mJustGoAPI = restAdapter.create(JustGoAPI.class);
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });


        // enable logs only for debug version
//        if(BuildConfig.DEBUG) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(); // Log Requests and Responses
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        System.out.println(HttpLoggingInterceptor.Level.BODY + " ==============APIService=============");


        client.addInterceptor(logging);
//            client.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                okhttp3.Response response = chain.proceed(request);
//
//                // todo deal with the issues the way you need to
//                if (response.code() == 404) {
//                    BaseApplication.getBaseApplication().showAlertDialog(true,"Something went Wrong!","Application server side error occur",new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                            setLoading(false);
//                        }
//                    }
//                    );
//
////                    return response;
//                }
//
//                return response;
//            }
//        });
//        }

        return client.build();
    }

    public JustGoAPI getApi() {
        return mJustGoAPI;
    }

}
