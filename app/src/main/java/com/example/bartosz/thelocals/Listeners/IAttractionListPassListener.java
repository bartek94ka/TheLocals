package com.example.bartosz.thelocals.Listeners;

public interface IAttractionListPassListener {
    void PassAttractionListIdToAttractionListDetails(String attractionListId);
    void PassAttractionListIdToCompanyAttraction(String id, String provinceName);
    void PassGuideIdToGuideTripList(String id);
}
