package com.Inventory.inventorytracker.model;

import java.util.List;

public class Box {

    private String owner;
    private static List<String> contents;

    private Integer id;
    private Integer boxID;
    private String description;

    public Box(String owner, List<String> contents, Integer id, Integer boxID, String description) {
        this.owner = owner;
        this.contents = contents;
        this.id = id;
        this.boxID = boxID;
        this.description = description;
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

    public String getDescription() {
        return description;
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

    public void addItem(String val){
        contents.add(val);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString(){
        String val = "";
        for(int index = 0; index < contents.size(); index++){
            val += contents.get(index);
            if(index >= 3) break;
        }
        return ("" + owner + " " + val);
    }
}
