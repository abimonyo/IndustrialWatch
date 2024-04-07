package com.app.industrialwatch.app.data.models;

public class GridItemModel {
    private String gridItemName;
    private int gridItemImage;

    public GridItemModel(String gridItemName, int gridItemImage) {
        this.gridItemName = gridItemName;
        this.gridItemImage = gridItemImage;
    }

    public String getGridItemName() {
        return gridItemName;
    }

    public void setGridItemName(String gridItemName) {
        this.gridItemName = gridItemName;
    }

    public int getGridItemImage() {
        return gridItemImage;
    }

    public void setGridItemImage(int gridItemImage) {
        this.gridItemImage = gridItemImage;
    }
}
