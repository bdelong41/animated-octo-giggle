package com.Inventory.inventorytracker.model;

import java.util.List;

public class Box {

    private String owner;
    private List<String> contents;

    private Integer id;
    private Integer boxID;

    public Box(String owner, List<String> contents, Integer id, Integer boxID) {
        this.owner = owner;
        this.contents = contents;
        this.id = id;
        this.boxID = boxID;
    }

    public String getOwner() {
        return owner;
    }

    public List<String> getContents() {
        return contents;
    }

    public Integer getId() {
        return id;
    }

    public Integer getBoxID() {
        return boxID;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBoxID(Integer boxID) {
        this.boxID = boxID;
    }
}
