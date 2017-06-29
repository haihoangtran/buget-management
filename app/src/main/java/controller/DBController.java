package controller;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import constant.Constant;
import controller.FileController;

import model.RecordTypeModel;

/**
 * Created by haitran on 6/21/17.
 */

public class DBController extends SQLiteOpenHelper {
    private SQLiteDatabase mDataBase;
    private Context context;
    private static DBController sInstance;
    private static Constant constant = new Constant();
    private static FileController fileController = new FileController();


    // Define some constant variables
    private String DB_NAME = constant.getDatabaseName();
    private int DB_VERSION = constant.getDatabaseVersion();
    private String RECORD_TYPE_TABLE = constant.getRecordTypeTable();
    private String RECORD_TYPE_TYPE_ID = constant.getRecordTypeTypeID();
    private String RECORD_TYPE_TYPE_NAME = constant.getRecordTypeTypeName();
    private String RECORD_TYPE_TYPE_TOTAL = constant.getRecordTypeTypeTotal();
    private String MONTHLY_TOTAL_TABLE = constant.getMonthlyTotalTable();
    private String MONTHLY_TOTAL_MONTH = constant.getMonthlyTotalMonth();
    private String MONTHLY_TOTAL_YEAR = constant.getMonthlyTotalYear();
    private String MONTHLY_TOTAL_TOTAL = constant.getMonthlyTotalTotal();
    private String MONTHLY_TOTAL_TYPE_ID = constant.getMonthlyTotalTypeID();

    public static synchronized DBController getInstance(Context context){
        if(sInstance == null){
            sInstance = new DBController(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBController(Context context){
        super(context, constant.getDatabaseName(), null, constant.getDatabaseVersion());
        this.context =context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // Create Record Type table
        this.createRecordTypeTable(db);
        // Create Monthly Total table
        this.createMonthlyTotalTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    public void openDataBase() throws SQLException {
        String mPath = "data/data/" + constant.PACKAGE_NAME + "/databases/" + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void importDB(String from) throws FileNotFoundException {
        /*
        Import database file from another source
        @param from: database from
         */
        switch (from){
            case "local":
                try{
                    fileController.importDBFromLocalStorage();
                }catch(FileNotFoundException e){
                    throw e;
                }
                break;
        }
        try{
            this.openDataBase();
            this.getReadableDatabase();
        }catch (SQLException e){
            throw e;
        }
    }

    public void addDefaultRecordType(){
        SQLiteDatabase db = this.getWritableDatabase();
        List <RecordTypeModel> recordTypesList= new ArrayList<>();
        recordTypesList.add(new RecordTypeModel(1, constant.getRecordTypeCheckingName(), 0.00));
        recordTypesList.add(new RecordTypeModel(2, constant.getRecordTypeSavingName(), 0.00));
        recordTypesList.add(new RecordTypeModel(3, constant.getRecordTypeExpenseName(), 0.00));
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (RecordTypeModel recordType : recordTypesList) {
                values.put(constant.getRecordTypeTypeID(), recordType.getTypeID());
                values.put(constant.getRecordTypeTypeName(), recordType.getTypeName());
                values.put(constant.getRecordTypeTypeTotal(), recordType.getTypeTotal());
                db.insert(constant.getRecordTypeTable(), null, values);
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }

    }

    public Double getCurrentBalance(){
        /*
        Get current balance from Record Type table by sum of checking and saving
         */
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "Select * from " + constant.getRecordTypeTable();
        Double currentBalance = 0.00;
        Cursor cursor = db.rawQuery(sql, null);
        try{
            if (cursor.moveToFirst()) {
                do {
                    if (!cursor.getString(1).matches(constant.getRecordTypeExpenseName())){
                        currentBalance += cursor.getDouble(2);
                    }
                } while (cursor.moveToNext());
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return currentBalance;
    }

    public Double getMonthlyTotal(int month, int year, boolean is_expense){
        /*
        Get monthly total of record type by month and year
        @param month
        @param year
        @param is_expense: True -> type name is expense. Otherwise, balance
         */

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "";
        if(is_expense) {
            sql = " Select M." + MONTHLY_TOTAL_TOTAL + " from " + MONTHLY_TOTAL_TABLE + " as M, " + RECORD_TYPE_TABLE + " as R where M." + MONTHLY_TOTAL_MONTH + " = " + month + " and M." + MONTHLY_TOTAL_YEAR + " = " + year + " and M."
                    + MONTHLY_TOTAL_TYPE_ID + " = R." + RECORD_TYPE_TYPE_ID + " and R." + RECORD_TYPE_TYPE_NAME + " = '" + constant.getRecordTypeExpenseName() + "'";
        }else{
            sql = " Select M." + MONTHLY_TOTAL_TOTAL + " from " + MONTHLY_TOTAL_TABLE + " as M, " + RECORD_TYPE_TABLE + " as R where M." + MONTHLY_TOTAL_MONTH + " = " + month + " and M." + MONTHLY_TOTAL_YEAR + " = " + year + " and M."
                    + MONTHLY_TOTAL_TYPE_ID + " = R." + RECORD_TYPE_TYPE_ID + " and R." + RECORD_TYPE_TYPE_NAME + " != '" + constant.getRecordTypeExpenseName() + "'";
        }
        Double total = 0.00;
        Cursor cursor = db.rawQuery(sql, null);
        try{
            if (cursor.moveToFirst()) {
                do {
                    total += cursor.getDouble(0);
                } while (cursor.moveToNext());
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return total;
     }

    /* ###################################################################
                            PRIVATE  FUNCTIONS
     ###################################################################*/

    private void createRecordTypeTable(SQLiteDatabase db){
        String sql = "Create table if not exists " + RECORD_TYPE_TABLE +" (" + RECORD_TYPE_TYPE_ID + " integer, " + RECORD_TYPE_TYPE_NAME + " text, " + RECORD_TYPE_TYPE_TOTAL
                     + " real, primary key(" + RECORD_TYPE_TYPE_ID + "))";
        db.execSQL(sql);
    }

    private void createMonthlyTotalTable(SQLiteDatabase db){
        String sql = "Create table if not exists " + MONTHLY_TOTAL_TABLE + " (" + MONTHLY_TOTAL_MONTH + " integer, " + MONTHLY_TOTAL_YEAR + " integer, " + MONTHLY_TOTAL_TOTAL
                     + " real, " + MONTHLY_TOTAL_TYPE_ID + " integer, primary key(" + MONTHLY_TOTAL_MONTH + ", " + MONTHLY_TOTAL_YEAR + ", " + MONTHLY_TOTAL_TYPE_ID +"), foreign key(" + MONTHLY_TOTAL_TYPE_ID
                + ") references " + RECORD_TYPE_TABLE + "(" + RECORD_TYPE_TYPE_ID + "))";
        db.execSQL(sql);
    }

//
//    // Temporary function
//    public void getDataType(){
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        ArrayList<String> arrTblNames = new ArrayList<String>();
//        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//        if (c.moveToFirst()) {
//            while ( !c.isAfterLast() ) {
//                arrTblNames.add( c.getString( c.getColumnIndex("name")) );
//                c.moveToNext();
//            }
//        }
//        System.out.println("in get database");
//        System.out.println(arrTblNames);
//
//        String selectAll = "Select * from " + "DATA_TYPE";
//        int keyID =0;
//        String typeName="";
//        Double total=0.00;
////        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectAll, null);
//        try {
//            if (cursor.moveToFirst()) {
//                do {
//                    keyID = Integer.parseInt(cursor.getString(0));
//                    typeName = cursor.getString(1);
//                    total = Double.parseDouble(cursor.getString(2));
//                    System.out.println("KEY ID: " + keyID);
//                    System.out.println("Type Name: " + typeName);
//                    System.out.println("Total: " + total);
//                } while (cursor.moveToNext());
//            }
//        }catch (Exception e){
//            Log.d(android.content.ContentValues.TAG, "Error while try to update");
//        }
//        finally {
//            if (cursor != null && !cursor.isClosed()){
//                cursor.close();
//            }
//        }
//
//
//    }

}
