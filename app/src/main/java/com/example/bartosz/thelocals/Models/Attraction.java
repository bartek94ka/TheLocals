package com.example.bartosz.thelocals.Models;

import java.text.DateFormat;
import java.util.Date;

public class Attraction {

    private Boolean isSelected;
    public String Id;
    public String CreationDate;
    public String Name;
    public String Description;
    public String Province;
    public String PhotoUrl;
    public String SourceUrl;
    public String Longitude;
    public String Latitude;
    public String UserId;
    public Boolean IsValidated;

    public Attraction(){
        //CreationDate = new Date().toString();
        isSelected = false;
    }
    public Attraction(String name, String description, String province, String photoUrl, String sourceUrl, String longitude, String latitude){
        isSelected = false;
        CreationDate = new Date().toString();
        Name = name;
        Description = description;
        Province = province;
        PhotoUrl = photoUrl;
        SourceUrl = sourceUrl;
        Longitude = longitude;
        Latitude = latitude;
        IsValidated = false;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
