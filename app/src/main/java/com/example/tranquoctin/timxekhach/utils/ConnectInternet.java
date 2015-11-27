package com.example.tranquoctin.timxekhach.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by sapui on 11/20/2015.
 */
public class ConnectInternet {
    public static boolean isInternetAvailable(Context context) {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(
                            Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();

        } catch (Exception e) {
//            Log.e("isInternetAvailable",e.getMessage());
            e.printStackTrace();
            return false;
        }


    }
}
