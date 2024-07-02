package com.app.industrialwatch.app.module.ui.supervisor;

import static com.app.industrialwatch.common.utils.AppConstants.DEFECT_MONITORING;
import static com.app.industrialwatch.common.utils.AppConstants.PREDICT_EMPLOYEE_VIOLATION;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.app.module.ui.employee.AddEmployeeActivity;
import com.app.industrialwatch.common.base.TakePhotoActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.FileUtils;
import com.app.industrialwatch.databinding.ActivityMonitoringEmployeeBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonitoringEmployeeActivity extends TakePhotoActivity implements View.OnClickListener, Callback<ResponseBody>, AdapterView.OnItemSelectedListener {

    ActivityMonitoringEmployeeBinding binding;
    Dialog dialog;
    List<BaseItem> sectionModelList;
    int selectedSectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMonitoringEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, "Monitoring Employee");
        binding.btnStartMonitoring.setOnClickListener(this);
        sectionModelList = new ArrayList<>();
        sectionModelList.add(new SectionModel(-1, "Select Section"));
        setSpinnerAdapter(sectionModelList, binding.spSection, this);
        initializeImagePicker();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnStartMonitoring.getId()) {
            showChooseVideoDialog();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sectionModelList.size() > 1) {
            sectionModelList.clear();

        }
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(AppConstants.GET_SPECIAL_SECTION_FOR_EMPLOYEE, getParams("employee_id", SharedPreferenceManager.getInstance().read("id", 0) + ""), this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            selectedPhotoUri = data.getData();
            File file = new File(Objects.requireNonNull(FileUtils.getPath(MonitoringEmployeeActivity.this, selectedPhotoUri)));
            if (file.exists()) {
                makePredictionApiCall(file);
            }
        }

    }


    private void makePredictionApiCall(File file) {
        List<MultipartBody.Part> videoPart = new ArrayList<>();
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
        videoPart.add(body);
        Map<String, RequestBody> bodyMap = new HashMap<>();
        RequestBody sectionBody = RequestBody.create(String.valueOf(selectedSectionId), MediaType.parse("text/plain"));
        bodyMap.put("section_id", sectionBody);

        //showProgressDialog(dialog);
        doPostRequestWithMapBody(PREDICT_EMPLOYEE_VIOLATION, bodyMap, videoPart, this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_SPECIAL_SECTION_FOR_EMPLOYEE)) {
                    JSONArray array = new JSONArray(response.body().string());
                    Log.d("section", array.length() + "");
                    //sectionModelList.removeIf()
                    sectionModelList.addAll(new Gson().fromJson(array.toString(), new TypeToken<List<SectionModel>>() {
                    }.getType()));

                } else {
                    JSONObject object = new JSONObject(response.body().string());
                    binding.tvEmployeeName.setVisibility(View.VISIBLE);
                    binding.tvEmployeeName.setText("Name :" + object.getString("employee_name"));
                    JSONArray array = object.getJSONArray("rules");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);
                        String ruleName = item.getString("rule_name");
                        int totalTime = item.getInt("total_time");
                        if (ruleName.equals("Smoking")) {
                            binding.layoutSmoking.setVisibility(View.VISIBLE);
                            binding.tvSmoking.setText(totalTime + "s");
                        } else if (ruleName.equals("Mobile Usage")) {
                            binding.layoutMobileUsage.setVisibility(View.VISIBLE);
                            binding.tvMobileUsage.setText(totalTime + "s");
                        } else if (ruleName.equals("Sitting")) {
                            binding.layoutSitting.setVisibility(View.VISIBLE);
                            binding.tvStting.setText(totalTime + "s");
                        }
                        // Now you can use the ruleName and totalTime as needed
                        Log.d("Rule Name", ruleName);
                        Log.d("Total Time", String.valueOf(totalTime));
                    }
                }
            } catch (IOException | JSONException e) {
                Log.d("error==>>", e.getMessage());
            }
        } else {
            showErrorMessage(response);
        }
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        showToast("Failed: Try again");
        Log.d("error==>>", t.getMessage());
        cancelDialog(dialog);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == binding.spSection.getId()) {
            if (sectionModelList != null && sectionModelList.size() > 0 && position != 0) {
                SectionModel model = (SectionModel) sectionModelList.get(position);
                selectedSectionId = model.getId();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}