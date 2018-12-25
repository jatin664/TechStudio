package com.example.jatin.techstudio.Extras;


import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.jatin.techstudio.Api;
import com.example.jatin.techstudio.CallBackWithRetry;
import com.example.jatin.techstudio.Client;
import com.example.jatin.techstudio.Data1;
import com.example.jatin.techstudio.FetchData;
import com.example.jatin.techstudio.R;
import com.example.jatin.techstudio.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Search extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener  {
private android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private ProgressBar progressBar;
    private String newsImageArry[];
    private String newsTitleArry[];
    private int idArry[];
    private String newsContentArry[];
    private List<Data1> listData1;
    private ImageView imageView;
    private int temp=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar= findViewById(R.id.searchToolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressbarS_id);
        imageView = findViewById(R.id.scrollUpSImageView);
        recyclerView = findViewById(R.id.recyclerviewS_id);
        recyclerView.setNestedScrollingEnabled(true);
        listData1 = new ArrayList<>();

        Crashlytics.log(Log.DEBUG, "Search", "Crash in onCreate method");

        retro();

    }

    private void retro() {

        Crashlytics.log(Log.DEBUG, "Search", "Crash in retro method");

        Retrofit retrofit = new Client(this).getRetrofit("https://jatinkumar664.000webhostapp.com/");

        Api api = retrofit.create(Api.class);

        final Call<List<FetchData>> listCall = api.getFetchData();

        listCall.enqueue(new CallBackWithRetry<List<FetchData>>(listCall) {
            @Override
            public void onResponse(Call<List<FetchData>> call, Response<List<FetchData>> response) {
                List<FetchData> list2 = response.body();

                for(int i=0;i<list2.size();i++){
                    temp++;
                }

                if(temp==0){
                    Toast.makeText(Search.this,"No Data to display",Toast.LENGTH_SHORT).show();
                    return;
                }

                newsTitleArry = new String[temp];
                newsImageArry = new String[temp];
                idArry=new int[temp];
                newsContentArry=new String[temp];


               for(int i=0;i<temp;i++){
                   Data1 data1=new Data1();
                   data1.d1_title=newsTitleArry[i]=list2.get(i).getTitle();
                   data1.d1_newsImage=newsImageArry[i]=list2.get(i).getImage();
                   data1.d1_content=newsContentArry[i]=list2.get(i).getContent();
                   data1.d1_id=idArry[i]=list2.get(i).getId();
               }

                final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Search.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setHasFixedSize(true);
                recyclerAdapter=new RecyclerAdapter(Search.this,listData1);
                recyclerView.setAdapter(recyclerAdapter);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recyclerView.smoothScrollToPosition(0);
                    }
                });

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        MenuItem menuItem=menu.findItem(R.id.search_id);
        menuItem.expandActionView();
        android.support.v7.widget.SearchView searchView=(android.support.v7.widget.SearchView)menuItem.getActionView();
        menuItem.getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        Crashlytics.log(Log.DEBUG, "Search", "Crash in onQueryTextChange method");


        final String userInput=s.toLowerCase();

        final List<Data1> list=new ArrayList<>();

        if(newsTitleArry==null){
            progressBar.setVisibility(View.VISIBLE);
        }

        if(newsTitleArry!=null){
            progressBar.setVisibility(View.GONE);
            for(int i=0;i<newsTitleArry.length;i++){
                if(newsTitleArry[i].toLowerCase().contains(userInput) || newsContentArry[i].toLowerCase().contains(userInput)){
                    Data1 data=new Data1();
                    data.d1_title=newsTitleArry[i];
                    data.d1_newsImage=newsImageArry[i];
                    data.d1_content=newsContentArry[i];
                    data.d1_id=idArry[i];
                    list.add(data);
                }
            }

            recyclerAdapter.updateList(list);
        }
        return true;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
