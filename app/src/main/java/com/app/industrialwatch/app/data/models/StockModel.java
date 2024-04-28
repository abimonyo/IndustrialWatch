package com.app.industrialwatch.app.data.models;

import static com.app.industrialwatch.common.utils.AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM;

import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.common.utils.AppConstants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StockModel implements BaseItem {

    @SerializedName("price_per_kg")
    @Expose
    private String price_per_unit;
    @SerializedName("purchased_date")
    @Expose
    private String purchased_date;
    @SerializedName("raw_material_id")
    @Expose
    private int raw_material_id;

    @SerializedName(value = "raw_material_name",alternate = {"name"})
    @Expose
    private String raw_material_name;
    @SerializedName(value = "total_quantity",alternate = {"quantity"})
    @Expose
    private String total_quantity;
    @SerializedName("stock_number")
    @Expose
    private String stock_number;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getStock_number() {
        return stock_number;
    }

    public void setStock_number(String stock_number) {
        this.stock_number = stock_number;
    }

    public String getPrice_per_unit() {
        return price_per_unit;
    }

    public void setPrice_per_unit(String price_per_unit) {
        this.price_per_unit = price_per_unit;
    }

    public String getPurchased_date() {
        return purchased_date;
    }

    public void setPurchased_date(String purchased_date) {
        this.purchased_date = purchased_date;
    }



    public int getRaw_material_id() {
        return raw_material_id;
    }

    public void setRaw_material_id(int raw_material_id) {
        this.raw_material_id = raw_material_id;
    }

    public String getRaw_material_name() {
        return raw_material_name;
    }

    public void setRaw_material_name(String raw_material_name) {
        this.raw_material_name = raw_material_name;
    }

    public String getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    @Override
    public int getItemType() {
        return VIEW_FOR_DETAIL_OR_FOR_ITEM;
    }
}
