package com.shakshin.timezonebootfixer;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences settings = context.getSharedPreferences("com.shakshin.timezonebootfixer", 0);
        if (settings.getBoolean("applyOnBoot", false)) {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.setTimeZone(settings.getString("timeZone", "Europe/Moscow"));
        }
    }
}
