package com.hfad.esbroster;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

class CustomAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtDate;
    TextView txtName;
    TextView txtShift;

    public CustomAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null){
            convertView = inflater.inflate(R.layout.showtimings_unused,null);
            txtDate=(TextView) convertView.findViewById(R.id.date_id);
            txtName=(TextView) convertView.findViewById(R.id.name_id);
            txtShift=(TextView) convertView.findViewById(R.id.shift_id);
        }
        HashMap<String, String> map=list.get(position);
        txtDate.setText( map.get(Constants.FIRST_COLUMN));
        txtName.setText( map.get(Constants.SECOND_COLUMN));
        txtShift.setText( map.get(Constants.THIRD_COLUMN));
return convertView;
    }
}
