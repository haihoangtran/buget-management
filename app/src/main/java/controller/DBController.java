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
import java.util.Collection;
import java.util.Collections;
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
    private String RECORD_TABLE = constant.getRecordTable();
    private String RECORD_RECORD_ID = constant.getRecordRecordID();
    private String RECORD_DATE = constant.getRecordDate();
    private String RECORD_PLACE = constant.getRecordPlace();
    private String RECORD_AMOUNT = constant.getRecordAmount();
    private String RECORD_TYPE_ID = constant.getRecordTypeID();

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
        //Create Record table
        this.createRecordTable(db);

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
                values.put(RECORD_TYPE_TYPE_ID, recordType.getTypeID());
                values.put(RECORD_TYPE_TYPE_NAME, recordType.getTypeName());
                values.put(RECORD_TYPE_TYPE_TOTAL, recordType.getTypeTotal());
                db.insert(RECORD_TYPE_TABLE, null, values);
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
            sql = " Select sum(M." + MONTHLY_TOTAL_TOTAL + ") from " + MONTHLY_TOTAL_TABLE + " as M, " + RECORD_TYPE_TABLE + " as R where M." + MONTHLY_TOTAL_MONTH + " = " + month + " and M." + MONTHLY_TOTAL_YEAR + " = " + year + " and M."
                    + MONTHLY_TOTAL_TYPE_ID + " = R." + RECORD_TYPE_TYPE_ID + " and R." + RECORD_TYPE_TYPE_NAME + " = '" + constant.getRecordTypeExpenseName() + "'";
        }else{
            sql = " Select sum(M." + MONTHLY_TOTAL_TOTAL + ") from " + MONTHLY_TOTAL_TABLE + " as M, " + RECORD_TYPE_TABLE + " as R where M." + MONTHLY_TOTAL_MONTH + " = " + month + " and M." + MONTHLY_TOTAL_YEAR + " = " + year + " and M."
                    + MONTHLY_TOTAL_TYPE_ID + " = R." + RECORD_TYPE_TYPE_ID + " and R." + RECORD_TYPE_TYPE_NAME + " != '" + constant.getRecordTypeExpenseName() + "'";
        }
        Double total = 0.00;
        Cursor cursor = db.rawQuery(sql, null);
        try{
            if (cursor.moveToFirst()) {
                do {
                   total = cursor.getDouble(0);
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

    public List<Double> getMonthlyTotalByYear(int year, boolean is_expense){
        /*
        Get all monthly total for 12 months by year
        @param year
        @param is_expense: True -> type name is expense. Otherwise, balance
         */
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "";
        if(is_expense){
            sql = "select M." + MONTHLY_TOTAL_MONTH + ", sum(M." + MONTHLY_TOTAL_TOTAL + ") from " + MONTHLY_TOTAL_TABLE + " as M, " + RECORD_TYPE_TABLE + " as R where " + MONTHLY_TOTAL_YEAR + " = " + year
                    + " and M." + MONTHLY_TOTAL_TYPE_ID + " = R." + RECORD_TYPE_TYPE_ID + " and R." + RECORD_TYPE_TYPE_NAME + " = '" + constant.getRecordTypeExpenseName() + "' group by M." + MONTHLY_TOTAL_MONTH;
        }else{
            sql = "select M." + MONTHLY_TOTAL_MONTH + ", sum(M." + MONTHLY_TOTAL_TOTAL + ") from " + MONTHLY_TOTAL_TABLE + " as M, " + RECORD_TYPE_TABLE + " as R where " + MONTHLY_TOTAL_YEAR + " = " + year
                    + " and M." + MONTHLY_TOTAL_TYPE_ID + " = R." + RECORD_TYPE_TYPE_ID + " and R." + RECORD_TYPE_TYPE_NAME + " != '" + constant.getRecordTypeExpenseName() + "' group by M." + MONTHLY_TOTAL_MONTH;
        }
        List<Double> totals = new ArrayList<Double>(Collections.nCopies(12, 0.00));
        Cursor cursor = db.rawQuery(sql, null);
        try{
            if (cursor.moveToFirst()) {
                do {
                    totals.set(cursor.getInt(0) - 1, cursor.getDouble(1));
                } while (cursor.moveToNext());
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return totals;
    }

    public void addRecord(String date, String place, Double amount, String recordType){
        /*
        Add record to Record table with 3 steps:
            1/ Add new record to Record Table
            2/ Update or add total in Monthly_Total table
            3/ Update total in Record_Type Table
         */

        int typeID = this.getTypeIDFromRecordType(recordType.toLowerCase());
        // Add record to Record Table
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(RECORD_DATE, date);
            values.put(RECORD_PLACE, place);
            values.put(RECORD_AMOUNT, amount);
            values.put(RECORD_TYPE_ID, typeID);
            db.insert(RECORD_TABLE, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
            db.close();
        }
        // Update or add total in Monthly_Total table
        String [] dateParts = date.split("/");
        this.updateOrAddMonthlyTotal(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[2]), amount, typeID);
        // Update Record Type
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

    private void createRecordTable(SQLiteDatabase db){
        String sql = "Create table " + RECORD_TABLE + "(" + RECORD_RECORD_ID + " integer primary key AUTOINCREMENT , " + RECORD_DATE + " text not null, " + RECORD_PLACE + " text, "
                     + RECORD_AMOUNT + " real, " + RECORD_TYPE_ID + " integer, foreign key(" + RECORD_TYPE_ID + ") references " + RECORD_TYPE_TABLE + "(" + RECORD_TYPE_TYPE_ID + "))";
        db.execSQL(sql);
    }

    private int getTypeIDFromRecordType(String typeName){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "Select " + RECORD_TYPE_TYPE_ID + " from " + RECORD_TYPE_TABLE + " where " + RECORD_TYPE_TYPE_NAME + " = '" + typeName + "'";
        int typeID = 0;
        Cursor cursor = db.rawQuery(sql, null);
        try{
            if (cursor.moveToFirst()) {
                do {
                    typeID = cursor.getInt(0);
                } while (cursor.moveToNext());
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return typeID;
    }

    private void updateOrAddMonthlyTotal(int month, int year, double amount, int typeID){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "Select * from " + MONTHLY_TOTAL_TABLE + " where " + MONTHLY_TOTAL_MONTH + " = " + month  + " and " + MONTHLY_TOTAL_YEAR + " = " + year + " and " + MONTHLY_TOTAL_TYPE_ID + " = " + typeID;
        Cursor cursor = db.rawQuery(sql, null);
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (cursor.getCount() == 1){
            String whereClause = MONTHLY_TOTAL_MONTH + " = " + month + " and " + MONTHLY_TOTAL_YEAR + " = " + year + " and " + MONTHLY_TOTAL_TYPE_ID + " = " + typeID;
            cursor.moveToFirst();
            values.put(MONTHLY_TOTAL_MONTH, cursor.getInt(0));
            values.put(MONTHLY_TOTAL_YEAR, cursor.getInt(1));
            values.put(MONTHLY_TOTAL_TOTAL, cursor.getDouble(2) + amount);
            values.put(MONTHLY_TOTAL_TYPE_ID, cursor.getInt(3));
            db.update(MONTHLY_TOTAL_TABLE, values, whereClause, null);
        }else if (cursor.getCount() == 0){
            db.beginTransaction();
            try{
                values.put(MONTHLY_TOTAL_MONTH, month);
                values.put(MONTHLY_TOTAL_YEAR, year);
                values.put(MONTHLY_TOTAL_TOTAL, amount);
                values.put(MONTHLY_TOTAL_TYPE_ID, typeID);
                db.insert(MONTHLY_TOTAL_TABLE, null, values);
                db.setTransactionSuccessful();
            }catch(Exception e){
                e.printStackTrace();
            }
            finally{
                db.endTransaction();
                db.close();
            }
        }else{
            try {
                throw new Exception ("There are 2 or more records from Monthly Total");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public void updateDataType(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(TYPE_TOTAL, 100.26);
//        values.put(TYPE_NAME, "Testing");
//        int row = db.update(TABLE_DATA_TYPE, values, KEY_ID + " = ?", new String[] {String.valueOf(2)});
//        System.out.println("UPDATE SUCCESS: " + row);
//    }

}
