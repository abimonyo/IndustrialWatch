package com.app.industrialwatch.app.module.ui.employee;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.common.base.BaseFragment;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.CalendarUtils;
import com.app.industrialwatch.databinding.FragmentEmployeeAttendanceBinding;
import com.app.industrialwatch.databinding.FragmentEmployeeSummaryBinding;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeSummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeSummaryFragment extends BaseFragment implements View.OnClickListener, Callback<ResponseBody> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Dialog dialog;
    FragmentEmployeeSummaryBinding binding;

    public EmployeeSummaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeSummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeSummaryFragment newInstance(String param1, String param2) {
        EmployeeSummaryFragment fragment = new EmployeeSummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmployeeSummaryBinding.inflate(inflater, container, false);
        binding.includedLayout.tvYear.setOnClickListener(this);
        binding.includedLayout.tvMonth.setOnClickListener(this);
        binding.includedLayout.tvSummary.setVisibility(View.GONE);
        setToolbarTitle(getString(R.string.summary));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = getProgressDialog(false);
        showProgressDialog(dialog, this.getActivity());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        binding.includedLayout.tvYear.setText(currentYear + "");
        binding.includedLayout.tvMonth.setText(CalendarUtils.getCurrentMonthShort());
        doGetRequest(AppConstants.GET_EMPLOYEE_SUMMARY, getMultipleParams((currentMonth + 1) + "," + currentYear), this);

    }

    private Map<String, String> getMultipleParams(String date) {
        Map<String, String> params = new HashMap<>();
        params.put("employee_id", SharedPreferenceManager.getInstance().read("id", 0)+"");
        params.put("date", date);
        return params;

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
        cancelDialog(dialog,this.getActivity());
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        showToast("Failed, Try again.");
        cancelDialog(dialog,this.getActivity());
    }

    private void showYearMenu() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Context wrapper = new ContextThemeWrapper(this.getActivity(), R.style.PopupStyle);
        PopupMenu popup = new PopupMenu(wrapper, binding.includedLayout.tvYear);
        for (int i = 2015; i <= currentYear; i++) {
            popup.getMenu().add(0, i, i, String.valueOf(i));
        }
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                ((TextView) binding.getRoot().findViewById(R.id.tv_year)).setText(item.getTitle().toString());
                showProgressDialog(dialog, EmployeeSummaryFragment.this.getActivity());
                doGetRequest(AppConstants.GET_EMPLOYEE_SUMMARY, getMultipleParams(binding.includedLayout.tvMonth.getTag().toString() + "," + item.getTitle().toString()), EmployeeSummaryFragment.this);

                return true;
            }
        });
        popup.show(); //showing popup menu
    }

    private void showMonthMenu() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] months = CalendarUtils.getMonthNames();
        Context wrapper = new ContextThemeWrapper(this.getActivity(), R.style.PopupStyle);
        PopupMenu popup = new PopupMenu(wrapper, binding.includedLayout.tvMonth);

        for (int i = 0; i < months.length; i++) {
            popup.getMenu().add(0, i + 1, i, months[i]);
        }
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                binding.includedLayout.tvMonth.setTag(item.getItemId());
                showProgressDialog(dialog, EmployeeSummaryFragment.this.getActivity());
                doGetRequest(AppConstants.GET_EMPLOYEE_SUMMARY, getMultipleParams(item.getItemId() + "," + binding.includedLayout.tvYear.getText().toString().trim()), EmployeeSummaryFragment.this);
                ((TextView) binding.getRoot().findViewById(R.id.tv_month)).setText(item.getTitle().toString());

                return true;
            }
        });
        popup.show(); //showing popup menu

    }

}