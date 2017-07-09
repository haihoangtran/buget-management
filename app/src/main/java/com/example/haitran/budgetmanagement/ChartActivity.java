package com.example.haitran.budgetmanagement;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import java.util.List;
import controller.DBController;

public class ChartActivity extends AppCompatActivity {
    private DBController dbController;
    private Spinner yearDropdown;
    private TabLayout viewTabLayout;
    private int tabPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setTitle(getString(R.string.chart));

        // Create an Back button next to title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Defines some variables
        this.dbController = DBController.getInstance(ChartActivity.this);

        //Handle year dropdown
        this.yearDropdownHandle();

        // View Tab layout
        this.viewTabLayout  = (TabLayout) findViewById(R.id.chart_view_tab);
        this.tabHandle();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                finish();
                Intent intent = new Intent(ChartActivity.this, HomeActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

     /* ******************************************************
               PRIVATE FUNCTIONS
    *********************************************************/
     private void yearDropdownHandle() {
         this.yearDropdown = (Spinner) findViewById(R.id.chart_year_dropdown);
         List<String> yearItems = dbController.getListYears();

         ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearItems);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         // Apply adapter to spinner
         this.yearDropdown.setAdapter(adapter);
         // Set first selected option
         this.yearDropdown.setSelection(yearItems.size() - 1);
         this.yearDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 grapHandle();
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
     }

    private void tabHandle(){
        this.viewTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                grapHandle();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void grapHandle(){
        /*
        Handle Graph View
         */
        int year = Integer.parseInt(this.yearDropdown.getSelectedItem().toString());
        switch(this.tabPosition){
            case 0:
                displayGraph(year, true, true);
                break;
            case 1:
                displayGraph(year, true, false);
                break;
            case 2:
                displayGraph(year, false, true);
                break;
        }
    }

    private void displayGraph(int year, boolean is_withdraw, boolean is_deposit){
        /*
        Displayed graph by year and type of record
         */
        GraphView graphVew = (GraphView) findViewById(R.id.chart_graph_view);
        graphVew.removeAllSeries();
        List<Double> dmTotal = dbController.getMonthlyTotalByYear(year, false);
        List<Double> wmTotal = dbController.getMonthlyTotalByYear(year, true);

        DataPoint[] dataPointsDeposit = new DataPoint[12];
        for (int i = 1; i <=dmTotal.size(); ++i){
            dataPointsDeposit[i-1] = new DataPoint(i, dmTotal.get(i-1));
        }
        DataPoint [] dataPointsWithdraw = new DataPoint[12];
        for (int i = 1; i <=wmTotal.size(); ++i){
            dataPointsWithdraw[i-1] = new DataPoint(i, wmTotal.get(i-1));
        }
        BarGraphSeries<DataPoint> seriesDeposit = new BarGraphSeries<>(dataPointsDeposit);
        BarGraphSeries<DataPoint> seriesWithdraw = new BarGraphSeries<>(dataPointsWithdraw);
        graphVew.getViewport().setXAxisBoundsManual(true);
        graphVew.getViewport().setMinX(1);
        graphVew.getViewport().setMaxX(12);
        graphVew.getGridLabelRenderer().setNumHorizontalLabels(12);

        //Add series of deposit
        if(is_deposit) {
            graphVew.addSeries(seriesDeposit);
            seriesDeposit.setColor(Color.GREEN);
            seriesDeposit.setDrawValuesOnTop(true);
            seriesDeposit.setValuesOnTopColor(Color.GRAY);
        }
        //Add series of withdraw
        if(is_withdraw) {
            graphVew.addSeries(seriesWithdraw);
            seriesWithdraw.setColor(Color.RED);
            seriesWithdraw.setDrawValuesOnTop(true);
            seriesWithdraw.setValuesOnTopColor(Color.GRAY);
        }
    }

}
