package controller;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import constant.Constant;
import controller.FileController;

/**
 * Created by haitran on 6/21/17.
 */

public class DBController extends SQLiteOpenHelper {
    private SQLiteDatabase mDataBase;
    private Context context;
    private static DBController sInstance;
    private static Constant constant = new Constant();
    private static FileController fileController = new FileController();

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
    public void onCreate(SQLiteDatabase db){}

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
        String mPath = "data/data/" + constant.PACKAGE_NAME + "/databases/" + constant.getDatabaseName();
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
            this.getDatabaseName();
        }catch (SQLException e){
            throw e;
        }
    }


    // Temporary function
    public void getDataType(){

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<String> arrTblNames = new ArrayList<String>();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                arrTblNames.add( c.getString( c.getColumnIndex("name")) );
                c.moveToNext();
            }
        }
        System.out.println("in get database");
        System.out.println(arrTblNames);

        String selectAll = "Select * from " + "DATA_TYPE";
        int keyID =0;
        String typeName="";
        Double total=0.00;
//        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    keyID = Integer.parseInt(cursor.getString(0));
                    typeName = cursor.getString(1);
                    total = Double.parseDouble(cursor.getString(2));
                    System.out.println("KEY ID: " + keyID);
                    System.out.println("Type Name: " + typeName);
                    System.out.println("Total: " + total);
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d(android.content.ContentValues.TAG, "Error while try to update");
        }
        finally {
            if (cursor != null && !cursor.isClosed()){
                cursor.close();
            }
        }


    }

}
