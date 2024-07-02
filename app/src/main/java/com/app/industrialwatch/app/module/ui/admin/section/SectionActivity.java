package com.app.industrialwatch.app.module.ui.admin.section;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.data.models.StockModel;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.app.module.ui.adapter.SectionAdapter;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivitySectionBinding;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionActivity extends BaseRecyclerViewActivity implements OnRecyclerViewItemClickListener, View.OnClickListener, Callback<ResponseBody> {

    ActivitySectionBinding binding;
    SectionAdapter adapter;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();

    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(AppConstants.SECTION_URL, getMultipleParams(), this);

    }
    private Map<String, String> getMultipleParams() {
        Map<String, String> params = new HashMap<>();
        params.put("status", 1+"");
        params.put("is_special","true");
        return params;

    }


    private void initView() {
        binding.btnAddSection.setOnClickListener(this);
        hideView(binding.includedRcv.headerLayout.layoutHeaderWrapper);
        setPrimaryActionBar(binding.sectionAppLayout.primaryToolbar, getString(R.string.section));
        initRecyclerView(binding.includedRcv.recyclerView);
        addArchivedSectionButton();
    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        SectionModel model = (SectionModel) adapter.getItemAt(holder.getLayoutPosition());
        Bundle bundle = new Bundle();
        bundle.putInt("id", model.getId());
        startActivity(bundle, SectionDetailsActivity.class);
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {
        if (resourceId == holder.itemView.findViewById(resourceId).getId()) {
            SectionModel model = (SectionModel) adapter.getItemAt(holder.getLayoutPosition());
            showProgressDialog(dialog);
            doGetRequest(AppConstants.CHANGE_SECTION_ACTIVITY_STATUS, getParams("section_id", model.getId() + ""), this);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnAddSection.getId()) {
            startActivity(new Intent(getApplicationContext(), AddUpdateSectionActivity.class));
        } else if (v.getId() == R.id.btn_download_images) {
            startActivity(new Intent(getApplicationContext(), ArchivedSectionActivity.class));
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.SECTION_URL)) {

                    JSONArray jsonArray = new JSONArray(response.body().string());
                    List<BaseItem> sectionList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SectionModel>>() {
                    }.getType());
                    if (adapter == null) {
                        adapter = new SectionAdapter(sectionList, SectionActivity.this);
                        setAdapter(adapter);
                    } else {
                        adapter.clearItems();
                        adapter.addAll(sectionList);
                    }

                } else if (call.request().url().url().toString().contains(AppConstants.CHANGE_SECTION_ACTIVITY_STATUS)) {
                    JSONObject object = new JSONObject(response.body().string());
                    showToast(object.getString("message"));
                    doGetRequest(AppConstants.SECTION_URL, getParams("status", 1 + ""), this);
                }
            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        } else if (response.code() == 404 && adapter != null) {
            adapter.clearItems();
            adapter.notifyDataSetChanged();

        } else {
            showErrorMessage(response);
        }
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        showToast("Failed:Try again");
        cancelDialog(dialog);
    }

    private void addArchivedSectionButton() {
        LinearLayout rootView = findViewById(binding.l.getId());
        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_download_images_button, null);
        Button button = inflatedView.findViewById(R.id.btn_download_images);
        button.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        button.setOnClickListener(this);
        button.setText("Archived Section");
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
}