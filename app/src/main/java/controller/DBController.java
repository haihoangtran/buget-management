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
        // Creat Record Type table
        this.createRecordTypeTable(db);
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

    public String getCurrentBalance(){
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
        return String.format("%.2f", currentBalance);
    }

    /* ###################################################################
                            PRIVATE  FUNCTIONS
     ###################################################################*/

    private void createRecordTypeTable(SQLiteDatabase db){
        String sql = "Create table if not exists " + RECORD_TYPE_TABLE +" (" + RECORD_TYPE_TYPE_ID + " integer, " + RECORD_TYPE_TYPE_NAME + " text, " + RECORD_TYPE_TYPE_TOTAL
                     + " real, primary key(" + RECORD_TYPE_TYPE_ID + "))";
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
