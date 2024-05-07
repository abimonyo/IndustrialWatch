package com.app.industrialwatch.app.module.ui.supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.RulesModel;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.data.models.SupervisorModel;
import com.app.industrialwatch.app.module.ui.adapter.ItemCheckBoxAdapter;
import com.app.industrialwatch.app.module.ui.adapter.SectionAdapter;
import com.app.industrialwatch.app.module.ui.admin.section.SectionActivity;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityAddSupervisorBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSupervisorActivity extends BaseActivity implements Callback<ResponseBody>, View.OnClickListener {

    ActivityAddSupervisorBinding binding;
    Dialog dialog;
    Bundle bundle;
    List<BaseItem> sectionList;
    JSONObject responseObject;
    List<Integer> selectedSectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddSupervisorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
        }
        sectionList = new ArrayList<>();
        sectionList.add(new SectionModel(-1, "Select Section"));
        selectedSectionList = new ArrayList<>();
        setPrimaryActionBar(binding.toolbar.primaryToolbar, getString(R.string.edit_supervisor));
        binding.btnAddBatch.setText(getString(R.string.update_supervisor));
        binding.btnAddBatch.setOnClickListener(this);
        //binding.sectionSpinner.setAdapter(new ArrayAdapter<String>());
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(AppConstants.SECTION_URL, getParams("status", 1 + ""), this);
        if (bundle != null)
            doGetRequest(AppConstants.GET_SUPERVISOR_DETAIL, getParams("supervisor_id", bundle.getInt(AppConstants.BUNDLE_KEY) + ""), this);
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.SECTION_URL)) {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    sectionList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SectionModel>>() {
                    }.getType());
                    ItemCheckBoxAdapter adapter = new ItemCheckBoxAdapter(this, sectionList, this);
                    binding.sectionSpinner.setAdapter(adapter);

                } else if (call.request().url().url().toString().contains(AppConstants.GET_SUPERVISOR_DETAIL)) {
                    responseObject = new JSONObject(response.body().string());
                    //binding.etSupervisorName.setText(responseObject.getString("employee_name"));
                    binding.etSupervisorUsername.setText(responseObject.getString("username"));
                    binding.etSupervisorPassword.setText(responseObject.getString("password"));
                    List<BaseItem> sectionList = new Gson().fromJson(responseObject.getJSONArray("sections").toString(), new TypeToken<List<SectionModel>>() {
                    }.getType());
                    checkAndMapRules(sectionList);
                } else if (call.request().url().url().toString().contains(AppConstants.UPDATE_SUPERVISOR)) {
                    JSONObject object = new JSONObject(response.body().string());
                    showToast(object.getString("message"));
                    finish();
                }

            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        } else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        cancelDialog(dialog);
        Log.d("error==>>", t.getMessage());
        showToast("Failed:Try again.");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnAddBatch.getId()) {
            if (selectedSectionList.size() > 0)
                makeApiCall();
            else
                showToast("Please select section.");
        } else {
            String result = (String) v.getTag();
            if (Boolean.parseBoolean(result.split(",")[0])) {
                if (!isSuperVisorExist(Integer.parseInt(result.split(",")[2])))
                    selectedSectionList.add(Integer.parseInt(result.split(",")[2]));
            } else {
                selectedSectionList.remove((Object) Integer.parseInt(result.split(",")[2]));
            }
        }
    }

    private void makeApiCall() {
        try {
            JSONObject object = new JSONObject();
            // object.put("name", getValueFromField(binding.etSupervisorName));
            object.put("password", getValueFromField(binding.etSupervisorPassword));
            object.put("employee_id", bundle.getInt(AppConstants.BUNDLE_KEY) + "");
            object.put("username", getValueFromField(binding.etSupervisorUsername));
            object.put("sections", new JSONArray(selectedSectionList));
            RequestBody body = RequestBody.create(object.toString(), MediaType.get("application/json; charset=utf-8"));
            showProgressDialog(dialog);
            doPostRequest(AppConstants.UPDATE_SUPERVISOR, body, this);
        } catch (JSONException e) {
            Log.d("error==>>", e.getMessage());
        }

    }

    private boolean isSuperVisorExist(int id) {
        Predicate<Integer> predicate = existingModel -> existingModel == id;
        return selectedSectionList.stream().anyMatch(predicate);
    }

    private void checkAndMapRules(List<BaseItem> list) {
        if (sectionList != null) {
            for (int i = 0; i < sectionList.size(); i++) {
                SectionModel section1 = (SectionModel) sectionList.get(i);
                for (int j = 0; j < list.size(); j++) {
                    SectionModel section2 = (SectionModel) list.get(j);
                    if (section1.getSectionName().equals(section2.getSectionName())) {
                        section2.setChecked(true);
                        sectionList.set(i, section2);
                        break;
                    }
                }
            }

        }
    }


}