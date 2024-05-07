package com.app.industrialwatch.app.module.ui.admin.production;

import android.Manifest;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.app.industrialwatch.R;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.DownloadHelper;
import com.app.industrialwatch.common.utils.PermissionUtils;
import com.app.industrialwatch.databinding.ActivityBatchDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatchDetailActivity extends BaseActivity implements Callback<ResponseBody>, View.OnClickListener {

    ActivityBatchDetailsBinding binding;
    Bundle bundle;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBatchDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null)
            bundle = getIntent().getExtras();
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, bundle.getString(AppConstants.KEY_NUMBER, ""));
        binding.btnDownloadImages.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(AppConstants.GET_BATCHES_DETAILS, getServerParams(), this);
    }

    private Map<String, String> getServerParams() {
        Map<String, String> params = new HashMap<>();
        params.put("batch_number", bundle.getString(AppConstants.KEY_NUMBER, ""));
        return params;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                setData(object);
            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        } else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    private void setData(JSONObject object) {
        try {
            binding.tvDate.setText(object.getString("date"));
            binding.tvDefectedPiece.setText(String.valueOf(object.getInt("defected_piece")));
            binding.tvTotalPiece.setText(String.valueOf(object.getInt("total_piece")));
            binding.tvRejectionTolerance.setText(object.getDouble("rejection_tolerance") + "%");
            binding.tvYield.setText(object.getDouble("batch_yield") + "%");
            if (object.getInt("status") == 1) {
                binding.tvStatus.setText("Rejected");
                binding.tvStatus.setBackgroundResource(R.color.red);
            } else if (object.getInt("status") == 0) {
                binding.tvStatus.setText("Accepted");
                binding.tvStatus.setBackgroundResource(R.color.green);
            } else {
                binding.tvStatus.setText("Pending");
                binding.tvStatus.setBackgroundResource(R.color.grey_with_50);
                binding.tvYield.setText("--");


            }

        } catch (JSONException e) {
            Log.d("error==>>", e.getMessage());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        showToast("Failed:Try again.");
        cancelDialog(dialog);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnDownloadImages.getId()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (PermissionUtils.checkAndRequestPermissions(BatchDetailActivity.this
                        , new String[]{Manifest.permission.POST_NOTIFICATIONS}, 12))
                    downloadDefectedImages();

            } else
                downloadDefectedImages();
        }
    }

    private void downloadDefectedImages() {

        DownloadHelper downloadManagerHelper = new DownloadHelper();
        String productNumber = bundle.getString(AppConstants.KEY_NUMBER);
        downloadManagerHelper.downloadFile(getApplicationContext(), AppConstants.Get_All_Defected_Images, productNumber);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 12 && PermissionUtils.verifyPermission(grantResults)) {
            downloadDefectedImages();
        } else
            showToast("Notification permission needed before downloading.");
    }
}