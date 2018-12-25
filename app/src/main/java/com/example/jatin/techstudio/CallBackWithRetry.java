package com.example.jatin.techstudio;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;

public abstract class CallBackWithRetry<FetchData> implements Callback<FetchData> {

    private static final int TOTAL_RETRY=3;
    private final Call<FetchData> call;
    private int retry_count=0;

    public CallBackWithRetry(Call<FetchData> call){
        this.call=call;
    }

    @Override
    public void onFailure(Call<FetchData> call, Throwable t) {
        if(retry_count++<TOTAL_RETRY){
            Log.v("CallBack","Retrying..."+ retry_count +" out of "+ TOTAL_RETRY);
            retry();
        }
    }

    private void retry() {
        call.clone().enqueue(this);
    }
}
