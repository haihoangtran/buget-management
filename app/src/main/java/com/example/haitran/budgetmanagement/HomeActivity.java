package com.example.haitran.budgetmanagement;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.BarGraphSeries;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;
import controller.UserController;
import controller.DBController;
import controller.FileController;

public class HomeActivity extends AppCompatActivity {

    private String userName="";
    private UserController userController;
    private DBController dbController;
    private FileController fileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ask Storage Permission
        ActivityCompat.requestPermissions(HomeActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        //User name handle
        userController = new UserController();
        this.userHandle();

        // Define variables
        dbController = DBController.getInstance(HomeActivity.this);
        fileController = new FileController();

        //Grapth view handle
        this.graphViewHandle();

        //Deposit button handle
        this.depositBtnHandle();

        //Withdraw button handle
        this.withdrawBtnHandle();

        //View button handle
        this.viewBtnHandle();

        //Balance button handle
        this.balanceHandle();

        //Expense button handle
        this.expenceHandle();

        //Chart button handle
        this.chartHandle();
    }

    // Handle App Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[]grantResult){
        switch(requestCode){
            case 1:
                if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                    System.out.println("Permission is granted");
                }
                else{
                    Toast toast_msg = Toast.makeText(getApplicationContext(),
                            getString(R.string.app_permission_denied),
                            Toast.LENGTH_SHORT);
                    toast_msg.setGravity(Gravity.CENTER, 0, 0);
                    toast_msg.show();
                }
        }
    }

    // Display menu items on action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Handle action of menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.home_menu_edit_name:
                this.editUserNameAlert(getString(R.string.change_user_name), true, "");
                break;
            case R.id.home_menu_export:
                fileController.exportDBToLocalStorage();
                break;
            case R.id.home_menu_import:
                try {
                    dbController.importDB("local");
                    reloadHomeActivity();
                }catch (FileNotFoundException e){
                    importFileNotFoundAlert();
                }
                break;
        }
        return true;
    }

    /* ******************************************************
                PRIVATE FUNCTIONS
     *********************************************************/
    // Handle user
    private void userHandle(){

        if (!userController.isExistedUser()) {
            this.editUserNameAlert(getString(R.string.add_new_user_name), false, "");
        }
        else{
            updateUserNameText(userController.getUserName());
        }
    }

    //Handle graph view
    private void graphViewHandle(){
        GraphView homeGraphView = (GraphView)findViewById(R.id.home_graph_view);
        homeGraphView.removeAllSeries();

        Calendar now = Calendar.getInstance();
        List<Double> dmTotal = dbController.getMonthlyTotalByYear(now.get(Calendar.YEAR), false);
        List<Double> wmTotal = dbController.getMonthlyTotalByYear(now.get(Calendar.YEAR), true);

        DataPoint [] dataPointsDeposit = new DataPoint[12];
        for (int i = 1; i <=dmTotal.size(); ++i){
            dataPointsDeposit[i-1] = new DataPoint(i, dmTotal.get(i-1));
        }
        DataPoint [] dataPointsWithdraw = new DataPoint[12];
        for (int i = 1; i <=wmTotal.size(); ++i){
            dataPointsWithdraw[i-1] = new DataPoint(i, wmTotal.get(i-1));
        }
        BarGraphSeries<DataPoint> seriesDeposit = new BarGraphSeries<>(dataPointsDeposit);
        BarGraphSeries<DataPoint> seriesWithdraw = new BarGraphSeries<>(dataPointsWithdraw);
        homeGraphView.getViewport().setXAxisBoundsManual(true);
        homeGraphView.getViewport().setMinX(1);
        homeGraphView.getViewport().setMaxX(12);
        homeGraphView.getGridLabelRenderer().setNumHorizontalLabels(12);
        homeGraphView.setTitle(Integer.toString(now.get(Calendar.YEAR)));
        //Add series of deposit
        homeGraphView.addSeries(seriesDeposit);
        seriesDeposit.setColor(Color.GREEN);
        //Add series of withdraw
        homeGraphView.addSeries(seriesWithdraw);
        seriesWithdraw.setColor(Color.RED);

        homeGraphView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });
    }

    // Handle Deposit Button
    private void depositBtnHandle(){
        TextView depotsitTxt = (TextView) findViewById(R.id.deposit_txt);
        depotsitTxt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, AddDataActivity.class);
                intent.putExtra("data_type", getString(R.string.deposit));
                startActivity(intent);
            }
        });
    }

    // Handle Withdraw button
    private void withdrawBtnHandle(){
        TextView withdrawTxt = (TextView) findViewById(R.id.withdraw_txt);
        withdrawTxt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, AddDataActivity.class);
                intent.putExtra("data_type", getString(R.string.withdraw));
                startActivity(intent);
            }
        });
    }

    // Handle View button
    private void viewBtnHandle(){
        TextView viewTxt = (TextView)findViewById(R.id.view_txt);
        viewTxt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, ViewRecordsActivity.class);
                intent.putExtra("data_type", getString(R.string.all_view_tab));
                startActivity(intent);
            }
        });
    }

    //Handle Balance button
    private void balanceHandle(){
        LinearLayout balanceLayout = (LinearLayout)findViewById(R.id.balance_layout);
        TextView balanceValue = (TextView)findViewById(R.id.balance_value);
        balanceValue.setText(String.format(Locale.US, "$%.2f", dbController.getCurrentBalance()));
        balanceLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, ViewRecordsActivity.class);
                intent.putExtra("data_type", getString(R.string.deposit));
                startActivity(intent);
            }
        });
    }

    //Handle Expense button
    private void expenceHandle(){
        LinearLayout expenseLayout = (LinearLayout)findViewById(R.id.expense_layout);
        TextView expenseValue = (TextView)findViewById(R.id.expense_value);
        Calendar now = Calendar.getInstance();
        expenseValue.setText(String.format(Locale.US, "$%.2f", dbController.getMonthlyTotal(now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR), true)));
        expenseLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, ViewRecordsActivity.class);
                intent.putExtra("data_type", getString(R.string.withdraw));
                startActivity(intent);
            }
        });
    }

    //Handle Chart button
    private void chartHandle(){
        LinearLayout chartLayout = (LinearLayout)findViewById(R.id.chart_layout);
        chartLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUserNameText(String newName){
        getSupportActionBar().setSubtitle(newName);
    }

    private void editUserNameAlert(final String alertTitle, final Boolean visibleCancel, final String error_msg){

        // Define and initialize elements
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View custom_view = getLayoutInflater().inflate(R.layout.username_edit_dialog, null);
        TextView title = (TextView) custom_view.findViewById(R.id.username_edit_title);
        title.setText(alertTitle);
        TextView error_txt = (TextView) custom_view.findViewById(R.id.empty_txt_error_msg);
        if (error_msg.matches("")){
            error_txt.setVisibility(View.INVISIBLE);
        }
        else{
            error_txt.setText(error_msg);
            error_txt.setVisibility(View.VISIBLE);
        }
        final EditText nameTf = (EditText) custom_view.findViewById(R.id.username_edit_text);
        Button okBtn = (Button) custom_view.findViewById(R.id.username_edit_ok_btn);
        Button cancelBtn = (Button) custom_view.findViewById(R.id.username_edit_cancel_btn);
        if (!visibleCancel){
            cancelBtn.setVisibility(View.INVISIBLE);
        }
        else{
            cancelBtn.setVisibility(View.VISIBLE);
        }
        // Set View
        builder.setView(custom_view);
        final AlertDialog customDialog = builder.create();
        // Action of Buttons
        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
                userName = nameTf.getText().toString();
                if (userName.matches("")){
                    editUserNameAlert(alertTitle, visibleCancel, getString(R.string.empty_username_txt_msg));
                }
                else {
                    if (alertTitle.matches(getString(R.string.add_new_user_name))){
                        dbController.getReadableDatabase();
                        dbController.addDefaultRecordType();
                        reloadHomeActivity();
                    }
                    userController.saveUserName(userName);
                    updateUserNameText(userName);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                customDialog.dismiss();
            }
        });
        //Show dialog
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
    }

    private void importFileNotFoundAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.file_not_found_alert_title));
        builder.setMessage(getString(R.string.file_not_found_alert_msg));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){

            }
        } );
        builder.create().show();
    }

    private void reloadHomeActivity(){
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
