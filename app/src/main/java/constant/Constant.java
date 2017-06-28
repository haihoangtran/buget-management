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
    // Table Record Type
    private String RECORD_TYPE_TABLE = "Record_Type";
    private String RECORD_TYPE_TYPE_ID = "type_id";
    private String RECORD_TYPE_TYPE_NAME = "type_name";
    private String RECORD_TYPE_TYPE_TOTAL = "type_total";
    private String RECORD_TYPE_EXPENSE_NAME = "expense";
    private String RECORD_TYPE_CHECKING_NAME  = "checking";
    private String RECORD_TYPE_SAVING_NAME = "saving";
    // Table Monthly Total
    private String MONTHLY_TOTAL_TABLE = "Monthly_Total";
    private String MONTHLY_TOTAL_MONTH = "month";
    private String MONTHLY_TOTAL_YEAR = "year";
    private String MONTHLY_TOTAL_TOTAL = "total";
    private String MONTHLY_TOTAL_TYPE_ID = "type_id";


    public String getDatabaseName(){
        return DATABASE_NAME;
    }

    public int getDatabaseVersion(){
        return DATABASE_VERSION;
    }

    public String getRecordTypeTable(){
        return RECORD_TYPE_TABLE;
    }

    public String getRecordTypeTypeID(){
        return RECORD_TYPE_TYPE_ID;
    }

    public String getRecordTypeTypeName(){
        return RECORD_TYPE_TYPE_NAME;
    }

    public String getRecordTypeTypeTotal(){
        return RECORD_TYPE_TYPE_TOTAL;
    }

    public String getRecordTypeExpenseName() {
        return RECORD_TYPE_EXPENSE_NAME;
    }

    public String getRecordTypeCheckingName() {
        return RECORD_TYPE_CHECKING_NAME;
    }

    public String getRecordTypeSavingName() {
        return RECORD_TYPE_SAVING_NAME;
    }

    public String getMonthlyTotalTable() {
        return MONTHLY_TOTAL_TABLE;
    }

    public String getMonthlyTotalMonth() {
        return MONTHLY_TOTAL_MONTH;
    }

    public String getMonthlyTotalYear() {
        return MONTHLY_TOTAL_YEAR;
    }

    public String getMonthlyTotalTotal() {
        return MONTHLY_TOTAL_TOTAL;
    }

    public String getMonthlyTotalTypeID() {
        return MONTHLY_TOTAL_TYPE_ID;
    }
}
