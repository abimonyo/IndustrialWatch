package com.app.industrialwatch.common.utils;

import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AppUtils {
    public static boolean validateEmptyEditText(EditText et) {
        return (et.getText().toString().equals("")) ? false : true;
    }
    public static boolean ifNotNullEmpty(String text) {
        return text != null && !text.isEmpty();
    }

    public static List<Map<String, Object>> jsonArrayToList(JSONArray jsonArray) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                Iterator<String> keys = object.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object value = object.get(key);
                    map.put(key, value);
                }
                resultList.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
