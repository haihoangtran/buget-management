package controller;

import android.os.Environment;
import constant.Contant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by haitran on 6/10/17.
 */

public class UserController {
    private Contant constant = new Contant();
    private String rootFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + constant.APP_NAME;
    private File folder;
    private File file;

    public UserController(){
        folder = new File(rootFolder);
        file = new File(rootFolder + "/" + constant.USERNAME_TXT_FILE);
    }

    public boolean isExistedUser(){
        /*
        Check that there is an existed user name or not
         */
        if (!file.exists()){
            return false;
        }
        else{
            String userName = this.getUserName();
            if (userName.matches("")){
                return false;
            }
            else{
                return true;
            }
        }
    }

    public void saveUserName(String user_name){
        /*
        Save user name to text file
        Use for adding or edit user name
         */

        // Create folder if it is not existed
        if(!folder.exists()){
            System.out.println("Successfull create folder");
            folder.mkdirs();
        }

        // Add user name to text file
        try{
            FileWriter fw = new FileWriter(file);
            fw.write(user_name);
            fw.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public String getUserName(){
        /*
        Get user name from text file
         */
        String userName = "";
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null){
                userName = line;
            }
            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }
        return userName;
    }

}
