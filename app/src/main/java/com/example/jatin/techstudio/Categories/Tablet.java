package com.example.jatin.techstudio.Categories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.jatin.techstudio.Api;
import com.example.jatin.techstudio.CallBackWithRetry;
import com.example.jatin.techstudio.Client;
import com.example.jatin.techstudio.Data1;
import com.example.jatin.techstudio.Extras.Search;
import com.example.jatin.techstudio.FetchData;
import com.example.jatin.techstudio.R;
import com.example.jatin.techstudio.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Tablet extends Fragment {
    RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private static Bundle savedState=null; //used to save state
    private SharedPreferences sharedPreferences; //for storing the size of count of total data fetched from server

    //widgets
    private ProgressBar progressBar,mainProgressbar;
    private List<Data1> listData1;
    private ImageView imageView,searchImage;

    //Primitives
    private String newsImageArry[],newsTitleArry[];
    private int idArry[];
    private Boolean isScrolling=false;
    private int currentItems,scrolledItems,totalItems;
    private int totalSize=0; //used to hold the value comes from sharedPreference
    private int temp=0,temp2=0,temp3=0;

    public Tablet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tablet, container, false);

        Crashlytics.log(Log.DEBUG, "Tablet", "Crash in onCreateView method");

        try {

            mainProgressbar= view.findViewById(R.id.mainprogressbarTb_id);
            progressBar = view.findViewById(R.id.progressbarTb_id);
            imageView = view.findViewById(R.id.scrollUpTbImageView);
            recyclerView = view.findViewById(R.id.recyclerviewTb_id);
            searchImage = view.findViewById(R.id.searchTbImageView);

            imageView.setVisibility(View.GONE);

            listData1 = new ArrayList<>();

            searchImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getContext(), Search.class));
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerView.smoothScrollToPosition(0);
                }
            });


            sharedPreferences = getContext().getSharedPreferences("myprefTb", Context.MODE_PRIVATE);

            //if savedState has data...
            if (savedState != null && isNetworkAvailable()) {


                //get total items that will be seen by user when data loading from server
                temp2 = savedState.getInt("size_of_adapter");

                //clear the list
                listData1.clear();

                //set up totalSize from sharedPreferences
                if (sharedPreferences.contains("totalSize")) {
                    //get total size in totalSize variabele
                    System.out.println(totalSize = sharedPreferences.getInt("totalSize", Toast.LENGTH_SHORT));
                }

                newsTitleArry=null;
                newsImageArry=null;
                idArry=null;

                //setting up array's by putting values from savedStates
                newsTitleArry = savedState.getStringArray("Title_Array");
                newsImageArry = savedState.getStringArray("Image_Array");
                idArry = savedState.getIntArray("ID_Array");

                restoreState();
            } else {
                retro();
            }

        }

        catch (Exception e){
            Toast.makeText(getContext(),""+e.toString(),Toast.LENGTH_LONG).show();
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void retro(){

        Crashlytics.log(Log.DEBUG, "Tablet", "Crash in retro method");

        try {

            mainProgressbar.setVisibility(View.VISIBLE);

            Retrofit retrofit = new Client(getContext()).getRetrofit("https://jatinkumar664.000webhostapp.com/");

            Api api = retrofit.create(Api.class);

            final Call<List<FetchData>> listCall = api.getTabletData();

            listCall.enqueue(new CallBackWithRetry<List<FetchData>>(listCall) {
                @Override
                public void onResponse(Call<List<FetchData>> call, Response<List<FetchData>> response) {

                    final List<FetchData> list2 = response.body();

                    mainProgressbar.setVisibility(View.GONE);

                    if (list2.size() == 0) {
                        Toast.makeText(getContext(), "No Data to display", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    sharedPreferences.edit().putInt("totalSize", list2.size()).commit();

                    newsTitleArry = new String[list2.size()];
                    newsImageArry = new String[list2.size()];
                    idArry = new int[list2.size()];

                    if (list2.size() < 10) {
                        for (int i = 0; i < list2.size(); i++) {
                            Data1 data1 = new Data1();
                            data1.d1_title = newsTitleArry[i]=list2.get(i).getTitle();
                            data1.d1_newsImage = newsImageArry[i]=list2.get(i).getImage();
                            data1.d1_id = idArry[i]=list2.get(i).getId();
                            listData1.add(data1);
                        }
                    } else if (list2.size() >= 10) {
                        for (int i = 0; i < 10; i++) {
                            Data1 data1 = new Data1();
                            data1.d1_title = newsTitleArry[i]=list2.get(i).getTitle();
                            data1.d1_newsImage = newsImageArry[i]=list2.get(i).getImage();
                            data1.d1_id = idArry[i]=list2.get(i).getId();
                            listData1.add(data1);
                        }
                    }

                    final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerAdapter = new RecyclerAdapter(getContext(), listData1);
                    recyclerView.setAdapter(recyclerAdapter);

                    totalItems=linearLayoutManager.getItemCount();

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            recyclerView.smoothScrollToPosition(0);
                        }
                    });

                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                isScrolling = true;
                                imageView.setVisibility(View.VISIBLE);
                                searchImage.setVisibility(View.VISIBLE);
                            }
                            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                                isScrolling = false;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setVisibility(View.GONE);
                                        searchImage.setVisibility(View.GONE);
                                    }
                                }, 5000);

                            }
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            imageView.setVisibility(View.VISIBLE);
                            searchImage.setVisibility(View.VISIBLE);

                            //get total no. of items
                            totalItems = linearLayoutManager.getItemCount();
                            //get current items on the screen
                            currentItems = linearLayoutManager.getChildCount();
                            //get scrolled out items
                            scrolledItems = linearLayoutManager.findFirstVisibleItemPosition();

                            if (isScrolling && currentItems + scrolledItems >= totalItems - 4 && currentItems + scrolledItems < list2.size()) {
                                isScrolling = false;
                                fetchNewData();
                            } else if (totalItems == list2.size()) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }

            });

        }
        catch (Exception e){
            Toast.makeText(getContext(),""+e.toString(),Toast.LENGTH_LONG).show();
        }


    }

    private void fetchNewData() {

        Crashlytics.log(Log.DEBUG, "Tablet", "Crash in fetchNewData method");

        try {

            progressBar.setVisibility(View.VISIBLE);

            //get the items in adapter in temp
            temp = totalItems;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerView.smoothScrollToPosition(0);
                }
            });

            //get Reference from getRetrofit function of Client class
            Retrofit retrofit = new Client(getContext()).getRetrofit("https://jatinkumar664.000webhostapp.com/");

            Api api = retrofit.create(Api.class);

            //make a call
            final Call<List<FetchData>> listCall = api.getTabletData();

            listCall.enqueue(new CallBackWithRetry<List<FetchData>>(listCall) {
                @Override
                public void onResponse(Call<List<FetchData>> call, Response<List<FetchData>> response) {
                    //get data in list2
                    final List<FetchData> list2 = response.body();

                    for (int j = 0; j < 15 && temp < list2.size(); j++) {
                        Data1 data1 = new Data1();

                        data1.d1_title = newsTitleArry[temp] = list2.get(temp).getTitle();
                        data1.d1_newsImage = newsImageArry[temp] = list2.get(temp).getImage();
                        data1.d1_id = idArry[temp] = list2.get(temp).getId();

                        listData1.add(data1);
                        temp++;

                    }

                    progressBar.setVisibility(View.GONE);
                    recyclerAdapter.notifyDataSetChanged();
                }
            });

        }
        catch (Exception e){
            Toast.makeText(getContext(),""+e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    public void restoreState() {

        Crashlytics.log(Log.DEBUG, "Tablet", "Crash in restoreState method");

        try{


            //if temp2 has items less than 10 then show all items...
            if (temp2 < 10) {
                for (int i = 0; i < temp2; i++) {
                    Data1 data1 = new Data1();
                    data1.d1_id = idArry[i];
                    data1.d1_title = newsTitleArry[i];
                    data1.d1_newsImage = newsImageArry[i];
                    listData1.add(data1);
                }
            }

            //if temp2 has items more than n equal to 10 then show only first 8 items...
            else if (temp2 >= 10) {
                for (int i = 0; i < 10; i++) {
                    Data1 data1 = new Data1();
                    data1.d1_id = idArry[i];
                    data1.d1_title = newsTitleArry[i];
                    data1.d1_newsImage = newsImageArry[i];
                    listData1.add(data1);
                }
            }

            final LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerAdapter = new RecyclerAdapter(getContext(), listData1);
            recyclerView.setAdapter(recyclerAdapter);

            totalItems=linearLayoutManager.getItemCount();


            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //if state=scrolling then...
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true;
                        imageView.setVisibility(View.VISIBLE);
                        searchImage.setVisibility(View.VISIBLE);
                    }
                    //if state=not scrolling ie. at rest then...
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        isScrolling = false;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setVisibility(View.GONE);
                                searchImage.setVisibility(View.GONE);
                            }
                        }, 5000);

                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    imageView.setVisibility(View.VISIBLE);
                    searchImage.setVisibility(View.VISIBLE);

                    //get total no. of items
                    totalItems = linearLayoutManager.getItemCount();
                    //get current items on the screen
                    currentItems = linearLayoutManager.getChildCount();
                    //get scrolled out items
                    scrolledItems = linearLayoutManager.findFirstVisibleItemPosition();

                /*if user is scrolling & current items on the screen and scrolled items are greater
                   than total items in the adapter - 4 ie.. load more as 4 items from top are seen
                    and load until all items are loaded which is stored in totalSize*/
                    if (isScrolling && currentItems + scrolledItems >= totalItems - 4 && currentItems + scrolledItems < totalSize) {
                        isScrolling = false;
                        fetchNewRestoredData();
                    }
                    //if all the items are loaded then...
                    else if (totalItems == totalSize) {
                        progressBar.setVisibility(View.GONE);

                    }
                }
            });

        }

        catch (Exception e){
            Toast.makeText(getContext(),""+e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    private void fetchNewRestoredData() {

        Crashlytics.log(Log.DEBUG, "Tablet", "Crash in fetchNewRestoreData method");

        try {


            progressBar.setVisibility(View.VISIBLE);

            //get the items in adapter in temp3
            temp3 = totalItems;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerView.smoothScrollToPosition(0);
                }
            });

            //get Reference from getRetrofit function of Client class
            Retrofit retrofit = new Client(getContext()).getRetrofit("https://jatinkumar664.000webhostapp.com/");

            Api api = retrofit.create(Api.class);

            //make a call
            final Call<List<FetchData>> listCall = api.getTabletData();

            listCall.enqueue(new CallBackWithRetry<List<FetchData>>(listCall) {
                @Override
                public void onResponse(Call<List<FetchData>> call, Response<List<FetchData>> response) {
                    //get data in list2
                    final List<FetchData> list2 = response.body();

                    for (int j = 0; j < 15 && temp3 < list2.size(); j++) {
                        Data1 data1 = new Data1();

                        data1.d1_title = newsTitleArry[temp3] = list2.get(temp3).getTitle();
                        data1.d1_newsImage = newsImageArry[temp3] = list2.get(temp3).getImage();
                        data1.d1_id = idArry[temp3] = list2.get(temp3).getId();

                        listData1.add(data1);
                        temp3++;

                    }

                    progressBar.setVisibility(View.GONE);
                    recyclerAdapter.notifyDataSetChanged();
                }

            });
        }

        catch(Exception e){
            Toast.makeText(getContext(), "" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onPause() {
        super.onPause();

        Crashlytics.log(Log.DEBUG, "Tablet", "Crash in Pause method");


        try {

            //if list is not empty then save state else set state=null so that retro can be called again
            if (!listData1.isEmpty()) {
                savedState = new Bundle();

                //onPausing save State of the app
                savedState.putStringArray("Title_Array", newsTitleArry); //save title array
                savedState.putStringArray("Image_Array", newsImageArry); //save newsImage array
                savedState.putIntArray("ID_Array", idArry); //save ID array
                savedState.putInt("size_of_adapter", totalItems); //save total items of the adapter
            } else {
                savedState = null;
            }

        }
        catch (Exception e){
            Toast.makeText(getContext(),""+e.toString(),Toast.LENGTH_LONG).show();
        }

    }

}

