package com.app.industrialwatch.app.data.models;

import android.text.TextUtils;

import com.app.industrialwatch.app.business.BaseItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SupervisorModel implements BaseItem, Serializable {
    @SerializedName("employee_id")
    @Expose
    int id;
    @SerializedName("employee_name")
    @Expose
    String name;
    @SerializedName("username")
    @Expose
    String username;    @SerializedName("password")
    @Expose
    String password;
    @SerializedName("sections")
    @Expose
    List<String> sectionList;

    public SupervisorModel(String name, String username) {
        this.name = name;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getSectionList() {
        return sectionList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSectionList(List<String> sectionList) {
        this.sectionList = sectionList;
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
    public String getSectionListAsString() {
        // Join the elements of sectionList with commas
        return TextUtils.join(", ", sectionList);
    }
}
