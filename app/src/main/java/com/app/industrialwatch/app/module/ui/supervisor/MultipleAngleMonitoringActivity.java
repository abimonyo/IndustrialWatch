package com.app.industrialwatch.app.module.ui.supervisor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.app.industrialwatch.R;
import com.app.industrialwatch.common.base.TakePhotoActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.FileUtils;
import com.app.industrialwatch.common.utils.JsonUtil;
import com.app.industrialwatch.databinding.ActivityMultipleAngleMonitoringBinding;
import com.app.industrialwatch.databinding.LayoutDialogDefectReportBinding;

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

public class MultipleAngleMonitoringActivity extends TakePhotoActivity implements View.OnClickListener, Callback<ResponseBody> {

    ActivityMultipleAngleMonitoringBinding binding;
    String type;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMultipleAngleMonitoringBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        initializeImagePicker();
        setSecondaryActionBar(binding.includedToolbar.secondaryToolbar, "Multiple Angle Monitoring",true);
    }

    private void init() {
        binding.selectFrontImageButton.setOnClickListener(this);
        binding.selectBackImageButton.setOnClickListener(this);
        binding.selectSideImagesButton.setOnClickListener(this);
        binding.startMonitoringButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.selectFrontImageButton.getId()) {
            type = "front";
            showChoosePhotoDialog(false);
        } else if (v.getId() == binding.selectBackImageButton.getId()) {
            type = "back";
            showChoosePhotoDialog(false);
        } else if (v.getId() == binding.selectSideImagesButton.getId()) {
            type = "sides";
            showChoosePhotoDialog(true);
        } else if (v.getId() == binding.startMonitoringButton.getId()) {
            makeApiCall();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                processLauncherResult(data);

            } else if (selectedPhotoUri != null) {
                //loadImageInView();
            }
        } else {
            selectedPhotoUri = null;
            showToast("Error, Try again selecting images");
        }
    }

    private void processLauncherResult(Intent data) {
        switch (type) {
            case "front":
                binding.frontImageView.setImageURI(data.getData());
                binding.frontImageView.setTag(data.getData());
                break;
            case "back":
                binding.backImageView.setImageURI(data.getData());
                binding.backImageView.setTag(data.getData());
                break;
            case "sides":
                binding.sideImageView1.setImageURI(data.getClipData().getItemAt(0).getUri());
                binding.sideImageView1.setTag(data.getClipData().getItemAt(0).getUri());
                binding.sideImageView2.setImageURI(data.getClipData().getItemAt(1).getUri());
                binding.sideImageView2.setTag(data.getClipData().getItemAt(1).getUri());
                binding.sideImageView3.setImageURI(data.getClipData().getItemAt(2).getUri());
                binding.sideImageView3.setTag(data.getClipData().getItemAt(2).getUri());
                binding.sideImageView4.setImageURI(data.getClipData().getItemAt(3).getUri());
                binding.sideImageView4.setTag(data.getClipData().getItemAt(3).getUri());
                break;

        }
    }

    private MultipartBody.Part createMultipartBodyPart(String key, Uri uri) {
        File file = new File(Objects.requireNonNull(FileUtils.getPath(MultipleAngleMonitoringActivity.this, uri)));
        if (file.exists()) {
            RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
            return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
        }
        return null;
    }

    private void addImagesToPartList(Map<String, ImageView> imageViewMap, List<MultipartBody.Part> imageParts) {
        for (Map.Entry<String, ImageView> entry : imageViewMap.entrySet()) {
            String key = entry.getKey();
            ImageView imageView = entry.getValue();
            Uri uri = (Uri) imageView.getTag();  // Assuming you've set the URI as tag
            if (uri != null) {
                MultipartBody.Part part = createMultipartBodyPart(key, uri);
                if (part != null) {
                    imageParts.add(part);
                }
            }
        }
    }

    private void makeApiCall() {
        List<MultipartBody.Part> imageParts = new ArrayList<>();

        // Map of ImageView elements with corresponding keys
        Map<String, ImageView> imageViewMap = new HashMap<>();
        imageViewMap.put("front", binding.frontImageView);
        imageViewMap.put("back", binding.backImageView);
        imageViewMap.put("sides", binding.sideImageView1);
        imageViewMap.put("sides", binding.sideImageView2);
        imageViewMap.put("sides", binding.sideImageView3);
        imageViewMap.put("sides", binding.sideImageView4);

        // Add images to the parts list
        addImagesToPartList(imageViewMap, imageParts);
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doPostMultipartBody(AppConstants.ANGLE_MONITORING, imageParts, MultipleAngleMonitoringActivity.this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                showDefectReportDialog(object);
            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        } else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        showToast("Failed: Try again.");
        cancelDialog(dialog);
    }

    @SuppressLint("SetTextI18n")
    public void showDefectReportDialog(JSONObject defectReportJson) {
        LayoutInflater inflater = LayoutInflater.from(this);
        LayoutDialogDefectReportBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_dialog_defect_report, null, false);

        try {
            binding.tvStatus.setText("Status: " + defectReportJson.getString("status"));
            JSONObject object=defectReportJson.getJSONObject("defects_report");
            binding.tvFrontDefects.setText("Front Defects: " + listToString(JsonUtil.jsonArrayToList(object.getJSONArray("front").toString())));
            binding.tvBackDefects.setText("Back Defects: " + listToString(JsonUtil.jsonArrayToList(object.getJSONArray("back").toString())));
            if (object.getJSONArray("sides").length()>0) {
                String sideDefectsText = "Side Defects:\n";
                StringBuilder builder=new StringBuilder();
                for (HashMap<String, Object> defect : JsonUtil.jsonArrayToHashMapList(object.getJSONArray("sides").toString())) {
                    for (Map.Entry<String, Object> entry : defect.entrySet()) {
                        Log.d("error==>>","Side " + entry.getKey() + ": " + entry.getValue());
                         builder.append("side " + entry.getKey() + ": " + entry.getValue() + "\n");
                    }
                }
                binding.tvSideDefects.setText(sideDefectsText+builder.toString());
            }
        } catch (JSONException e) {
            Log.d("error==>>", e.getMessage());
            showToast("Error Processing Response.");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(binding.getRoot());
        AlertDialog dialog = builder.create();
        binding.btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private String listToString(List<String> list) {
        return list == null ? "None" : String.join(", ", list);
    }
}