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
    // User Table
    private String U_TABLE = "User";
    private String U_ID = "u_id";
    private String U_NAME = "u_name";
    // Budget_Category Table
    private String BC_TABLE ="Budget_Category";
    private String BC_ID = "bc_id";
    private String BC_TYPE = "bc_type";
    // Budget Monthly Table
    private String BM_TABLE = "Budget_Monthly";
    private String BM_MONTH = "bm_month";
    private String BM_YEAR = "bm_year";
    private String BM_BC_ID = "bc_id";
    private String BM_AMOUNT = "bm_amount";
    // Budget_Detail Table
    private String BD_TABLE = "Budget_Detail";
    private String BD_ID = "bd_id";
    private String BD_DATE = "bd_date";
    private String BD_PLACE = "bd_place";
    private String BD_AMOUNT = "bd_amount";
    private String BD_BC_ID = "bc_id";
    // Payment Table
    private String P_TABLE = "Payment";
    private String P_ID = "p_id";
    private String P_TO = "p_to";
    private String P_TOTAL = "p_total";             // total amount paid
    private String P_BALANCE ="p_balance";          // amount are left
    private String P_COMPLETE = "p_complete";
    // Paument_Detail Table
    private String PD_TABLE = "Payment_Detail";
    private String PD_ID = "pd_id";
    private String PD_AMOUNT = "pd_amount";
    private String PD_DATE = "pd_date";
    private String PD_P_ID = "p_id";

    /****************************************************
     *              DATABLE INFORMATION                 *
     ****************************************************/
    public String getDatabaseName(){
        return DATABASE_NAME;
    }

    public int getDatabaseVersion(){
        return DATABASE_VERSION;
    }

    /****************************************************
     *                    USER TABLE                    *
     ****************************************************/
    public String getUserTable(){
        return U_TABLE;
    }

    public String getUserID (){
        return U_ID;
    }

    public String getUserName (){
        return U_NAME;
    }

    /****************************************************
     *            BUDGET CATEGORY TABLE                 *
     ****************************************************/
    public String getBudgetCategoryTable(){
        return BC_TABLE;
    }

    public String getBudgetCategoryID(){
        return BC_ID;
    }

    public String getBudgetCategoryType(){
        return BC_TYPE;
    }

    /****************************************************
     *            BUDGET MONTHLY TABLE                  *
     ****************************************************/
    public String getBudgetMontlyTable(){
        return BM_TABLE;
    }

    public String getBudgetMontlyMonth(){
        return BM_MONTH;
    }

    public String getBudgetMontlyYear(){
        return BM_YEAR;
    }

    public String getBudgetMontlyBCID(){
        return BM_BC_ID;
    }

    public String getBudgetMontlyAmount(){
        return BM_AMOUNT;
    }

    /****************************************************
     *              BUDGET DETAIL TABLE                 *
     ****************************************************/
    public String getBudgetDetailTable() {
        return BD_TABLE;
    }

    public String getBudgetDetailID() {
        return BD_ID;
    }

    public String getBudgetDetailDate() {
        return BD_DATE;
    }

    public String getBudgetDetailPlace() {
        return BD_PLACE;
    }

    public String getBudgetDetailAmount() {
        return BD_AMOUNT;
    }

    public String getBudgetDetailBCID() {
        return BD_BC_ID;
    }

    /****************************************************
     *                  PAYMENT TABLE                   *
     ****************************************************/
    public String getPaymentTable() {
        return P_TABLE;
    }

    public String getPaymentID() {
        return P_ID;
    }

    public String getPaymentTo() {
        return P_TO;
    }

    public String getPaymentTotal() {
        return P_TOTAL;
    }

    public String getPaymentBalance() {
        return P_BALANCE;
    }

    public String getPaymentComplete() {
        return P_COMPLETE;
    }

    /****************************************************
     *              PAYMENT DETAIL TABLE                *
     ****************************************************/
    public String getPaymentDetailTable() {
        return PD_TABLE;
    }

    public String getPaymentDetailID() {
        return PD_ID;
    }

    public String getPaymentDetailAmount() {
        return PD_AMOUNT;
    }

    public String getPaymentDetailDate() {
        return PD_DATE;
    }

    public String getPaymentDetailPID() {
        return PD_P_ID;
    }
}
