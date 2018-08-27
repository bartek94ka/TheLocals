package com.example.bartosz.thelocals.Listeners;

import com.example.bartosz.thelocals.Models.Attraction;
import com.example.bartosz.thelocals.Models.AttractionList;

import java.util.ArrayList;

public interface IAttractionPassListener {
    void PassAttractionList(ArrayList<Attraction> attractions);
    void PassAttractionListToAttractionLists(AttractionList attractionList);
}
