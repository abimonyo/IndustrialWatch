package com.app.industrialwatch.app.module.ui.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.GridItemModel;
import com.app.industrialwatch.app.module.ui.adapter.GridItemAdapter;
import com.app.industrialwatch.app.module.ui.admin.production.InventoryActivity;
import com.app.industrialwatch.app.module.ui.admin.production.ProductActivity;
import com.app.industrialwatch.app.module.ui.admin.production.RawMaterialActivity;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityEmployeeProductivityDashboardBinding;

import java.util.ArrayList;

public class EmployeeProductivityDashboardActivity extends BaseActivity implements View.OnClickListener {

    ActivityEmployeeProductivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeProductivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.layoutIncluded.layoutHeader.ivLogout.setVisibility(View.GONE);
        binding.layoutIncluded.layoutHeader.dashboardTitle.setText(getString(R.string.employee_productivity));
        binding.layoutIncluded.layoutHeader.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.layoutIncluded.gridLayout.girdView.setAdapter(new GridItemAdapter(this, fillGridItems(), this));

    }

    private ArrayList<GridItemModel> fillGridItems() {
        ArrayList<GridItemModel> gridItemModels = new ArrayList<>();
        gridItemModels.add(new GridItemModel(getString(R.string.employee_record), R.drawable.ic_employee_record));
        gridItemModels.add(new GridItemModel(getString(R.string.add_employee), R.drawable.ic_add_employee));
        gridItemModels.add(new GridItemModel(getString(R.string.employee_ranking), R.drawable.ic_employee_ranking));
        return gridItemModels;

    }

    @Override
    public void onClick(View v) {
        if ((int) v.getTag() == 0) {
            startActivity(new Intent(getApplicationContext(), EmployeeRecordActivity.class));
        } else if ((int) v.getTag() == 1) {
            startActivity(new Intent(getApplicationContext(), AddEmployeeActivity.class));
        } else if ((int) v.getTag() == 2) {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.FROM, AppConstants.RANKING);
            startActivity(bundle, EmployeeRecordActivity.class);
        }
    }
}