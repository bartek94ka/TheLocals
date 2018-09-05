package com.example.bartosz.thelocals.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttractionList {
    public String Id;



    public String CompanyId;
    public Date CreationDate;
    public String Name;
    public String Province;
    public String Description;
    public String Duration;
    public String AdditionalInfo;
    public String UserId;
    public HashMap<String,Attraction> Attractions;

    public AttractionList(){
        CreationDate = new Date();
    }
    public AttractionList(String name, String companyId, HashMap<String,Attraction> attractions){
        CreationDate = new Date();
        CompanyId = companyId;
        Name = name;
        Attractions = attractions;
    }
    /*
    public AttractionList(String name, String companyId, List<Attraction> attractions){
        CreationDate = new Date();
        CompanyId = companyId;
        Name = name;
        Attractions = attractions;
    }
    */
}
