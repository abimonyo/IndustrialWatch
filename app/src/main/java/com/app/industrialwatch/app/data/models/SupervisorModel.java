package com.app.industrialwatch.app.data.models;

import com.app.industrialwatch.app.business.BaseItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupervisorModel implements BaseItem {
    @SerializedName("name")
            @Expose
    String name;
    @SerializedName("username")
            @Expose
    String username;

    public SupervisorModel(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int getItemType() {
        return BaseItem.ITEM_SUPERVISOR;
    }
}
