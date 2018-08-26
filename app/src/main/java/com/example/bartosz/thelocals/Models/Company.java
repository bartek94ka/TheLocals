package com.example.bartosz.thelocals.Models;

import java.util.List;

public class Company {
    public String Id;
    public String Name;
    public String Address;
    public String Description;
    public String UrlAddress;
    public String LogoUrl;
    public String PhoneNumber;
    public String Email;
    public List<String> AttractionSuggestedList;
    public Boolean IsValidated;

    public Company(){}
    public Company(String name, String address, String description, String urlAddress, String logoUrl, String phoneNumber, String email, List<String> attractionSuggestedList){
        Name = name;
        Address = address;
        Email = email;
        Description = description;
        UrlAddress = urlAddress;
        LogoUrl = logoUrl;
        PhoneNumber = phoneNumber;
        AttractionSuggestedList = attractionSuggestedList;
        IsValidated = false;
    }
}
