package com.app.industrialwatch.app.data.models;

import com.app.industrialwatch.app.business.BaseItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ViolationModel implements BaseItem, Serializable {
    @SerializedName("violation_id")
            @Expose
    int violationId;
    @SerializedName("rule_name")
    @Expose
    String ruleName;
    @SerializedName("date")
    @Expose
    String date;
    @SerializedName("time")
    @Expose
    String time;
    @SerializedName("images")
    @Expose
    List<String> imagesList;

    public int getViolationId() {
        return violationId;
    }

    public void setViolationId(int violationId) {
        this.violationId = violationId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    @Override
    public int getItemType() {
        return ITEM_VIOLATIONS;
    }
}
