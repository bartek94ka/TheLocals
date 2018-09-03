package com.example.bartosz.thelocals.Validators;

import android.content.Context;
import android.widget.Toast;

public class UserDataValidator {
    public Boolean IsDataCorrect(final Context context, String name, String surname){
        if(name.isEmpty()){
            Toast.makeText(context, "Name can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*
        if(surname.isEmpty()){
            Toast.makeText(context, "Surname can not be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        */
        return true;
    }
}
