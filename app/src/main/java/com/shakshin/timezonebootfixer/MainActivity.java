package com.shakshin.timezonebootfixer;

import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayList<TimeZone> zones = new ArrayList<TimeZone>();
    TimeZone activeZone = null;
    boolean bootTime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("com.shakshin.timezonebootfixer", 0);

        activeZone = TimeZone.getTimeZone(settings.getString("timeZone", "Europe/Moscow"));
        bootTime = settings.getBoolean("applyOnBoot", true);

        TextView t = (TextView) findViewById(R.id.textActive);
        CheckBox b = (CheckBox) findViewById(R.id.atBoot);
        b.setChecked(bootTime);
        t.setText(activeZone.getDisplayName() + "\n (" + activeZone.getID() + ")");

        ListView list = (ListView) findViewById(R.id.listZones);

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        list.setAdapter(adapter);

        for (String tz : TimeZone.getAvailableIDs()) {
            TimeZone zone = TimeZone.getTimeZone(tz);
            listItems.add(zone.getDisplayName() + "\n (" + tz + ")");
            zones.add(zone);
        }
        adapter.notifyDataSetChanged();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                selectZone(position);
            }
        });
    }

    public void selectZone(int pos) {
        TextView t = (TextView) findViewById(R.id.textActive);

        activeZone = zones.get(pos);
        t.setText(activeZone.getDisplayName() + "\n (" + activeZone.getID() + ")");
        saveSettings();
    }

    public void applyNow(View view) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.setTimeZone(activeZone.getID());
    }

    public void bootChanged(View view) {
        bootTime = ((CheckBox) view).isChecked();
        saveSettings();
    }

    private void saveSettings()
    {
        SharedPreferences settings = getSharedPreferences("com.shakshin.timezonebootfixer", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("applyOnBoot", bootTime);
        editor.putString("timeZone", activeZone.getID());
        editor.commit();
    }

}
