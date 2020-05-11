package com.example.access_standard;

import android.content.Context;
import android.content.Intent;


public interface IBroadcastReceiverStandard {

    public void attach(Context context);

    public void onReceive(Context context, Intent intent);

}
