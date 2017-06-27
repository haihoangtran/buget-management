package model;

/**
 * Created by haitran on 6/26/17.
 */

import constant.Constant;

public class RecordTypeModel {
    private static Constant constant = new Constant();
    private int typeID;
    private String typeName;
    private double typeTotal;

    public RecordTypeModel(int typeID, String typeName, double typeTotal){
        this.typeID = typeID;
        this.typeName = typeName;
        this.typeTotal = typeTotal;
    }

    public int getTypeID() {
        return typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public double getTypeTotal() {
        return typeTotal;
    }


}
