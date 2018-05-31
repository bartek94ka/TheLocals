package com.example.bartosz.thelocals.Models;

import java.util.ArrayList;

public class User {
    public String Id;
    public String Email;
    public String Name;
    public String Surname;
    public String FullName;

    public User(){}
    public User(String email, String name, String surname){
        Email = email;
        Name = name;
        Surname = surname;
        FullName = name + " " + surname;
    }
}
