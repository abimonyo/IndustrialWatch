package com.app.industrialwatch.app.module.ui.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.databinding.ActivityEmployeeDashboardBinding;

public class EmployeeDashboardActivity extends BaseActivity implements View.OnClickListener {


    NavController controller;
    NavHostFragment navHostFragment;
    ActivityEmployeeDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        binding.fab.setOnClickListener(this);
        binding.includedToolbar.arrowButton.setVisibility(View.GONE);
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar,"");
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        controller = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigationView, controller);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.fab.getId()) {
            controller.navigate(R.id.employeeSummaryFragment);
        }
    }
}