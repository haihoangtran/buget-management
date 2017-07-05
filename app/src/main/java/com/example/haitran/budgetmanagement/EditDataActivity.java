package com.example.haitran.budgetmanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import model.RecordModel;

public class EditDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        setTitle(getString(R.string.edit_title));

        // Create an Back button next to title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Sample
        String datatype = getIntent().getStringExtra("record_type");
        RecordModel data = (RecordModel) getIntent().getSerializableExtra("record");
        TextView sampleTxt = (TextView)findViewById(R.id.this_sample_Text);
//        TextView place = (TextView)findViewById(R.id.place_txt);
//        TextView date = (TextView)findViewById(R.id.date_txt);
//        TextView typeData = (TextView)findViewById(R.id.type_txt);
//        TextView amount = (TextView)findViewById(R.id.amount_txt);
//
//        sampleTxt.setText(datatype);
//        place.setText(data.place);
//        date.setText(data.date);
//        typeData.setText(data.typeData);
//        amount.setText(data.amount);


    }

    // Click on back button on nav, go back to parent screen with all setting up.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
