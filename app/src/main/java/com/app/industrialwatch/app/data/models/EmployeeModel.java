package com.app.industrialwatch.app.data.models;

import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.common.utils.AppConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EmployeeModel implements BaseItem, Serializable {
    @SerializedName("employee_id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("image")
    String imageUrl;
    @SerializedName("section_name")
    String sectionName;
    @SerializedName("productivity")
    double productivity;
    @SerializedName("job_role")
    String jobRole;
    @SerializedName("attendance_date")
    @Expose
    String attendanceDate;
    @SerializedName("status")
    @Expose
    String status;

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public double getProductivity() {
        return productivity;
    }

    public void setProductivity(double productivity) {
        this.productivity = productivity;
    }

    @Override
    public int getItemType() {
        return AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM;
    }
}
