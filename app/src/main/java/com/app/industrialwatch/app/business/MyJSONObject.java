package com.app.industrialwatch.app.business;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyJSONObject extends JSONObject {

    public MyJSONObject(String json) throws JSONException {
        super(json);
    }

    @NonNull
    @Override
    public String getString(String name) throws JSONException {
        if (has(name) && !get(name).equals(null))
            return super.getString(name);
        else
            return "";
    }

    @NonNull
    @Override
    public JSONObject getJSONObject(String name) throws JSONException {
        if (has(name) && !get(name).equals(null))
            return super.getJSONObject(name);
        else
            return new JSONObject();
    }

    @NonNull
    @Override
    public JSONArray getJSONArray(String name) throws JSONException {
        if (has(name) && !get(name).equals(null))
            return super.getJSONArray(name);
        else
            return new JSONArray();
    }
}
