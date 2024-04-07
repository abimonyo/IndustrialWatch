package com.app.industrialwatch.app.data.models;

import com.app.industrialwatch.app.business.BaseItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RawMaterialModel implements BaseItem {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name; // Or any other property you want to display
    }

    @Override
    public int getItemType() {
        return BaseItem.ITEM_RAW_METERIAL;
    }
}
