package com.example.bartosz.thelocals.Models;

import java.util.ArrayList;

public class User {
    public String Id;
    public String Email;
    public String Name;
    public String Surname;
    public String FullName;
    public String SelectedProvince;

    public User(){}
    public User(String id, String email, String name, String surname, String province){
        Id = id;
        SelectedProvince = province;
        Email = email;
        Name = name;
        Surname = surname;
        FullName = name + " " + surname;
    }
}
