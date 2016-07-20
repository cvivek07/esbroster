package com.hfad.esbroster;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivek on 21-Apr-16.
 */
public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        TextView txtDate;
        TextView txtName;
        TextView txtShift;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.showtimings_unused, null);
            holder = new ViewHolder();
            holder.txtDate = (TextView) convertView.findViewById(R.id.date_id);
            holder.txtName = (TextView) convertView.findViewById(R.id.name_id);
            holder.txtShift = (TextView) convertView.findViewById(R.id.shift_id);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtDate.setText(rowItem.getDate_string());
        holder.txtName.setText(rowItem.getName_string());
        holder.txtShift.setText(rowItem.getShift_string());

        return convertView;
    }
}
