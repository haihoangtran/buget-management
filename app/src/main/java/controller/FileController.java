package controller;

/**
 * Created by haitran on 6/20/17.
 */

import android.os.Environment;
import android.os.SystemClock;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import constant.Constant;
import java.io.File;

public class FileController {
    private static Constant constant = new Constant();
    private static String appDBPath = "/data/" + constant.PACKAGE_NAME + "/databases/";
    private static String rootLocalStorage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + constant.APP_NAME;


    public void exportDBToLocalStorage(){
        /*
        Export database file to local storage
         */
        String src_path = appDBPath  + constant.getDatabaseName();
        File srcFile = new File(Environment.getDataDirectory(), src_path);
        File destFile = new File(new File(rootLocalStorage + "/db"), constant.getDatabaseName());
        createFolder(rootLocalStorage + "/db");
        try{
            FileChannel srcChannel = new FileInputStream(srcFile).getChannel();
            FileChannel destChannel = new FileOutputStream(destFile).getChannel();
            destChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            destChannel.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void importDBFromLocalStorage() throws FileNotFoundException {
        /*
        Import database file from local storage
         */
        File srcFile = new File(rootLocalStorage + "/db", constant.getDatabaseName());
        File destFile = new File(Environment.getDataDirectory() + appDBPath, constant.getDatabaseName());
        if (srcFile.exists()) {
            try {
                createFolder(Environment.getDataDirectory() + appDBPath);
                destFile.createNewFile();
                FileChannel srcChanel = new FileInputStream(srcFile).getChannel();
                FileChannel destChannel = new FileOutputStream(destFile).getChannel();
                srcChanel.transferTo(0, srcChanel.size(), destChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
                throw new FileNotFoundException("Cannot found file at '" + rootLocalStorage + "/db/" + constant.getDatabaseName() + "'");
        }
    }

    /*  *******************************************************
                        PRIVATE FUNCTIONS
     ******************************************************* */
    private void createFolder (String folderPath){
        /*
        Create folder if it is not existed
         */
        File folder = new File (folderPath);
        if (!folder.exists()){
            folder.mkdirs();
        }
    }
}
