package com.example.bartosz.thelocals.Models;

import java.util.Date;
import java.util.List;

public class AttractionList {
    public String Id;
    public String CompanyId;
    public Date CreationDate;
    public String Name;
    //public List<Attraction> Attractions;
    public List<String> Attractions;

    public AttractionList(){
        CreationDate = new Date();
    }
    public AttractionList(String name, String companyId, List<String> attractions){
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
