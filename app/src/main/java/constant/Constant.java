package constant;

/**
 * Created by haitran on 6/10/17.
 */

public class Constant {
    public String APP_NAME = "Budget Mangement";
    public String USERNAME_TXT_FILE = "user.txt";
    public String PACKAGE_NAME = "com.example.haitran.budgetmanagement";

    // Constant for database
    private String DATABASE_NAME = "bmdb.db";
    private int DATABASE_VERSION = 1;

    public String getDatabaseName(){
        return DATABASE_NAME;
    }

    public int getDatabaseVersion(){
        return DATABASE_VERSION;
    }
}
