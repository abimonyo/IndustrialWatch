package com.app.industrialwatch.app.data.models;

import com.app.industrialwatch.app.business.BaseItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BatchModel implements BaseItem {
    String name;
    @SerializedName("batch_number")
    @Expose
    String batch_number;
    @SerializedName("product_number")
    @Expose
    String product_number;

    public BatchModel() {
    }

    public String getBatch_number() {
        return batch_number;
    }

    public void setBatch_number(String batch_number) {
        this.batch_number = batch_number;
    }

    public String getProduct_number() {
        return product_number;
    }

    public void setProduct_number(String product_number) {
        this.product_number = product_number;
    }

    public BatchModel(String name) {
        this.name = name;
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
        return BaseItem.ITEM_BATCH_NUMBER;
    }
}
