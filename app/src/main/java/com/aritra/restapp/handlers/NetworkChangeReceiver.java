package com.aritra.restapp.handlers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);

//        Snackbar.make(context, status, Snackbar.LENGTH_LONG).show();
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();

    }
}
