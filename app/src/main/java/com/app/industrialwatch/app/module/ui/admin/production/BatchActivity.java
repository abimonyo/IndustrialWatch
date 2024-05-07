package com.app.industrialwatch.app.module.ui.admin.production;

import static com.app.industrialwatch.common.utils.AppConstants.BUNDLE_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.BatchModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.common.base.TakePhotoActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.AppUtils;
import com.app.industrialwatch.common.utils.DownloadHelper;
import com.app.industrialwatch.common.utils.PermissionUtils;
import com.app.industrialwatch.databinding.ActivityBatchBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatchActivity extends BaseRecyclerViewActivity implements View.OnClickListener, Callback<ResponseBody>, OnRecyclerViewItemClickListener {

    ActivityBatchBinding binding;
    Bundle bundle;
    ProductionAdapter adapter;
    List<BaseItem> batchModelList;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null)
            bundle = getIntent().getExtras();
        initView();
    }

    private void initView() {
        binding.includedLayout.includedRcv.headerLayout.layoutHeaderWrapper.setVisibility(View.GONE);
        binding.includedLayout.btnAddSection.setText(getString(R.string.create_batch));
        binding.includedLayout.btnAddSection.setOnClickListener(this);
        initRecyclerView(binding.includedLayout.includedRcv.recyclerView);
        setPrimaryActionBar(binding.includedLayout.sectionAppLayout.primaryToolbar, bundle.getString(BUNDLE_KEY, ""));
        addDownloadImagesButton();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_download_images) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (PermissionUtils.checkAndRequestPermissions(BatchActivity.this
                        , new String[]{Manifest.permission.POST_NOTIFICATIONS},12))
                    downloadDefectedImages();

            }else
                downloadDefectedImages();
        } else
            startActivity(bundle, AddBatchActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(adapter);
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(AppConstants.GET_ALL_BATCHES, getServerParams(), this);
    }

    private Map<String, String> getServerParams() {
        Map<String, String> params = new HashMap<>();
        params.put("product_number", bundle.getString(AppConstants.KEY_NUMBER, ""));
        return params;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                JSONArray array = new JSONArray(response.body().string());
                batchModelList = new Gson().fromJson(array.toString(), new TypeToken<List<BatchModel>>() {
                }.getType());
                if (adapter == null) {
                    adapter = new ProductionAdapter(batchModelList, this);
                    setAdapter(adapter);
                }
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        } else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        cancelDialog(dialog);
        showToast("Failed: try again.");
    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        BatchModel model = (BatchModel) batchModelList.get(holder.getLayoutPosition());
        Bundle bundle_ = new Bundle();
        bundle_.putString(AppConstants.KEY_NUMBER, model.getBatch_number());
        startActivity(bundle_, BatchDetailActivity.class);
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }

    private void downloadDefectedImages() {
        DownloadHelper downloadManagerHelper = new DownloadHelper();
        String productNumber = bundle.getString(AppConstants.KEY_NUMBER);
        downloadManagerHelper.downloadFile(getApplicationContext(), AppConstants.Get_All_Defected_Images, productNumber);

    }

    @SuppressLint("ResourceAsColor")
    private void addDownloadImagesButton() {
        LinearLayout rootView = findViewById(binding.includedLayout.l.getId());
        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_download_images_button, null);
        Button button = inflatedView.findViewById(R.id.btn_download_images);
        ;
        button.setOnClickListener(this);
        button.setText("Defected Images");
        int paddingPixels = (int) getResources().getDisplayMetrics().density * 12; // Replace '8' with your desired padding in pixels
        button.setCompoundDrawablePadding(paddingPixels);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.END;
        float scale = getResources().getDisplayMetrics().density;
        int marginDp = 20; // Example margin in dp
        int marginPixels = (int) (marginDp * scale + 0.5f); // Convert dp to pixels
        button.setPadding(marginPixels, 0, marginPixels, 0);
        params.setMargins(marginPixels, marginPixels, marginPixels, marginPixels); // Add margin at the top
        button.setLayoutParams(params);
        rootView.addView(button, 1);

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