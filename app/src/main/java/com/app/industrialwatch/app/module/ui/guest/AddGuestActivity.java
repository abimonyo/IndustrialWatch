package com.app.industrialwatch.app.module.ui.guest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.module.ui.employee.AddEmployeeActivity;
import com.app.industrialwatch.common.base.TakePhotoActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.AppUtils;
import com.app.industrialwatch.common.utils.FileUtils;
import com.app.industrialwatch.databinding.ActivityAddGuestBinding;

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

public class AddGuestActivity extends TakePhotoActivity implements View.OnClickListener, Callback<ResponseBody> {

    ActivityAddGuestBinding binding;
    ClipData clipData;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddGuestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeImagePicker();
        initView();
    }
    private void initView(){
        binding.ivEmployeeProfile.setOnClickListener(this);
        binding.btnAddEmployee.setOnClickListener(this);
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, getString(R.string.add_guest));

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

        Map<String, RequestBody> bodyMap = new HashMap<>();
        List<MultipartBody.Part> imagePart = new ArrayList<>();
        try {

            RequestBody nameBody = RequestBody.create(getValueFromField(binding.etEmployeeName), MediaType.parse("text/plain"));
            bodyMap.put("name", nameBody);

            for (int i = 0; i < clipData.getItemCount(); i++) {
                File file = new File(Objects.requireNonNull(FileUtils.getPath(AddGuestActivity.this, clipData.getItemAt(i).getUri())));
                if (file.exists()) {
                    RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
                    MultipartBody.Part body = MultipartBody.Part.createFormData("files", file.getName(), requestFile);
                    imagePart.add(body);

                }
            }

            dialog = getProgressDialog(false);
            showProgressDialog(dialog);
            doPostRequestWithMapBody(AppConstants.INSERT_GUEST, bodyMap, imagePart, this);
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
        if (response.isSuccessful()){
            if (call.request().url().url().toString().contains(AppConstants.INSERT_GUEST)) {
                try {
                    showToast(new JSONObject(response.body().string()).getString("message"));
                    cancelDialog(dialog);
                    finish();

                } catch (JSONException |IOException e) {
                   Log.d("error-->>",e.getMessage());
                }
            }else
                showErrorMessage(response);
            cancelDialog(dialog);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        showToast("Failed, Try again.");
        cancelDialog(dialog);
    }
}