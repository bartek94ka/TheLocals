package com.example.bartosz.thelocals.Models;

import java.util.Date;
import java.util.List;

public class Guide {
    public String Id;
    public Date CreationDate;
    public String FirstName;
    public String LastName;
    public String Email;
    public String City;
    public String PhoneNumber;
    public String AboutMe;
    public String PhotoUrl;
    //public List<String> Attractions;
    public List<String> Languages;
    //public List<String> Specialization;
    public Boolean IsValidated;

    public Guide(){
        CreationDate = new Date();
    }
    public Guide(String firstName, String lastName, String email, String city, String phoneNumber, String aboutMe, String photoUrl, List<String> attractions, List<String> languages, List<String> specialization){
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        City = city;
        PhoneNumber = phoneNumber;
        AboutMe = aboutMe;
        PhotoUrl = photoUrl;
        Languages = languages;
        //Specialization = specialization;
        IsValidated = false;
    }
}
