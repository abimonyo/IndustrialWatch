package com.app.industrialwatch.app.module.ui.employee;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.base.BaseFragment;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.FragmentEmployeeHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeHomeFragment extends BaseFragment implements Callback<ResponseBody> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentEmployeeHomeBinding binding;
    Dialog dialog;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmployeeHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeHomeFragment newInstance(String param1, String param2) {
        EmployeeHomeFragment fragment = new EmployeeHomeFragment();
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

        binding = FragmentEmployeeHomeBinding.inflate(inflater, container, false);
        setToolbarTitle(SharedPreferenceManager.getInstance().read("name",""));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        dialog = getProgressDialog(false);
        showProgressDialog(dialog, this.getActivity());
        doGetRequest(AppConstants.GET_EMPLOYEE_DETAIL, getParams("employee_id", SharedPreferenceManager.getInstance().read("id", 0)+""), this);

    }

    public Map<String, String> getParams(String key, String value) {
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        return params;
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
                    binding.tvTotalAttendance.setText(object.getString("total_attendance"));
                }
            } catch (IOException | JSONException e) {
                Log.d("error==>>", e.getMessage());
            }

        } else
            showErrorMessage(response);
        cancelDialog(dialog, this.getActivity());
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        showToast("Failed, Try again.");
        cancelDialog(dialog, this.getActivity());
    }
}