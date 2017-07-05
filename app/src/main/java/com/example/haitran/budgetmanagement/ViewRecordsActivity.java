package com.example.haitran.budgetmanagement;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.support.design.widget.TabLayout.Tab;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.List;
import java.util.Map;
import controller.DBController;
import model.RecordModel;

public class ViewRecordsActivity extends AppCompatActivity{

    private String dataType;
    private Spinner monthDropdown;
    private Spinner yearDropdown;
    private TabLayout viewTabLayout;
    private Map <String, String> monthEqual;
    private DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);
        setTitle(getString(R.string.view));

        // Create an Back button next to title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Define some variables
        dbController = DBController.getInstance(ViewRecordsActivity.this);
        monthEqual= new HashMap<String, String>();
        monthEqual.put("January", "01");
        monthEqual.put("February", "02");
        monthEqual.put("March", "03");
        monthEqual.put("April", "04");
        monthEqual.put("May", "05");
        monthEqual.put("June", "06");
        monthEqual.put("July", "07");
        monthEqual.put("August", "08");
        monthEqual.put("September", "09");
        monthEqual.put("October", "10");
        monthEqual.put("November", "11");
        monthEqual.put("December", "12");


        // Get parameter datatype
        this.dataType = getIntent().getStringExtra("data_type");

        //Handle month dropdown
        this.monthDropdownHandle();

        //Handle year dropdown
        this.yearDropdownHandle();

        this.viewTabLayout  = (TabLayout) findViewById(R.id.view_tab);
        this.tabHandle();
        if(this.dataType.equals(getString(R.string.all_view_tab))){
            System.out.println("Handle table view for all tab first time");
            this.listViewHandle();
        }
        else if(this.dataType.equals(getString(R.string.withdraw))) {
            TabLayout.Tab withdrawTab = viewTabLayout.getTabAt(1);
            withdrawTab.select();
        }
        else if(this.dataType.equals(getString(R.string.deposit))) {
            TabLayout.Tab depositTab = viewTabLayout.getTabAt(2);
            depositTab.select();
        }

    }
    /* ******************************************************
               PRIVATE FUNCTIONS
    *********************************************************/
    private void monthDropdownHandle(){
        this.monthDropdown = (Spinner)findViewById(R.id.month_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.month_dropdown_items,
                                                                            android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply adapter to spinner
        this.monthDropdown.setAdapter(adapter);
        // Set first selected option
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        this.monthDropdown.setSelection(calendar.get(Calendar.MONTH));
        // handle on selected items of month spinner
        this.monthDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayedRecords();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void yearDropdownHandle(){
        this.yearDropdown = (Spinner)findViewById(R.id.year_dropdown);
        List <String> yearItems = dbController.getListYears();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply adapter to spinner
        this.yearDropdown.setAdapter(adapter);
        // Set first selected option
        this.yearDropdown.setSelection(yearItems.size() - 1);
        this.yearDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            displayedRecords();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
}

    private void monthlyTotalHandle(){
        /*
        Handle Mothly Total Text
         */
        int month = Integer.parseInt(monthEqual.get(this.monthDropdown.getSelectedItem().toString()));
        int year = Integer.parseInt(this.yearDropdown.getSelectedItem().toString());
        TextView monthTotalTxt = (TextView) findViewById(R.id.monthly_total_text);
        if (dataType.equals(getString(R.string.withdraw))){
            monthTotalTxt.setText(String.format(Locale.US, "$%.2f", dbController.getMonthlyTotal(month, year, true)));
            monthTotalTxt.setTextColor(getResources().getColor(R.color.homeExpenseTxt));
        }else if(dataType.equals(getString(R.string.deposit))){
            monthTotalTxt.setText(String.format(Locale.US, "$%.2f", dbController.getMonthlyTotal(month, year, false)));
            monthTotalTxt.setTextColor(getResources().getColor(R.color.homeBalanceTxt));
        }else{
            monthTotalTxt.setText("");
        }
    }

    // Handle all selected items of month and year spinners
    private void displayedRecords(){
        this.monthlyTotalHandle();
        this.listViewHandle();
    }

    private void tabHandle(){
        this.viewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        dataType = getString(R.string.all_view_tab);
                        break;
                    case 1:
                        dataType = getString(R.string.withdraw);
                        break;
                    case 2:
                        dataType = getString(R.string.deposit);
                        break;
                }
                displayedRecords();
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {
            }
        });
    }

    private void listViewHandle(){
        final ArrayList<RecordModel> records;
        String month = monthEqual.get(this.monthDropdown.getSelectedItem().toString());
        String year = this.yearDropdown.getSelectedItem().toString();
        if (dataType.equals(getString(R.string.all_view_tab))){
            records = dbController.getMonthlyRecords(month, year, true, true);
        }else if (dataType.equals(getString(R.string.withdraw))){
            records = dbController.getMonthlyRecords(month, year, false, true);
        }else{
            records = dbController.getMonthlyRecords(month, year, false, false);
        }

        ViewRecordsAdapter dataAdapter = new ViewRecordsAdapter(this, 0, records);
        ListView dataLv = (ListView)findViewById(R.id.record_list_view);
        dataLv.setAdapter(dataAdapter);

        dataLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final RecordModel selectedData = records.get(position);
                Intent intent = new Intent(ViewRecordsActivity.this, EditDataActivity.class);
                intent.putExtra("record_type", dataType);
                intent.putExtra("record", (Serializable) selectedData);
                startActivity(intent);
            }
        });

    }
}

