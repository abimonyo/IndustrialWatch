package com.app.industrialwatch.app.business;

public interface BaseItem {

    int SECTION_DETAILS = 1;
    int SECTION = 2;
    int ITEM_BATCH_NUMBER = 3;
    int ITEM_RAW_METERIAL = 4;
    int ITEM_SUPERVISOR = 5;
    int ITEM_RULE = 6;
    int ITEM_EMPLOYEE_RECORD = 7;
    int ITEM_INVENTORY = 8;
    int ITEM_INVENTORY_DETAIL = 9;
    int ITEM_EMPLOYEE_ATTENDANCE = 10;
    int ITEM_EMPLOYEE_RANKING = 11;
    int ITEM_VIOLATIONS = 12;

    int getItemType();
}
