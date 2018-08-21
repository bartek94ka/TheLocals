package com.example.bartosz.thelocals.Models;

import java.text.DateFormat;
import java.util.Date;

public class Attraction {
    public String Id;
    public String CreationDate;
    public String Name;
    public String Description;
    public String PhotoUrl;
    public String SourceUrl;
    public String Longitude;
    public String Latitude;
    public Boolean IsValidated;

    public Attraction(){
        CreationDate = new Date().toString();
    }
    public Attraction(String name, String description, String photoUrl, String sourceUrl, String longitude, String latitude){
        CreationDate = new Date().toString();
        Name = name;
        Description = description;
        PhotoUrl = photoUrl;
        SourceUrl = sourceUrl;
        Longitude = longitude;
        Latitude = latitude;
        IsValidated = false;
    }
}
