package com.example.haitran.budgemanagement;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddDataActivity extends AppCompatActivity {
    private String dataType;
    private BottomNavigationView navigation;
    private EditText dateTf;
    private EditText placeTf;
    private EditText amountTf;
    private TextView placeTxt;
    private LinearLayout depositTypeLayout;
    private RadioButton checkingCb;
    private RadioButton savingCb;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Create an Back button next to title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get value of Intent parameter
        this.dataType = getIntent().getStringExtra("data_type");

        // Define class variables
        this.dateTf = (EditText)findViewById(R.id.date_tf);
        this.placeTf = (EditText)findViewById(R.id.place_tf);
        this.amountTf = (EditText)findViewById(R.id.amount_tf);
        this.placeTxt = (TextView) findViewById(R.id.place_title_txt);
        this.depositTypeLayout = (LinearLayout)findViewById(R.id.checkbox_layout);
        this.checkingCb = (RadioButton)findViewById(R.id.checking_checkbox);
        this.savingCb = (RadioButton)findViewById(R.id.saving_checkbox);
        this.addBtn = (Button)findViewById(R.id.add_btn);

        // Define value for variable
        this.dateHandle();
        this.checkingCheckboxHandle();
        this.savingCheckboxHandle();
        this.addButtonHandle();

        //----------    Handle content and bottom navigation bar
        this.navigation = (BottomNavigationView) findViewById(R.id.add_data_navigation);
        if (dataType.equals(getString(R.string.deposit))){
            navigation.setSelectedItemId(R.id.navigation_deposit);
            this.depositViewHandle();
        }
        else{
            navigation.setSelectedItemId(R.id.navigation_withdraw);
            this.withdrawViewHandle();
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /* ******************************************************
               PRIVATE FUNCTIONS
    *********************************************************/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_deposit:
                    depositViewHandle();
                    return true;

                case R.id.navigation_withdraw:
                    withdrawViewHandle();
                    return true;
            }
            return false;
        }
    };

    private void withdrawViewHandle(){
        // Set title of activity
        setTitle(getString(R.string.withdraw));
        this.placeTxt.setText(getString(R.string.to));
        this.resetActivity();
        this.dataType = getString(R.string.withdraw);
        this.depositTypeLayout.setVisibility(View.INVISIBLE);
    }

    private void depositViewHandle(){
        // Set title of activity
        setTitle(getString(R.string.deposit));
        this.placeTxt.setText(getString(R.string.from));
        this.resetActivity();
        this.dataType = getString(R.string.deposit);
        this.depositTypeLayout.setVisibility(View.VISIBLE);

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

    private void addButtonHandle(){
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final String dateValue = dateTf.getText().toString();
                final String placeValue = placeTf.getText().toString();
                final String amountValue = amountTf.getText().toString();

                // Dismiss keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                            InputMethodManager.HIDE_NOT_ALWAYS);

                if ((dateValue.matches("")) || (placeValue.matches("")) || (amountValue.matches(""))){
                    emptyValueAlert();
                }
                else{
                    Map <String, String> data = new HashMap<String, String>();
                    data.put("date", dateValue);
                    data.put("place", placeValue);
                    data.put("amount", amountValue);
                    confirmationAlert(data);
                }
            }
        });
    }

    private void emptyValueAlert(){

        // Define and initialize elements
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View custom_view = getLayoutInflater().inflate(R.layout.add_data_empty_dialog, null);
        Button okBtn = (Button) custom_view.findViewById(R.id.add_data_empty_alert_ok_btn);

        // Set View
        builder.setView(custom_view);
        final AlertDialog customDialog = builder.create();
        // Action of Buttons
        okBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                customDialog.dismiss();

            }
        });
        //Show dialog
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.show();
    }

    private void confirmationAlert(Map <String, String> data){
        // Define and initialize elements
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View custom_view = getLayoutInflater().inflate(R.layout.add_data_confirmation_dialog, null);
        TextView placeTitle = (TextView)custom_view.findViewById(R.id.confirm_alert_place_title);
        TextView dateTxt = (TextView)custom_view.findViewById(R.id.confirm_alert_date_value);
        TextView placeTxt = (TextView)custom_view.findViewById(R.id.confirm_alert_place_value);
        TextView amountTxt = (TextView)custom_view.findViewById(R.id.confirm_alert_amount_value);
        TextView typeTxt = (TextView)custom_view.findViewById(R.id.confirm_alert_type_value);
        Button noBtn = (Button)custom_view.findViewById(R.id.add_data_confirmation_alert_no_btn);
        Button yesBtn = (Button)custom_view.findViewById(R.id.add_data_confirmation_alert_yes_btn);

        // Set value for some text element
        dateTxt.setText(data.get("date"));
        amountTxt.setText(data.get("amount"));
        placeTxt.setText(data.get("place"));
        if (this.dataType == getString(R.string.deposit)){
            placeTitle.setText(getString(R.string.from));
            if (this.checkingCb.isChecked()){
                typeTxt.setText(getString(R.string.checking));
            }
            else{
                typeTxt.setText(getString(R.string.saving));
            }
        }
        else{
            placeTitle.setText(getString(R.string.to));
            LinearLayout typeLayout = (LinearLayout)custom_view.findViewById(R.id.confirm_alert_type_layout);
            typeLayout.setVisibility(View.INVISIBLE);
        }

        // Set View
        builder.setView(custom_view);
        final AlertDialog customDialog = builder.create();

        //Action of buttons
        noBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                customDialog.dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast toast_msg = Toast.makeText(getApplicationContext(),
                                                 getString(R.string.add_data_successfully_toast_msg),
                                                 Toast.LENGTH_SHORT);
                toast_msg.setGravity(Gravity.CENTER, 0, 0);
                toast_msg.show();
                customDialog.dismiss();

                // Reset activity
                resetActivity();
            }
        });

        //Show dialog
        customDialog.show();
    }

    private void dateHandle(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateTf.setText(dateFormat.format(new Date()));
    }

    private void resetActivity(){
        // Reset all values to default value
        this.dateHandle();
        this.placeTf.setText("");
        this.amountTf.setText("");
        this.checkingCb.setChecked(true);
        this.savingCb.setChecked(false);
    }

}
