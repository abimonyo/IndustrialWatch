package com.app.industrialwatch.app.module.ui.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.EmployeeModel;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.module.ui.adapter.EmployeeAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityEmployeeRecordBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRecordActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, AdapterView.OnItemSelectedListener, OnRecyclerViewItemClickListener, SearchView.OnQueryTextListener {

    ActivityEmployeeRecordBinding binding;
    List<BaseItem> sectionModelList;
    Dialog dialog;
    int selectedSectionId = -1;
    EmployeeAdapter adapter;
    List<BaseItem> employeeModelList;
    Bundle bundle;
    boolean isForRanking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeRecordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            if (bundle.getString(AppConstants.FROM, "").equals(AppConstants.RANKING))
                isForRanking = true;
        }
        initView();
    }

    private void initView() {
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, getString(R.string.employee_record));
        initRecyclerViewForGrid(binding.includedRcv.recyclerView, 2);
        binding.includedRcv.headerLayout.layoutHeaderWrapper.setVisibility(View.GONE);
        sectionModelList = new ArrayList<>();
        sectionModelList.add(new SectionModel(-1, "All Section"));
        setSpinnerAdapter(sectionModelList, binding.spSection, this);
        binding.searchBar.setOnQueryTextListener(this);
        if (isForRanking) {
            // employee ranking
            binding.searchBar.setVisibility(View.GONE);
            setPrimaryActionBar(binding.includedToolbar.primaryToolbar, getString(R.string.employee_ranking));
            initRecyclerView(binding.includedRcv.recyclerView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog = getProgressDialog(false);
        adapter = null;
        setAdapter(null);
        showProgressDialog(dialog);
        doGetRequest(AppConstants.SECTION_URL, getParams("status", 1 + ""), this);
        doGetRequest(AppConstants.GET_ALL_EMPLOYEES, getMultipleServerParams(), this);
    }

    private Map<String, String> getMultipleServerParams() {
        Map<String, String> params = new HashMap<>();
        params.put("section_id", selectedSectionId + "");
        params.put("ranking_required", (isForRanking ? 1 : 0) + "");
        return params;
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

                } else if (call.request().url().url().toString().contains(AppConstants.GET_ALL_EMPLOYEES)) {
                    JSONArray array = new JSONArray(response.body().string());
                    if (isForRanking)
                        AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.ITEM_EMPLOYEE_RANKING;
                    else
                        AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.ITEM_EMPLOYEE_RECORD;

                    employeeModelList = new Gson().fromJson(array.toString(), new TypeToken<List<EmployeeModel>>() {
                    }.getType());
                    if (adapter == null) {
                        adapter = new EmployeeAdapter(employeeModelList, (isForRanking ? null : this), EmployeeRecordActivity.this);
                        setAdapter(adapter);
                    } else {
                        adapter.clearItems();
                        adapter.addAll(employeeModelList);
                    }
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
            if (sectionModelList != null && sectionModelList.size() > 0) {
                SectionModel model=(SectionModel) sectionModelList.get(position);
                selectedSectionId = model.getId();
                doGetRequest(AppConstants.GET_ALL_EMPLOYEES, getMultipleServerParams(), this);

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        EmployeeModel model = (EmployeeModel) employeeModelList.get(holder.getLayoutPosition());
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.BUNDLE_KEY, model);
        startActivity(bundle, EmployeeDetailActivity.class);
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return true;
    }

    private void filter(String query) {
        List<BaseItem> filteredData = new ArrayList<>();
        for (int i = 0; i < employeeModelList.size(); i++) {
            EmployeeModel model = (EmployeeModel) employeeModelList.get(i);
            if (model.getName().toLowerCase().contains(query.toLowerCase())
                    || model.getSectionName().toLowerCase().contains(query.toLowerCase())
                    || model.getJobRole().toLowerCase().contains(query.toLowerCase())) {
                filteredData.add(model);
            }
        }
        if (filteredData.isEmpty()) {
            showToast("No Data Found.");
        } else {
            adapter.clearItems();
            adapter.addAll(filteredData);
        }

    }
}