package com.app.industrialwatch.app.data.models;

import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.common.utils.AppConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RulesModel  implements BaseItem {
    @SerializedName("allowed_time")
    @Expose
    private String allowedTime;
    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("fine")
    @Expose
    private Double fine;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
@Expose
    private boolean isChecked;
    public String getAllowedTime() {
        return allowedTime;
    }

    public void setAllowedTime(String allowedTime) {
        this.allowedTime = allowedTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Double getFine() {
        return fine;
    }

    public void setFine(Double fine) {
        this.fine = fine;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getFineAsRS() {
        return "Rs " + (fine != null ? String.valueOf(fine) : "");
    }
    public String getTimeAsTime() {
        return "Time " + (allowedTime != null ? String.valueOf(allowedTime) : "");
    }
    public String getFineAsString() {
        return String.valueOf(fine);
    }
    public void setFineFromString(String stringValue) {
        try {
            fine = Double.parseDouble(stringValue);
        } catch (NumberFormatException e) {
            fine = 0.0; // Default value
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int getItemType() {
        return AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM;
    }
}
