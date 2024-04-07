package com.app.industrialwatch.app.data.models;

import com.app.industrialwatch.app.business.BaseItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SectionModel implements BaseItem {
    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("name")
    @Expose
    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SectionModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getSectionName() {
        return name;
    }

    public void setSectionName(String name) {
        this.name = name;
    }

    @Override
    public int getItemType() {
        return BaseItem.SECTION;
    }
}
