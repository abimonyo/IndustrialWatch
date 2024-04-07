package com.app.industrialwatch.common.utils;

public class AppConstants {
     /* %%%% Intent Pass %%%% */
    public static String FROM="from";
    public static String TO="to";
    public static String BUNDLE_KEY="key";

    /* %%%% Response Code %%%%% */
    public static int OK=200;

    public static int VIEW_FOR_DETAIL_OR_FOR_ITEM=1;


    /* %%%% Base Url %%%% */
    public static String BASE_URL="http://192.168.43.211:5000/api/";

    /* %%%% LOGIN API %%%% */

    public static String LOGIN_URL="User/Login";

    /* %%%%% SECTION API %%%% */
    public static String SECTION_URL="Section/GetAllSections";
    public static String GET_RULES_FOR_SECTION_URL="Section/GetRulesForSection";
    public static String GET_ALL_RULES_URL="Rule/GetAllRule";
    public static String INSERT_SECTION="Section/InsertSection";

    /* %%%%%% SUPERVISOR API %%%%% */
    public static String GET_ALL_SUPERVISOR="Supervisor/GetAllSupervisors";

    /* %%%%% Production API %%%%% */
    public static String GET_ALL_RAW_MATERIAL="Production/GetAllRawMaterials";
    public static String GET_ALL_INVENTORY="Production/GetAllInventory";
    public static String GET_INVENTORY_DETAIL_BY_RAW_MATERIAL="Production/GetStockDetailOfRawMaterial";
    public static String GET_ALL_PRODUCT="Production/GetAllProducts";
    public static String INSERT_RAW_MATERIAL="Production/AddRawMaterial";
    public static String INSERT_PRODUCT="Production/AddProduct";
    public static String INSERT_STOCK="Production/AddStock";
}
