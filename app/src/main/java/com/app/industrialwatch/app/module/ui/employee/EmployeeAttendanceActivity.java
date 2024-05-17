package com.app.industrialwatch.app.module.ui.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.EmployeeModel;
import com.app.industrialwatch.app.module.ui.adapter.EmployeeAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityEmployeeAttendanceBinding;
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

public class EmployeeAttendanceActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody> {

    ActivityEmployeeAttendanceBinding binding;
    Bundle bundle;
    EmployeeModel model;
    Dialog dialog;
    EmployeeAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeAttendanceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            model = (EmployeeModel) bundle.getSerializable(AppConstants.BUNDLE_KEY);
        }
        initView();
    }

    private void initView() {
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, model.getName());
        setRecyclerViewHeader("Date", "", "", "Status");
        initRecyclerView(binding.includedRcv.recyclerView);
        setRecyclerViewDivider();
        //findTextViewById(R.id.tv_end_item).setTextColor(getResources().getColor(R.color.white));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bundle != null) {
            adapter=null;
            dialog = getProgressDialog(false);
            showProgressDialog(dialog);
            setAdapter(null);
            doGetRequest(AppConstants.GET_EMPLOYEE_ATTENDANCE, getParams("employee_id", model.getId() + ""), this);
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_EMPLOYEE_ATTENDANCE)) {
                    JSONArray array = new JSONArray(response.body().string());
                    AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM= BaseItem.ITEM_EMPLOYEE_ATTENDANCE;
                  List<BaseItem>  employeeModelList = new Gson().fromJson(array.toString(), new TypeToken<List<EmployeeModel>>() {
                    }.getType());
                    if (adapter == null) {

                        adapter = new EmployeeAdapter(employeeModelList, null, EmployeeAttendanceActivity.this);
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
}