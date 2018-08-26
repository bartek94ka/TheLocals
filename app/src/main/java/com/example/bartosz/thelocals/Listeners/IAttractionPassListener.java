package com.example.bartosz.thelocals.Listeners;

import com.example.bartosz.thelocals.Models.Attraction;

import java.util.ArrayList;

public interface IAttractionPassListener {
    void PassAttractionList(ArrayList<Attraction> attractions);
    void PassAttractionListToAttractionLists(ArrayList<Attraction> attractions);
}
