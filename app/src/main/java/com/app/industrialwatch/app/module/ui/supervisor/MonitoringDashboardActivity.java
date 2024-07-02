package com.app.industrialwatch.app.module.ui.supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.industrialwatch.R;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.databinding.ActivityMonitoringDashboardBinding;

public class MonitoringDashboardActivity extends BaseActivity {

    ActivityMonitoringDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMonitoringDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSecondaryActionBar(binding.includedToolbar.secondaryToolbar,getString(R.string.defect_monitoring),true);
        binding.singleDiscButton.setOnClickListener(v->{startActivity(new Intent(getApplicationContext(),MultipleAngleMonitoringActivity.class));});
        binding.batchButton.setOnClickListener(v->{startActivity(new Intent(getApplicationContext(),DefectsMonitoringActivity.class));});
    }
}