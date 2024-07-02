package com.app.industrialwatch.app.module.ui.employee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.BatchModel;
import com.app.industrialwatch.app.data.models.RawMaterialModel;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.network.ApiService;
import com.app.industrialwatch.app.network.RetrofitClient;
import com.app.industrialwatch.common.base.TakePhotoActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.AppUtils;
import com.app.industrialwatch.common.utils.FileUtils;
import com.app.industrialwatch.databinding.ActivityAddEmployeeBinding;
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

public class AddEmployeeActivity extends TakePhotoActivity implements View.OnClickListener, Callback<ResponseBody>, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    ActivityAddEmployeeBinding binding;
    List<BaseItem> sectionModelList;
    List<BaseItem> jobRoleModelList;
    int selectedSectionId, selectedRoleId;
    String selectedGender, selectedJobType;
    ClipData clipData;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeImagePicker();
        initView();
    }

    private void initView() {
        binding.ivEmployeeProfile.setOnClickListener(this);
        binding.btnAddEmployee.setOnClickListener(this);
        binding.rdgGender.setOnCheckedChangeListener(this);
        binding.rdJobType.setOnCheckedChangeListener(this);
        sectionModelList = new ArrayList<>();
        jobRoleModelList = new ArrayList<>();
        sectionModelList.add(new SectionModel(-1, "Select Section"));
        jobRoleModelList.add(new SectionModel(-1, "Select Role"));
        setSpinnerAdapter(sectionModelList, binding.spSection, this);
        setSpinnerAdapter(jobRoleModelList, binding.spRole, this);
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, getString(R.string.add_employee));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sectionModelList.size() > 1 || jobRoleModelList.size() > 1) {
            sectionModelList.clear();
            jobRoleModelList.clear();
        }
        doGetRequest(AppConstants.SECTION_URL, getParams("status", "1"), this);
        doGetRequest(AppConstants.GET_ALL_ROLES, this);
    }


    @Override
    public void onClick(View v) {
        if (v.equals(binding.ivEmployeeProfile)) {

            showChoosePhotoDialog(true);
        } else if (v.getId() == binding.btnAddEmployee.getId()) {
            validateAndCallApi();
        }
    }

    private void validateAndCallApi() {
        if (!AppUtils.validateEmptyEditText(binding.etEmployeeName)) {
            binding.etEmployeeName.setError("Please enter name");
            binding.etEmployeeName.requestFocus();
            return;
        }
        if (!AppUtils.validateEmptyEditText(binding.etEmployeeUserName)) {
            binding.etEmployeeUserName.setError("Please enter username");
            binding.etEmployeeUserName.requestFocus();
            return;
        }
        if (!AppUtils.validateEmptyEditText(binding.etEmployeePassword)) {
            binding.etEmployeePassword.setError("Please enter password");
            binding.etEmployeePassword.requestFocus();
            return;
        }
        if (!AppUtils.validateEmptyEditText(binding.etEmployeeSalary)) {
            binding.etEmployeePassword.setError("Please enter salary");
            binding.etEmployeePassword.requestFocus();
            return;
        }
        if (selectedSectionId == 0) {
            showToast("Please select section");
            return;
        }
        if (selectedRoleId == 0) {
            showToast("Please select role");
            return;
        }
        if (selectedGender == null && selectedGender.equals("")) {
            showToast("Please select gender");
            return;
        }
        if (selectedJobType == null && selectedJobType.equals("")) {
            showToast("Please select job type");
            return;
        }
        if (clipData != null && clipData.getItemCount() < 5 && selectedPhotoUri != null && selectedPhotoUri.equals("")) {
            showToast("please upload images.");
            return;
        }
        Map<String, RequestBody> bodyMap = new HashMap<>();
        List<MultipartBody.Part> imagePart = new ArrayList<>();
        try {

            RequestBody nameBody = RequestBody.create(getValueFromField(binding.etEmployeeName), MediaType.parse("text/plain"));
            RequestBody userNameBody = RequestBody.create(getValueFromField(binding.etEmployeeUserName), MediaType.parse("text/plain"));
            RequestBody passwordBody = RequestBody.create(getValueFromField(binding.etEmployeePassword), MediaType.parse("text/plain"));
            RequestBody sectionBody = RequestBody.create(String.valueOf(selectedSectionId), MediaType.parse("text/plain"));
            RequestBody roleBody = RequestBody.create(String.valueOf(selectedRoleId), MediaType.parse("text/plain"));
            RequestBody genderBody = RequestBody.create(selectedGender, MediaType.parse("text/plain"));
            RequestBody jobTypeBody = RequestBody.create(selectedJobType, MediaType.parse("text/plain"));
            RequestBody salaryBody = RequestBody.create(getValueFromField(binding.etEmployeeSalary), MediaType.parse("text/plain"));
            bodyMap.put("name", nameBody);
            bodyMap.put("username", userNameBody);
            bodyMap.put("password", passwordBody);
            bodyMap.put("job_role_id", roleBody);
            bodyMap.put("salary", salaryBody);
            bodyMap.put("section_id", sectionBody);
            bodyMap.put("job_type", jobTypeBody);
            bodyMap.put("gender", genderBody);
            for (int i = 0; i < clipData.getItemCount(); i++) {
                File file = new File(Objects.requireNonNull(FileUtils.getPath(AddEmployeeActivity.this, clipData.getItemAt(i).getUri())));
                if (file.exists()) {
                    RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
                    MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
                    imagePart.add(body);

                }
            }

            dialog = getProgressDialog(false);
            showProgressDialog(dialog);
            doPostRequestWithMapBody(AppConstants.INSERT_EMPLOYEE, bodyMap, imagePart, this);
        } catch (Exception e) {
            Log.d("error==>>", e.getMessage());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                clipData = data.getClipData();
                if (clipData != null && clipData.getItemCount() < 5) {
                    showToast("please select at least 5 images.");
                    return;
                }
                selectedPhotoUri = data.getClipData().getItemAt(0).getUri();
                loadImageInView();
            } else if (selectedPhotoUri != null) {
                loadImageInView();
            }
        } else {
            selectedPhotoUri = null;
            showToast("Error, Try again selecting images");
        }


    }

    private void loadImageInView() {
        binding.ivEmployeeProfile.setImageURI(selectedPhotoUri);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.SECTION_URL)) {
                    JSONArray array = new JSONArray(response.body().string());
                    Log.d("section", array.length() + "");
                    sectionModelList.addAll(new Gson().fromJson(array.toString(), new TypeToken<List<SectionModel>>() {
                    }.getType()));

                } else if (call.request().url().url().toString().contains(AppConstants.GET_ALL_ROLES)) {
                    JSONArray array = new JSONArray(response.body().string());
                    Log.d("role", array.length() + "");

                    jobRoleModelList.addAll(new Gson().fromJson(array.toString(), new TypeToken<List<SectionModel>>() {
                    }.getType()));
                } else if (call.request().url().url().toString().contains(AppConstants.INSERT_EMPLOYEE)) {
                    showToast(new JSONObject(response.body().string()).getString("message"));
                    finish();
                }

            } catch (IOException | JSONException e) {
                Log.d("error==>>", e.getMessage());
            }

        } else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        showToast("Failed, Try again.");
        cancelDialog(dialog);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == binding.spSection.getId()) {
            if (sectionModelList != null && sectionModelList.size() > 0 && position != 0) {
                SectionModel model=(SectionModel)sectionModelList.get(position);
                selectedSectionId =model.getId();
            }
        } else if (jobRoleModelList != null && jobRoleModelList.size() > 0 && position != 0) {
            SectionModel model=(SectionModel)jobRoleModelList.get(position);

            selectedRoleId = model.getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == binding.rdgGender.getId()) {
            if (group.getCheckedRadioButtonId() == binding.rdMale.getId())
                selectedGender = "Male";
            else if (group.getCheckedRadioButtonId() == binding.rdFemale.getId())
                selectedGender = "Female";
        } else {
            if (group.getCheckedRadioButtonId() == binding.rdFullTime.getId())
                selectedJobType = "Full time";
            else if (group.getCheckedRadioButtonId() == binding.rdPartTime.getId())
                selectedJobType = "Part time";
        }
    }
}