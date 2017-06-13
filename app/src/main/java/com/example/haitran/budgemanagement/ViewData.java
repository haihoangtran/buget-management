package com.example.haitran.budgemanagement;

import java.io.Serializable;

/**
 * Created by haitran on 6/4/17.
 */

public class ViewData implements Serializable {
    public String place;
    public String date;
    public String typeData;
    public String amount;
    public ViewData(String place, String date, String typeData, String amount){
        this.place = place;
        this.date = date;
        this.typeData = typeData;
        this.amount = amount;
    }
}