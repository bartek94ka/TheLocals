package com.example.bartosz.thelocals.Listeners;

public interface IComapnyPassListener {
    void PassComapnyIdToComapnyAttractionSugesstedList(String id);
    void PassAttractionListIdToCompanyAttractionList(String id, String provinceName);
    void PassAttractionListIdToCompanyAttractionListDetails(String id);
    void PassCompanyIdToCompanyDetails(String id);
    void PassCompanyIdToCompanyEdit(String id);
}
