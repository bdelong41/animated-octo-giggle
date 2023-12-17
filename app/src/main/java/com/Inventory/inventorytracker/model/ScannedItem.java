package com.Inventory.inventorytracker.model;

public class ScannedItem {

    private static Integer boxID = 5;

    public ScannedItem() {
        this.boxID = 5;
    }

    public static Integer getBoxID() {
        return boxID;
    }

    public static void setBoxID(Integer boxID) {
        ScannedItem.boxID = boxID;
    }
}
