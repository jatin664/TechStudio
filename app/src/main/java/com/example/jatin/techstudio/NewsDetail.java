package com.example.jatin.techstudio;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.crashlytics.android.Crashlytics;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsDetail extends AppCompatActivity {
ImageView imageView;
TextView title,content;
View view;
ProgressBar progressBar;
private int getId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //catch ID thrown
        getId=getIntent().getIntExtra("throwId",0);

        imageView= findViewById(R.id.shownewsimage_id);
        title= findViewById(R.id.shownewstitle_id);
        content= findViewById(R.id.shownewscontent_id);
        progressBar= findViewById(R.id.progressbar_id);
        view= findViewById(R.id.view_id);

        view.setVisibility(View.GONE);

        Crashlytics.log(Log.DEBUG, "NewsDetail", "Crash in onCreate method");

        //if internet connection is not available..
        if(!isNetworkAvailable()){
            Toast.makeText(this,"You're Internet Connection is not active",Toast.LENGTH_LONG).show();
        }

        shownews();

    }
    public void shownews(){

        Crashlytics.log(Log.DEBUG, "NewsDetail", "Crash in showNews method");

        Retrofit retrofit = new Client(this).getRetrofit("https://jatinkumar664.000webhostapp.com/");

        Api api=retrofit.create(Api.class);

        Call<List<FetchData>> listCall=api.getFetchData();

        listCall.enqueue(new CallBackWithRetry<List<FetchData>>(listCall) {
            @Override
            public void onResponse(Call<List<FetchData>> call, Response<List<FetchData>> response) {
                view.setVisibility(View.VISIBLE);
                List<FetchData> fetchData=response.body();

                //load all data and match the data with ID thrown
                for(int i=0;i<fetchData.size();i++){
                    progressBar.setVisibility(View.GONE);

                    if(fetchData.get(i).getId()==getId ){
                        Crashlytics.log(Log.DEBUG, "NewsDetail", "Crash in Glide method");

                        Glide.with(getApplicationContext()).load("https://jatinkumar664.000webhostapp.com/images/"+fetchData.get(i).getImage())
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                                .apply(new RequestOptions().override(480,240))
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        int w=resource.getIntrinsicWidth();
                                        int h=resource.getIntrinsicHeight();

                                        if(w<=h){
                                            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                                            imageView.setLayoutParams(layoutParams);
                                            imageView.setScaleType(ImageView.ScaleType.CENTER);
                                            imageView.setAdjustViewBounds(true);
                                            imageView.setImageDrawable(resource);
                                            }
                                            else{
                                            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,600);
                                            imageView.setLayoutParams(layoutParams);
                                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                            imageView.setImageDrawable(resource);
                                        }
                                    }
                                });

                        title.setText(fetchData.get(i).getTitle());
                        content.setText(fetchData.get(i).getContent());
                        break;
                    }
                }
            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
