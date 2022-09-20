package com.example.backgroundoperation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class NetworkConnectivityCheckReciver extends BroadcastReceiver {

    private ConetivityInternetInterface conetivityInternetInterface;

    public NetworkConnectivityCheckReciver(ConetivityInternetInterface conetivityInternetInterface) {
        this.conetivityInternetInterface = conetivityInternetInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkConnected(context)) {
            conetivityInternetInterface.isConnected(true);
        } else {
            conetivityInternetInterface.isConnected(false);
        }


    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


}
