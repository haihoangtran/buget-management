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
import java.util.Locale;
import constant.Constant;
import model.RecordModel;

/**
 * Created by haitran on 6/4/17.
 */

class ViewRecordsAdapter extends ArrayAdapter<RecordModel> {
    private Context context;
    private List<RecordModel> records;
    private  Constant constant = new Constant();

    public ViewRecordsAdapter(Context context, int resource, ArrayList<RecordModel> records){
        super(context, resource, records);
        this.context = context;
        this.records = records;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RecordModel record = records.get(position);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_record_cell, null);

        // Lookup view for data population
        TextView placeTxt = (TextView) view.findViewById(R.id.place_txt);
        TextView dateTxt = (TextView) view.findViewById(R.id.date_txt);
        TextView typeTxt = (TextView) view.findViewById(R.id.type_txt);
        TextView amountTxt = (TextView) view.findViewById(R.id.amount_txt);

        // Populate the data into the template view using the data object
        placeTxt.setText(record.getPlace());
        dateTxt.setText(record.getDate());
        amountTxt.setText(String.format(Locale.US, "$%.2f", record.getAmount()));
        String typeName = record.getTypeName();
        if(typeName.equals(constant.getRecordTypeExpenseName())){
            typeTxt.setText(this.context.getString(R.string.withdraw).toLowerCase());
            typeTxt.setTextColor(this.context.getResources().getColor(R.color.homeExpenseTxt));
        }else{
            typeTxt.setText(typeName.toLowerCase());
            typeTxt.setTextColor(this.context.getResources().getColor(R.color.homeBalanceTxt));
        }

        // Return the completed view to render on screen
        return view;
    }

}