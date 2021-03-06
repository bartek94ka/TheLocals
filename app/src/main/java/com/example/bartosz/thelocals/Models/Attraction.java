package com.example.bartosz.thelocals.Models;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.util.Date;

public class Attraction implements Comparable<Attraction>{

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
    public Integer VisitsCounter;
    public Integer AttracionListOrder;

    public Attraction(){
        VisitsCounter = 0;
        isSelected = false;
    }
    public Attraction(String name, String description, String province, String photoUrl, String sourceUrl, String longitude, String latitude){
        VisitsCounter = 0;
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

    @Override
    public int compareTo(@NonNull Attraction attraction) {
        if(attraction.AttracionListOrder != null && this.AttracionListOrder != null){
            int compareAttracionListOder = attraction.AttracionListOrder;
            return this.AttracionListOrder - compareAttracionListOder;
        }
        return 0;
    }
}
