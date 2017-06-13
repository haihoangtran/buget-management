package com.example.haitran.budgemanagement;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.List;

public class ViewRecordsActivity extends AppCompatActivity{

    private String dataType;
    private Spinner monthDropdown;
    private Spinner yearDropdown;
    private TabLayout viewTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_records);
        setTitle(getString(R.string.view));

        // Create an Back button next to title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Handle month dropdown
        this.monthDropdownHandle();

        //Handle year dropdown
        this.yearDropdownHandle();

        // Get parameter datatype
        this.dataType = getIntent().getStringExtra("data_type");

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
                selectedSpinnerItemshandle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void yearDropdownHandle(){
        this.yearDropdown = (Spinner)findViewById(R.id.year_dropdown);
        List <String> yearItems = new ArrayList<String>(){{
            add("2016");
            add("2017");
        }};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply adapter to spinner
        this.yearDropdown.setAdapter(adapter);
        // Set first selected option
        this.yearDropdown.setSelection(yearItems.size() - 1);
        this.yearDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedSpinnerItemshandle();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });
}

    // Handle all selected items of month and year spinners
    private void selectedSpinnerItemshandle(){
        System.out.println("Month: " + this.monthDropdown.getSelectedItem());
        System.out.println("Year: " + this.yearDropdown.getSelectedItem());
        this.listViewHandle();
    }

    private void tabHandle(){
        this.viewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        dataType = getString(R.string.all_view_tab);
                        System.out.println("Current tab: " + getString(R.string.all_view_tab));
                        listViewHandle();
                        break;
                    case 1:
                        dataType = getString(R.string.withdraw);
                        System.out.println("Current tab: " + getString(R.string.withdraw));
                        listViewHandle();
                        break;
                    case 2:
                        dataType = getString(R.string.deposit);
                        System.out.println("Current tab: " + getString(R.string.deposit));
                        listViewHandle();
                        break;
                }
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
        final ArrayList<ViewData> datas = new ArrayList<>();
        datas.add(new ViewData("place 1", "date 1", "withdraw", "$25,096"));
        datas.add(new ViewData("place 2", "date 2", "withdraw", "$27,096"));
        datas.add(new ViewData("place 3", "date 3", "deposit", "$14,678"));
        datas.add(new ViewData("place 4", "date 4", "withdraw", "$23,678"));
        datas.add(new ViewData("place 5", "date 5", "deposit", "$12,456"));
        datas.add(new ViewData("place 6", "date 6", "deposit", "$9,097"));
        datas.add(new ViewData("place 7", "date 7", "withdraw", "$10,234"));
        datas.add(new ViewData("place 8", "date 8", "withdraw", "$99,567"));
        datas.add(new ViewData("place 9", "date 9", "deposit", "$23,567"));
        datas.add(new ViewData("place 10", "date 10", "deposit", "$12,567"));
        datas.add(new ViewData("place 11", "date 11", "deposit", "$12,879"));
        datas.add(new ViewData("place 12", "date 12", "withdraw", "$19,,563"));
        datas.add(new ViewData("place 13", "date 13", "withdraw", "$4,560"));
        datas.add(new ViewData("place 14", "date 14", "deposit", "$98,576"));
        datas.add(new ViewData("place 15", "date 15", "withdraw", "$12,345"));
        datas.add(new ViewData("place 16", "date 16", "withdraw", "$12,457"));
        datas.add(new ViewData("place 17", "date 17", "withdraw", "$100,485"));
        datas.add(new ViewData("place 18", "date 18", "deposit", "$12,,309"));
        datas.add(new ViewData("place 19", "date 19", "withdraw", "15,346"));
        datas.add(new ViewData("place 20", "date 20", "deposit", "$12,397"));
        datas.add(new ViewData("place 21", "date 21", "withdraw", "$19,235"));
        datas.add(new ViewData("place 22", "date 22", "withdraw", "$12,509"));
        datas.add(new ViewData("place 23", "date 23", "deposit", "$7,349"));
        datas.add(new ViewData("place 24", "date 24", "withdraw", "$28,342"));
        datas.add(new ViewData("place 25", "date 25", "withdraw", "$56,097"));
        datas.add(new ViewData("place 26", "date 26", "deposit", "$49,859"));
        datas.add(new ViewData("place 27", "date 27", "withdraw", "$36,439"));

        ViewRecordsAdapter dataAdapter = new ViewRecordsAdapter(this, 0, datas);
        ListView dataLv = (ListView)findViewById(R.id.data_list_view);
        dataLv.setAdapter(dataAdapter);

        dataLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ViewData selectedData = datas.get(position);
                Intent intent = new Intent(ViewRecordsActivity.this, EditDataActivity.class);
                intent.putExtra("data_type", dataType);
                intent.putExtra("data", (Serializable) selectedData);
                startActivity(intent);
            }
        });

    }
}

