package com.example.haitran.budgetmanagement;

import android.Manifest;
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
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
        List<Double> dmTotal = new ArrayList<>();
        dmTotal.add(2345.00);
        dmTotal.add(3245.23);
        dmTotal.add(1345.98);
        dmTotal.add(2536.75);
        dmTotal.add(2897.65);
        dmTotal.add(3176.45);
        dmTotal.add(1256.87);
        dmTotal.add(2965.35);
        dmTotal.add(3785.67);
        dmTotal.add(3333.55);
        dmTotal.add(1234.67);
        dmTotal.add(2765.50);
        List<Double> wmTotal = new ArrayList<>();
        wmTotal.add(1256.87);
        wmTotal.add(2965.35);
        wmTotal.add(3785.67);
        wmTotal.add(3333.55);
        wmTotal.add(1234.67);
        wmTotal.add(2765.50);
        wmTotal.add(2345.00);
        wmTotal.add(3245.23);
        wmTotal.add(1345.98);
        wmTotal.add(2536.75);
        wmTotal.add(2897.65);
        wmTotal.add(3176.45);
        this.graphViewHandle(dmTotal, wmTotal, "2017");

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
                    System.out.println("*************************");
                    dbController.getDataType();
                    System.out.println("*************************");
                }catch (FileNotFoundException e){
                    e.printStackTrace();
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
    private void graphViewHandle( List<Double> dmTotal, List<Double> wmTotal, String grapth_title){
        GraphView homeGraphView = (GraphView)findViewById(R.id.home_graph_view);
        homeGraphView.removeAllSeries();

        DataPoint [] dataPointsDeposit = new DataPoint[12];
        for (int i = 1; i <=dmTotal.size(); ++i){
            dataPointsDeposit[i-1] = new DataPoint(i, dmTotal.get(i-1));
        }
        DataPoint [] dataPointsWithdraw = new DataPoint[12];
        for (int i = 1; i <=wmTotal.size(); ++i){
            dataPointsWithdraw[i-1] = new DataPoint(i, wmTotal.get(i-1));
        }
        LineGraphSeries<DataPoint> seriesDeposit = new LineGraphSeries<>(dataPointsDeposit);
        LineGraphSeries<DataPoint> seriesWithdraw = new LineGraphSeries<>(dataPointsWithdraw);
        homeGraphView.getViewport().setXAxisBoundsManual(true);
        homeGraphView.getViewport().setMinX(1);
        homeGraphView.getViewport().setMaxX(12);
        homeGraphView.getGridLabelRenderer().setNumHorizontalLabels(12);
        homeGraphView.setTitle(grapth_title);
        //Add series of deposit
        homeGraphView.addSeries(seriesDeposit);
        seriesDeposit.setColor(Color.GREEN);
        //Add series of withdraw
        homeGraphView.addSeries(seriesWithdraw);
        seriesWithdraw.setColor(Color.RED);
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
        expenseLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeActivity.this, ViewRecordsActivity.class);
                intent.putExtra("data_type", getString(R.string.withdraw));
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
        if (error_msg == ""){
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
}
