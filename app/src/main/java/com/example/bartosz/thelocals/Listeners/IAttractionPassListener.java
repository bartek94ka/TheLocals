package com.example.bartosz.thelocals.Listeners;

import com.example.bartosz.thelocals.Models.Attraction;

import java.util.ArrayList;

public interface IAttractionPassListener {
    public void PassAttractionList(ArrayList<Attraction> attractions);
    public void PassAttractionListToAttractionLists(ArrayList<Attraction> attractions);
}
