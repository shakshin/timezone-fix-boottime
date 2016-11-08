package com.shakshin.timezonebootfixer;

import android.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeZoneGroupAdapter extends BaseExpandableListAdapter {

    private Map<String, ArrayList<TimeZone>> data = new HashMap<String, ArrayList<TimeZone>>();
    private ArrayList<String> keys = new ArrayList<String>();

    public TimeZoneGroupAdapter()
    {
        super();
        for (String zoneId : TimeZone.getAvailableIDs()) {
            TimeZone tz = TimeZone.getTimeZone(zoneId);
            String region = zoneId.split("/")[0];
            if (!data.containsKey(region)) {
                data.put(region, new ArrayList<TimeZone>());
                keys.add(region);
            }
            data.get(region).add(tz);
        }
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(keys.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(keys.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(keys.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1000 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView view = new TextView(parent.getContext());
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        view.setText(keys.get(groupPosition));
        view.setTextSize(24);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TimeZone tz = data.get(keys.get(groupPosition)).get(childPosition);

        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                - TimeUnit.HOURS.toMinutes(hours);

        String id =  tz.getID().split("/")[1];

        String timeZoneString = String.format("%s (GMT %d:%02d)\n%s", id, hours,
                minutes, tz.getDisplayName());

        TextView view = new TextView(parent.getContext());
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT));
        view.setText(timeZoneString);
        view.setPadding(100, 10, 10, 10);
        view.setTextSize(18);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
