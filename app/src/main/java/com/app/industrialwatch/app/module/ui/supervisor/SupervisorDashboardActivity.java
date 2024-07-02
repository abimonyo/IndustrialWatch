package com.app.industrialwatch.app.module.ui.supervisor;

import static com.app.industrialwatch.common.utils.AppConstants.MARK_ATTENDANCE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.GridItemModel;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.app.module.ui.adapter.GridItemAdapter;
import com.app.industrialwatch.app.module.ui.admin.main.AdminDashboardActivity;
import com.app.industrialwatch.app.module.ui.admin.section.SectionActivity;
import com.app.industrialwatch.app.module.ui.authentication.LoginActivity;
import com.app.industrialwatch.app.module.ui.employee.AddEmployeeActivity;
import com.app.industrialwatch.app.module.ui.employee.EmployeeAttendanceActivity;
import com.app.industrialwatch.app.module.ui.employee.EmployeeProductivityDashboardActivity;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.base.TakePhotoActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.FileUtils;
import com.app.industrialwatch.databinding.ActivitySupervisorDashboardBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupervisorDashboardActivity extends TakePhotoActivity implements View.OnClickListener, Callback<ResponseBody> {

    ActivitySupervisorDashboardBinding binding;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupervisorDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initializeImagePicker();
    }

    private void initView() {
        binding.includedLayout.layoutIncluded.dashboardTitle.setText(R.string.supervisor_dashboard);
        binding.includedLayout.layoutIncluded.ivBack.setVisibility(View.GONE);
        binding.includedLayout.tvName.setText(SharedPreferenceManager.getInstance().read("name", "Name"));
        binding.includedLayout.gridLayout.girdView.setAdapter(new GridItemAdapter(this, fillGridItems(), this));
        binding.includedLayout.layoutIncluded.ivLogout.setOnClickListener(v -> {
            SharedPreferenceManager.setSingletonInstance(SupervisorDashboardActivity.this);
            SharedPreferenceManager.getInstance().save(AppConstants.LOGIN_KEY, false);
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });
    }

    private ArrayList<GridItemModel> fillGridItems() {
        ArrayList<GridItemModel> gridItemModels = new ArrayList<>();
        gridItemModels.add(new GridItemModel(getString(R.string.employee_monitoring), R.drawable.img_emoloyee_monitoring));
        gridItemModels.add(new GridItemModel(getString(R.string.defect_monitoring), R.drawable.img_defect_monitoring));
        gridItemModels.add(new GridItemModel(getString(R.string.my_attendance), R.drawable.img_attendance));
        gridItemModels.add(new GridItemModel(getString(R.string.mark_attendance), R.drawable.ic_mark_attendance));
        return gridItemModels;
    }

    @Override
    public void onClick(View v) {
        if ((int) v.getTag() == 0) {
            startActivity(new Intent(getApplicationContext(), MonitoringEmployeeActivity.class));
        } else if ((int) v.getTag() == 1) {
           // DefectsMonitoringActivity
            startActivity(new Intent(getApplicationContext(), MonitoringDashboardActivity.class));
        } else if ((int) v.getTag() == 2) {
            startActivity(new Intent(getApplicationContext(), EmployeeAttendanceActivity.class));
        } else if ((int) v.getTag() == 3) {
            // mark attendance
            isVideo=false;
            dialog=getProgressDialog(true);
            showChoosePhotoDialog(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedPhotoUri = data.getData();
                List<MultipartBody.Part> imagePart = new ArrayList<>();
                File file = new File(Objects.requireNonNull(FileUtils.getPath(SupervisorDashboardActivity.this, selectedPhotoUri)));
                if (file.exists()) {
                    RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                    imagePart.add(body);

                }
                showProgressDialog(dialog);
                doPostMultipartBody(MARK_ATTENDANCE,imagePart,SupervisorDashboardActivity.this);
            }
        } else {
            selectedPhotoUri = null;
            showToast("Error, Try again selecting images");
        }


    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()){
            try {
                JSONObject obj=new JSONObject(response.body().string());
                showToast(obj.getString("message"));
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        }else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>",t.getMessage());
        showToast("Failed: Try again");
        cancelDialog(dialog);
    }
}