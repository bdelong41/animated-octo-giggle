package com.Inventory.inventorytracker.model;

public class ScannedItem {

    private static Integer boxID;

    public ScannedItem(Integer boxID) {
        this.boxID = boxID;
    }

    public static Integer getBoxID() {
        return boxID;
    }

    public static void setBoxID(Integer boxID) {
        ScannedItem.boxID = boxID;
    }
}
