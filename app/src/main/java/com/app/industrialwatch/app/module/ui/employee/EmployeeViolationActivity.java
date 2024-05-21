package com.app.industrialwatch.app.module.ui.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.EmployeeModel;
import com.app.industrialwatch.app.data.models.ViolationModel;
import com.app.industrialwatch.app.module.ui.adapter.EmployeeAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityEmployeeViolationBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeViolationActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, OnRecyclerViewItemClickListener {

    ActivityEmployeeViolationBinding binding;
    Bundle bundle;
    EmployeeModel model;
    Dialog dialog;
    EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeViolationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            model = (EmployeeModel) bundle.getSerializable(AppConstants.BUNDLE_KEY);
        }
        initView();
    }

    private void initView() {
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, model.getName());
        initRecyclerView(binding.includedRcv.recyclerView);
        binding.includedRcv.headerLayout.layoutHeaderWrapper.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (model != null) {
            adapter=null;
            setAdapter(null);
            dialog = getProgressDialog(false);
            showProgressDialog(dialog);
            doGetRequest(AppConstants.GET_ALL_VIOLATIONS, getParams("employee_id", model.getId() + ""), this);
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_ALL_VIOLATIONS)) {
                    JSONArray array = new JSONArray(response.body().string());
                    List<BaseItem> violationModelList=new Gson().fromJson(array.toString(),new TypeToken<List<ViolationModel>>(){}.getType());
                    if (adapter==null){
                        adapter=new EmployeeAdapter(violationModelList,this,EmployeeViolationActivity.this);
                        setAdapter(adapter);
                    }else {
                        adapter.clearItems();
                        adapter.addAll(violationModelList);
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
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {

    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }
}