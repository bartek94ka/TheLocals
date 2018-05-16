package com.example.bartosz.thelocals.Models;

import java.util.Date;
import java.util.List;

public class AttractionList {
    public String Id;
    public Date CreationDate;
    public String Name;
    public List<Attraction> Attractions;

    public AttractionList(){
        CreationDate = new Date();
    }
    public AttractionList(String name, List<Attraction> attractions){
        Name = name;
        Attractions = attractions;
    }
}
