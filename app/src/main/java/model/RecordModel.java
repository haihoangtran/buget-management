package model;

import java.io.Serializable;

/**
 * Created by haitran on 7/1/17.
 */

public class RecordModel implements Serializable{
    private int recordID;
    private String date;
    private String place;
    private Double amount;
    private int typeID;
    private String typeName;

    public RecordModel(int recordID, String date, String place, Double amount, int typeID, String typeName){
        this.recordID = recordID;
        this.date = date;
        this.place = place;
        this.amount = amount;
        this.typeID = typeID;
        this.typeName = typeName;
    }

    public int getRecordID() {
        return recordID;
    }

    public String getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public Double getAmount() {
        return amount;
    }

    public int getTypeID() {
        return typeID;
    }
    public String getTypeName(){
        return typeName;
    }
}
