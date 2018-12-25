package com.example.jatin.techstudio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    String BASE_URL="https://jatinkumar664.000webhostapp.com/";

    @GET("shownewsdata.php")
    Call<List<FetchData>> getFetchData();

    @GET("phoneData.php")
    Call<List<FetchData>> getPhoneData();

    @GET("gamingData.php")
    Call<List<FetchData>> getGamingData();

    @GET("laptopData.php")
    Call<List<FetchData>> getLaptopData();

    @GET("tabletsData.php")
    Call<List<FetchData>> getTabletData();

    @GET("tvData.php")
    Call<List<FetchData>> getTVData();

}
