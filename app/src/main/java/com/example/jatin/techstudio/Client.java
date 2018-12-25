package com.example.jatin.techstudio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    public Retrofit retrofit=null;
    public Context context;

    public Client(Context context) {
        this.context = context;
    }


    public Retrofit getRetrofit(String url) {

        Crashlytics.log(Log.DEBUG, "Client", "Crash in getRetrofit method");

        OkHttpClient okHttpClient=null;
        if(retrofit==null){
                okHttpClient=new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(5,TimeUnit.MINUTES)
                    .addInterceptor(providerOfflineCache())
                    .addNetworkInterceptor(providerCacheInterceptor())
                    .cache(providerCache())
                    .build();
        }

        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }



    private Cache providerCache(){
        Cache cache=null;
        try{
            cache=new Cache(context.getCacheDir(),5*1024*1024); //5MB

        }catch (Exception e){
            Toast.makeText(context, "Cache not found!", Toast.LENGTH_SHORT).show();

        }
        return cache;
    }

    private Interceptor providerCacheInterceptor(){
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response response=chain.proceed(chain.request());

                //rewrite response header to force use of cache
                CacheControl cacheControl=new CacheControl.Builder()
                        .maxAge(10,TimeUnit.MINUTES)
                        .build();

                return response.newBuilder().header("cache_controller",cacheControl.toString())
                        .build();

            }
        };
    }

    private Interceptor providerOfflineCache(){
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request=chain.request();
                if(!isNetworkAvailable()){
                    CacheControl cacheControl=new CacheControl.Builder()
                            .maxStale(7,TimeUnit.DAYS)
                            .build();
                    request=request.newBuilder().cacheControl(cacheControl).build();
                }
                return chain.proceed(request);
            }
        };
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
