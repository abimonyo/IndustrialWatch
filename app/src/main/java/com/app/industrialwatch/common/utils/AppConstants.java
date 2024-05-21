package com.app.industrialwatch.common.utils;

public class AppConstants {
    /* %%%% Intent Pass %%%% */
    public static String FROM = "from";
    public static String TO = "to";
    public static String BUNDLE_KEY = "key";
    public static String KEY_NUMBER = "number";
    public static String RECORD="record";
    public static String RANKING="ranking";
    public static String LOGIN_KEY="isLogin";

    /* %%%% Response Code %%%%% */
    public static int OK = 200;

    public static int VIEW_FOR_DETAIL_OR_FOR_ITEM = 1;


    /* %%%% Base Url %%%% */
    public static String BASE_URL = "http://192.168.100.10:5000/api/";
    public static String IMAGE_URL="EmployeeImage/";
    public static String VIOLATION_IMAGES="EmployeeViolationImage/";

    /* %%%% LOGIN API %%%% */

    public static String LOGIN_URL = "Employee/Login";

    /* %%%%% SECTION API %%%% */
    public static String SECTION_URL = "Section/GetAllSections";
    public static String GET_SECTION_DETAIL = "Section/GetSectionDetail";
    public static String GET_ALL_RULES_URL = "Section/GetAllRule";
    public static String INSERT_SECTION = "Section/InsertSection";
    public static String UPDATE_SECTION = "Section/UpdateSection";
    public static String CHANGE_SECTION_ACTIVITY_STATUS="Section/ChangeSectionAcitivityStatus";

    /* %%%%%% Employee API %%%%% */
    public static String GET_ALL_SUPERVISOR = "Employee/GetAllSupervisors";
    public static String GET_ALL_ROLES = "Employee/GetAllJobRoles";
    public static String INSERT_EMPLOYEE = "Employee/AddEmployee";
    public static String GET_SUPERVISOR_DETAIL = "Employee/GetSupervisorDetail";
    public static String UPDATE_SUPERVISOR="Employee/UpdateSupervisor";
    public static String GET_ALL_EMPLOYEES="Employee/GetAllEmployees";
    public static String GET_EMPLOYEE_DETAIL="Employee/GetEmployeeDetail";
    public static String GET_EMPLOYEE_ATTENDANCE="Employee/GetEmployeeAttendance";
    public static String GET_ALL_VIOLATIONS="Employee/GetAllViolations";
    public static String GET_EMPLOYEE_SUMMARY="Employee/GetEmployeeSummary";
    public static String GET_EMPLOYEE_PROFILE="Employee/GetEmployeeProfile";
    public static String UPDATE_EMPLOYEE_PROFILE="Employee/UpdateEmployeeProfile";


    /* %%%%% Production API %%%%% */
    public static String GET_ALL_RAW_MATERIAL = "Production/GetAllRawMaterials";
    public static String GET_ALL_INVENTORY = "Production/GetAllInventory";
    public static String GET_INVENTORY_DETAIL_BY_RAW_MATERIAL = "Production/GetStockDetailOfRawMaterial";
    public static String GET_ALL_PRODUCT = "Production/GetAllProducts";
    public static String INSERT_RAW_MATERIAL = "Production/AddRawMaterial";
    public static String INSERT_PRODUCT = "Production/AddProduct";
    public static String INSERT_STOCK = "Production/AddStock";
    public static String GET_LINK_PRODUCTS = "Production/GetLinkedProducts";
    public static String LINK_PRODUCT = "Production/LinkProduct";
    public static String UNLINK_PRODUCT = "Production/GetUnlinkedProducts";
    public static String GET_PRODUCT_FORMULA = "Production/GetFormulaOfProduct";
    public static String GET_ALL_BATCHES = "Production/GetAllBatches";
    public static String GET_BATCHES_DETAILS = "Production/GetBatchDetails";
    public static String CREATE_BATCH = "Production/AddBatch";
    public static String Get_All_Defected_Images = "Production/GetAllDefectedImages";
}
