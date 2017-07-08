package com.example.haitran.budgetmanagement;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.Locale;
import controller.DBController;
import model.RecordModel;
import constant.Constant;

public class EditDataActivity extends AppCompatActivity {
    private String dataType;
    private RecordModel currentRecord;
    private DBController dbController;
    private EditText dateTf;
    private TextView placeTxt;
    private EditText placeTf;
    private EditText amountTf;
    private RadioButton checkingCb;
    private RadioButton savingCb;
    private LinearLayout checkboxLayout;
    private Button deleteBtn;
    private Button editBtn;
    private Constant constant = new Constant();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setTitle(getString(R.string.edit_title));

        // Create an Back button next to title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Define variables
        this.dataType = getIntent().getStringExtra("data_type");
        this.currentRecord = (RecordModel) getIntent().getSerializableExtra("record");
        this.dbController = DBController.getInstance(EditDataActivity.this);
        this.dateTf = (EditText)findViewById(R.id.edit_date_tf);
        this.placeTxt = (TextView) findViewById(R.id.edit_place_title_txt);
        this.placeTf = (EditText) findViewById(R.id.edit_place_tf);
        this.amountTf = (EditText) findViewById(R.id.edit_amount_tf);
        this.checkingCb = (RadioButton)findViewById(R.id.edit_checking_checkbox);
        this.savingCb = (RadioButton)findViewById(R.id.edit_saving_checkbox);
        this.checkboxLayout = (LinearLayout) findViewById(R.id.edit_checkbox_layout);
        this.deleteBtn = (Button)findViewById(R.id.delete_btn);
        this.editBtn = (Button)findViewById(R.id.edit_btn);

        // Define initial value for variables.
        this.displayCurrentRecord();
        this.checkingCheckboxHandle();
        this.savingCheckboxHandle();
        this.deleteButtonHandle();
        this.editButtonHandle();

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                this.goBackViewRecordActivity();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /* ******************************************************
               PRIVATE FUNCTIONS
    *********************************************************/
    private void displayCurrentRecord(){
        String typeName = currentRecord.getTypeName();
        if (typeName.equals(constant.getRecordTypeExpenseName())){
            checkboxLayout.setVisibility(View.INVISIBLE);
            this.placeTxt.setText(getString(R.string.to));
        }else{
            this.placeTxt.setText(getString(R.string.from));
            checkboxLayout.setVisibility(View.VISIBLE);
            if (typeName.equals(constant.getRecordTypeCheckingName())){
                checkingCb.setChecked(true);
                savingCb.setChecked(false);
            }else{
                checkingCb.setChecked(false);
                savingCb.setChecked(true);
            }
        }
        this.dateTf.setText(currentRecord.getDate());
        this.placeTf.setText(currentRecord.getPlace());
        this.amountTf.setText(String.format(Locale.US, "%.2f", currentRecord.getAmount()));
    }

    private void checkingCheckboxHandle(){
        checkingCb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                checkingCb.setChecked(true);
                savingCb.setChecked(false);
            }
        });
    }

    private void savingCheckboxHandle(){
        savingCb.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                savingCb.setChecked(true);
                checkingCb.setChecked(false);
            }
        });
    }

    private void deleteButtonHandle(){
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                confirmationAlert(false);
            }
        });
    }

    private void editButtonHandle(){
        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                confirmationAlert(true);
            }
        });
    }

    private void confirmationAlert(final boolean is_edit){
        // Define elements
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customView = getLayoutInflater().inflate(R.layout.edit_confirmation_dialog, null);
        TextView confirmaMsg = (TextView) customView.findViewById(R.id.edit_confirmation_alert_msg);
        Button cancelBtn = (Button)customView.findViewById(R.id.edit_confirmation_alert_left_btn);
        Button rightBtn = (Button)customView.findViewById(R.id.edit_confirmation_alert_right_btn);

        // Set values depends on is_edit or not
        if (is_edit){
            confirmaMsg.setText(R.string.edit_confirmation_msg);
            rightBtn.setText(R.string.edit_confirmation_update_btn);
        }else{
            confirmaMsg.setText(R.string.delete_confirmation_msg);
            rightBtn.setText(R.string.delete_btn);
        }

        // Set view
        builder.setView(customView);
        final AlertDialog customDialog = builder.create();

        // Action of button
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                customDialog.dismiss();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                customDialog.dismiss();
                if (is_edit){
                    String typeName = currentRecord.getTypeName();
                    if (!typeName.equals(constant.getRecordTypeExpenseName())){
                        if(checkingCb.isChecked()){
                            typeName = getString(R.string.checking);
                        }else if(savingCb.isChecked()){
                            typeName = getString(R.string.saving);
                        }
                    }
                    RecordModel newRecord = new RecordModel(currentRecord.getRecordID(),
                                                            dateTf.getText().toString(),
                                                            placeTf.getText().toString(),
                                                            Double.parseDouble(amountTf.getText().toString()),
                                                            0,
                                                            typeName);
                    dbController.updateRecord(currentRecord, newRecord);
                }else{
                    dbController.deleteRecordByRecordID(currentRecord);
                }

                // Go back to view Record
                goBackViewRecordActivity();
            }
        });

        // Show dialog
        customDialog.show();
    }

    private void goBackViewRecordActivity(){
        finish();
        Intent intent = new Intent(EditDataActivity.this, ViewRecordsActivity.class);
        intent.putExtra("data_type", dataType);
        startActivity(intent);
    }
}
