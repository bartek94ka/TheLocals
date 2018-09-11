package com.example.bartosz.thelocals.Listeners;

public interface IGuidePassListener {
    void PassAttractionListIdToGuideTripDetails(String id);
    void PassAttractionListIdToGuideTripList(String id);
    void PassGuideIdToGuideDetails(String id);
}
