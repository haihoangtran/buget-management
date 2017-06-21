package com.example.haitran.budgetmanagement;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by haitran on 6/4/17.
 */

class ViewRecordsAdapter extends ArrayAdapter<ViewData> {
    private Context context;
    private List<ViewData> datas;


    public ViewRecordsAdapter(Context context, int resource, ArrayList<ViewData> datas){
        super(context, resource, datas);
        this.context = context;
        this.datas = datas;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ViewData data = datas.get(position);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_record_cell, null);

        // Lookup view for data population
        TextView placeTxt = (TextView) view.findViewById(R.id.place_txt);
        TextView dateTxt = (TextView) view.findViewById(R.id.date_txt);
        TextView typeTxt = (TextView) view.findViewById(R.id.type_txt);
        TextView amountTxt = (TextView) view.findViewById(R.id.amount_txt);

        // Populate the data into the template view using the data object
        placeTxt.setText(data.place);
        dateTxt.setText(data.date);
        typeTxt.setText(data.typeData);
        amountTxt.setText(data.amount);

        // Return the completed view to render on screen
        return view;
    }

}