package com.app.industrialwatch.common.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtil {
    public static JSONObject convertStringListToJSONObject(ArrayList<String> stringList, String key) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray(stringList);
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject.put(key, jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONArray renameObjectsKeyInAllArray(JSONArray originalArray,String key) {
        JSONArray newArray = new JSONArray();
        try {
            for (int i = 0; i < originalArray.length(); i++) {
                JSONObject originalObj = originalArray.getJSONObject(i);
                String originalKey = originalObj.keys().next();
                JSONArray valueArray = originalObj.getJSONArray(originalKey);
                JSONObject newObj = new JSONObject();
                newObj.put(key, valueArray);
                newArray.put(newObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newArray;
    }
}


