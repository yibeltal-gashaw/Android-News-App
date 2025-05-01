package com.estifatech.newsapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private NetworkChangeListener listener;
    public interface NetworkChangeListener{
        void onNetworkAvailable();
        void onNetworkUnavailable();
    }
    public void setNetworkChangeListener(NetworkChangeListener listener){
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE
        );
        if (connectivityManager == null) return;

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(
                connectivityManager.getActiveNetwork()
        );
        if (capabilities != null){
            boolean hasInternet =
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            if(hasInternet){
                if(listener != null) listener.onNetworkAvailable();
            }
            else {
                if(listener != null) listener.onNetworkUnavailable();
            }
        }
        else{
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
                if(listener != null) listener.onNetworkAvailable();
            }
            else {
                if (listener != null) listener.onNetworkUnavailable();
            }
        }

    }
}
