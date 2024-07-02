package com.app.industrialwatch.common.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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

    public static JSONArray renameObjectsKeyInAllArray(JSONArray originalArray, String key, int rawMaterialId) {
        JSONArray newArray = new JSONArray();
        try {
            for (int i = 0; i < originalArray.length(); i++) {
                JSONObject originalObj = originalArray.getJSONObject(i);
                String originalKey = originalObj.keys().next();
                JSONArray valueArray = originalObj.getJSONArray(originalKey);
                JSONObject newObj = new JSONObject();
                newObj.put("raw_material_id", originalKey);
                newObj.put(key, valueArray);
                newArray.put(newObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newArray;
    }
    public static List<String> jsonArrayToList(String jsonArray) {
        List<String> list = new ArrayList<>();

        try {
            // Parse the JSON array string
            JSONArray jsonArr = new JSONArray(jsonArray);

            // Convert each item to string and add to the list
            for (int i = 0; i < jsonArr.length(); i++) {
                list.add(jsonArr.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
    public static List<HashMap<String, Object>> jsonArrayToHashMapList(String jsonArray) {
        List<HashMap<String, Object>> list = new ArrayList<>();

        try {
            // Parse the JSON array string
            JSONArray jsonArr = new JSONArray(jsonArray);

            // Iterate over the JSON array
            for (int i = 0; i < jsonArr.length(); i++) {
                // Get the JSON object at the current index
                JSONObject jsonObj = jsonArr.getJSONObject(i);

                // Convert the JSON object to a HashMap
                HashMap<String, Object> map = new HashMap<>();
                int side = jsonObj.getInt("side");
                String defect = jsonObj.getString("defect");

                /*for (Iterator<String> it = jsonObj.keys(); it.hasNext(); ) {
                    String key = it.next();
                    Object value = jsonObj.get(key);

                }*/
                map.put(String.valueOf(side), defect);
                // Add the HashMap to the list
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }
}


