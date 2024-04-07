package com.app.industrialwatch.common.utils;

import android.widget.EditText;

public class AppUtils {
    public static boolean validateEmptyEditText(EditText et) {
        return (et.getText().toString().equals("")) ? false : true;
    }
}
