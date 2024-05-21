package com.app.industrialwatch.app.module.ui.admin.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.GridItemModel;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.app.module.ui.adapter.GridItemAdapter;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.app.module.ui.admin.production.ProductionActivity;
import com.app.industrialwatch.app.module.ui.admin.production.ProductionDashboardActivity;
import com.app.industrialwatch.app.module.ui.admin.section.SectionActivity;
import com.app.industrialwatch.app.module.ui.employee.EmployeeProductivityDashboardActivity;
import com.app.industrialwatch.app.module.ui.supervisor.SupervisorActivity;
import com.app.industrialwatch.databinding.ActivityAdminDashboardBinding;

import java.util.ArrayList;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAdminDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.layoutIncluded.ivBack.setVisibility(View.GONE);
        initGridView();
    }

    private void initGridView() {
        binding.gridLayout.girdView.setAdapter(new GridItemAdapter(this, fillGridItems(), this));
        binding.tvName.setText(SharedPreferenceManager.getInstance().read("name",""));

    }

    private ArrayList<GridItemModel> fillGridItems() {
        ArrayList<GridItemModel> gridItemModels = new ArrayList<>();
        gridItemModels.add(new GridItemModel(getString(R.string.section), R.drawable.img_home));
        gridItemModels.add(new GridItemModel(getString(R.string.supervisor), R.drawable.img_supervisor));
        gridItemModels.add(new GridItemModel(getString(R.string.employee_productivity), R.drawable.img_emp_prod));
        gridItemModels.add(new GridItemModel(getString(R.string.production), R.drawable.img_production));
        return gridItemModels;
    }

    @Override
    public void onClick(View v) {
        if ((int) v.getTag() == 0) {
            startActivity(new Intent(getApplicationContext(), SectionActivity.class));
        } else if ((int) v.getTag() == 1) {
            startActivity(new Intent(getApplicationContext(), SupervisorActivity.class));
        } else if ((int) v.getTag() == 2) {
            startActivity(new Intent(getApplicationContext(), EmployeeProductivityDashboardActivity.class));
        } else if ((int) v.getTag() == 3) {
            startActivity(new Intent(getApplicationContext(), ProductionDashboardActivity.class));
        }
    }
}