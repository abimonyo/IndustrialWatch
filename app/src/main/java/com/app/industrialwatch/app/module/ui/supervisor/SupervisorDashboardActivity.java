package com.app.industrialwatch.app.module.ui.supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.GridItemModel;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.app.module.ui.adapter.GridItemAdapter;
import com.app.industrialwatch.app.module.ui.admin.section.SectionActivity;
import com.app.industrialwatch.app.module.ui.employee.EmployeeAttendanceActivity;
import com.app.industrialwatch.app.module.ui.employee.EmployeeProductivityDashboardActivity;
import com.app.industrialwatch.databinding.ActivitySupervisorDashboardBinding;

import java.util.ArrayList;

public class SupervisorDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySupervisorDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySupervisorDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }
    private void initView(){
        binding.includedLayout.layoutIncluded.dashboardTitle.setText(R.string.supervisor_dashboard);
        binding.includedLayout.layoutIncluded.ivBack.setVisibility(View.GONE);
        binding.includedLayout.tvName.setText(SharedPreferenceManager.getInstance().read("name","Name"));
        binding.includedLayout.gridLayout.girdView.setAdapter(new GridItemAdapter(this, fillGridItems(), this));

    }
    private ArrayList<GridItemModel> fillGridItems() {
        ArrayList<GridItemModel> gridItemModels = new ArrayList<>();
        gridItemModels.add(new GridItemModel(getString(R.string.employee_monitoring), R.drawable.img_emoloyee_monitoring));
        gridItemModels.add(new GridItemModel(getString(R.string.defect_monitoring), R.drawable.img_defect_monitoring));
        gridItemModels.add(new GridItemModel(getString(R.string.my_attendance), R.drawable.img_attendance));
        return gridItemModels;
    }

    @Override
    public void onClick(View v) {
        if ((int) v.getTag() == 0) {
            startActivity(new Intent(getApplicationContext(), SectionActivity.class));
        } else if ((int) v.getTag() == 1) {
            startActivity(new Intent(getApplicationContext(), SupervisorActivity.class));
        } else if ((int) v.getTag() == 2) {
            startActivity(new Intent(getApplicationContext(), EmployeeAttendanceActivity.class));
        }
    }
}