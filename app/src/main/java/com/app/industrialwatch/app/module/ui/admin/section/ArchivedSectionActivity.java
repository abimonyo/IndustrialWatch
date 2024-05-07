package com.app.industrialwatch.app.module.ui.admin.section;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.module.ui.adapter.SectionAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityArchivedSectionBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArchivedSectionActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, OnRecyclerViewItemClickListener {

    ActivityArchivedSectionBinding binding;
    Dialog dialog;
    SectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArchivedSectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setPrimaryActionBar(binding.includedLayout.sectionAppLayout.primaryToolbar, "Archived Section");
        binding.includedLayout.includedRcv.headerLayout.layoutHeaderWrapper.setVisibility(View.GONE);
        initRecyclerView(binding.includedLayout.includedRcv.recyclerView);
        hideView(binding.includedLayout.btnAddSection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(AppConstants.SECTION_URL, getParams("status", 0 + ""), this);

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
                        adapter = new SectionAdapter(sectionList, ArchivedSectionActivity.this);
                        setAdapter(adapter);
                    } else {
                        adapter.clearItems();
                        adapter.addAll(sectionList);
                    }
                } else if (call.request().url().url().toString().contains(AppConstants.CHANGE_SECTION_ACTIVITY_STATUS)) {
                    JSONObject object = new JSONObject(response.body().string());
                    showToast(object.getString("message"));
                    doGetRequest(AppConstants.SECTION_URL, getParams("status", 0 + ""), this);
                }
            } catch (IOException | JSONException e) {
                Log.d("error==>>", e.getMessage());
            }


        } else if (response.code() == 404 && adapter != null) {

                adapter.clearItems();
                adapter.notifyDataSetChanged();

        } else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        cancelDialog(dialog);
        Log.d("error==>>", t.getMessage());
        showToast("Failed: Try again.");
    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {

    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {
        if (resourceId == holder.itemView.findViewById(resourceId).getId()) {
            SectionModel model = (SectionModel) adapter.getItemAt(holder.getLayoutPosition());
            showProgressDialog(dialog);
            doGetRequest(AppConstants.CHANGE_SECTION_ACTIVITY_STATUS, getParams("section_id", model.getId() + ""), this);
        }

    }
}