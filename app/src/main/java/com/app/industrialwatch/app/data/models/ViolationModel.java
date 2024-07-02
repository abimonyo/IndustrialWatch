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
    List<ImageModel> imagesList;
    @SerializedName("start_time")
    @Expose
    private String startTime;

    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("fine")
    @Expose
    private double fine;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

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

    public List<ImageModel> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<ImageModel> imagesList) {
        this.imagesList = imagesList;
    }

    @Override
    public int getItemType() {
        return ITEM_VIOLATIONS;
    }
    public static class ImageModel {
        @SerializedName("capture_time")
        @Expose
        private String captureTime;

        @SerializedName("image_url")
        @Expose
        private String imageUrl;

        public String getCaptureTime() {
            return captureTime;
        }

        public void setCaptureTime(String captureTime) {
            this.captureTime = captureTime;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
