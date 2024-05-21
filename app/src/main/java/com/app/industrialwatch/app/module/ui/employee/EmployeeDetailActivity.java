package com.app.industrialwatch.app.module.ui.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.EmployeeModel;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityEmployeeDetailBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeDetailActivity extends BaseActivity implements Callback<ResponseBody>, View.OnClickListener {

    ActivityEmployeeDetailBinding binding;
    Bundle bundle;
    Dialog dialog;
    EmployeeModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            model = (EmployeeModel) bundle.getSerializable(AppConstants.BUNDLE_KEY);
        }
        initView();
    }

    private void initView() {
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, model.getName());
        binding.btnAttendance.setOnClickListener(this);
        binding.btnSummary.setOnClickListener(this);
        binding.btnViolations.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bundle != null) {
            dialog = getProgressDialog(false);
            showProgressDialog(dialog);
            doGetRequest(AppConstants.GET_EMPLOYEE_DETAIL, getParams("employee_id", model.getId() + ""), this);
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_EMPLOYEE_DETAIL)) {
                    JSONObject object = new JSONObject(response.body().string());
                    binding.tvTotalFine.setText(object.getInt("total_fine") + "");
                    binding.tvProductivity.setText(object.getDouble("productivity") + "%");
                    binding.progressBar.setProgress(Float.valueOf(object.getDouble("productivity") + ""));
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
    public void onClick(View v) {
        if (v.getId() == binding.btnAttendance.getId()) {
            startActivity(bundle, EmployeeAttendanceActivity.class);
        } else if (v.getId() == binding.btnViolations.getId()) {
            startActivity(bundle, EmployeeViolationActivity.class);
        } else if (v.getId() == binding.btnSummary.getId()) {
            startActivity(bundle, EmployeeSummaryActivity.class);
        }
    }
}