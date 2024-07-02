package com.app.industrialwatch.app.module.ui.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.EmployeeModel;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.CalendarUtils;
import com.app.industrialwatch.databinding.ActivityEmployeeSummaryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeSummaryActivity extends BaseActivity implements View.OnClickListener, Callback<ResponseBody> {

    ActivityEmployeeSummaryBinding binding;
    Dialog dialog;
    Bundle bundle;
    EmployeeModel model;
    StringBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeSummaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            model = (EmployeeModel) bundle.getSerializable(AppConstants.BUNDLE_KEY);
        }
        initView();
    }

    private void initView() {
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar,model!=null?model.getName():"");
        binding.includedLayout.tvYear.setOnClickListener(this);
        binding.includedLayout.tvMonth.setOnClickListener(this);
        builder = new StringBuilder();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if (model != null) {
            dialog = getProgressDialog(false);
            showProgressDialog(dialog);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            binding.includedLayout.tvYear.setText(currentYear + "");
            binding.includedLayout.tvMonth.setText(CalendarUtils.getCurrentMonthShort());
            doGetRequest(AppConstants.GET_EMPLOYEE_SUMMARY, getMultipleParams((currentMonth + 1) + "," + currentYear), this);
       // }
    }

    private Map<String, String> getMultipleParams(String date) {
        Map<String, String> params = new HashMap<>();
        params.put("employee_id", model!=null?model.getId()+"": SharedPreferenceManager.getInstance().read("id", 0) + "");
        params.put("date", date);
        return params;

    }

    private void showYearMenu() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupStyle);
        PopupMenu popup = new PopupMenu(wrapper, findViewById(R.id.tv_year));
        for (int i = 2015; i <= currentYear; i++) {
            popup.getMenu().add(0, i, i, String.valueOf(i));
        }
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                ((TextView) findViewById(R.id.tv_year)).setText(item.getTitle().toString());
                showProgressDialog(dialog);
                doGetRequest(AppConstants.GET_EMPLOYEE_SUMMARY, getMultipleParams(binding.includedLayout.tvMonth.getTag().toString() + "," + item.getTitle().toString()), EmployeeSummaryActivity.this);

                return true;
            }
        });
        popup.show(); //showing popup menu
    }

    private void showMonthMenu() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] months = CalendarUtils.getMonthNames();
        Context wrapper = new ContextThemeWrapper(this, R.style.PopupStyle);
        PopupMenu popup = new PopupMenu(wrapper, findViewById(R.id.tv_month));

        for (int i = 0; i < months.length; i++) {
            popup.getMenu().add(0, i + 1, i, months[i]);
        }
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                binding.includedLayout.tvMonth.setTag(item.getItemId());
                showProgressDialog(dialog);
                doGetRequest(AppConstants.GET_EMPLOYEE_SUMMARY, getMultipleParams(item.getItemId() + "," + binding.includedLayout.tvYear.getText().toString().trim()), EmployeeSummaryActivity.this);
                ((TextView) findViewById(R.id.tv_month)).setText(item.getTitle().toString());

                return true;
            }
        });
        popup.show(); //showing popup menu

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.includedLayout.tvYear.getId()) {
            showYearMenu();
        } else if (v.getId() == binding.includedLayout.tvMonth.getId()) {
            showMonthMenu();
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_EMPLOYEE_SUMMARY)) {
                    JSONObject object = new JSONObject(response.body().string());
                    binding.includedLayout.tvTotalViolation.setText(object.getInt("violation_count") + "");
                    binding.includedLayout.tvTotalFine.setText(object.getDouble("total_fine") + "");
                    String[] arrgs = object.getString("attendance_rate").split("/");
                    binding.includedLayout.progressBar.setTotalProgress(arrgs[1].equals("A") ? 0 : Float.valueOf(arrgs[1]));
                    binding.includedLayout.progressBar.setProgress(arrgs[0].equals("N") ? 0 : Float.valueOf(arrgs[0]));
                    binding.includedLayout.tvAttendance.setText(arrgs[0]);
                    binding.includedLayout.tvTotalAttendance.setText("/" + arrgs[1]);

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