package com.app.industrialwatch.app.module.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.app.module.ui.admin.main.AdminDashboardActivity;
import com.app.industrialwatch.app.module.ui.admin.section.SectionActivity;
import com.app.industrialwatch.app.module.ui.authentication.LoginActivity;
import com.app.industrialwatch.app.module.ui.employee.EmployeeDashboardActivity;
import com.app.industrialwatch.app.module.ui.supervisor.SupervisorDashboardActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivitySupervisorDashboardBinding;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        LoginActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferenceManager.setSingletonInstance(SplashActivity.this);
                if (!SharedPreferenceManager.getInstance().read(AppConstants.LOGIN_KEY,false))
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                else {
                    String role = SharedPreferenceManager.getInstance().read("user_role", "");
                    switch (role) {
                        case "Supervisor":
                            startActivity(new Intent(getApplicationContext(), SupervisorDashboardActivity.class));
                            break;
                        case "Employee":
                            startActivity(new Intent(getApplicationContext(), EmployeeDashboardActivity.class));
                            break;
                        default:
                            startActivity(new Intent(getApplicationContext(), AdminDashboardActivity.class));
                            break;

                    }
                }
                finish();
            }
        }, 3000);
    }
}