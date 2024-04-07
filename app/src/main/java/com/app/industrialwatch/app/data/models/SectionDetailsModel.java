package com.app.industrialwatch.app.data.models;

import com.app.industrialwatch.app.business.BaseItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SectionDetailsModel implements BaseItem {

    @SerializedName("id")
    @Expose
    int id;
    @SerializedName("data")
    @Expose
    List<RulesModel> model;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<RulesModel> getModel() {
        return model;
    }

    public void setModel(List<RulesModel> model) {
        this.model = model;
    }

    @Override
    public int getItemType() {
        return BaseItem.SECTION_DETAILS;
    }
}
