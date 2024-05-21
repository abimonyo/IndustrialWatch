package com.app.industrialwatch.app.module.ui.employee;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.app.module.ui.adapter.EmployeeAdapter;
import com.app.industrialwatch.app.module.ui.authentication.LoginActivity;
import com.app.industrialwatch.app.module.ui.supervisor.AddSupervisorActivity;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.base.BaseFragment;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.PicassoUtils;
import com.app.industrialwatch.databinding.FragmentEmployeeProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeProfileFragment extends BaseFragment implements Callback<ResponseBody>, View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FragmentEmployeeProfileBinding binding;
    Dialog dialog;
    Context context;


    public EmployeeProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeProfileFragment newInstance(String param1, String param2) {
        EmployeeProfileFragment fragment = new EmployeeProfileFragment();
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
        binding = FragmentEmployeeProfileBinding.inflate(inflater, container, false);
        binding.layoutLogout.setOnClickListener(this);
        setToolbarTitle(getString(R.string.profile));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        addEditButtonInToolbar();
        dialog = getProgressDialog(false);
        onAttach(this.getContext());
        showProgressDialog(dialog, this.getActivity());
        doGetRequest(AppConstants.GET_EMPLOYEE_PROFILE, getParams("employee_id", SharedPreferenceManager.getInstance().read("id", 0)+""), this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_EMPLOYEE_PROFILE)) {
                    JSONObject object = new JSONObject(response.body().string());
                    PicassoUtils.picassoLoadImageOrPlaceHolder(this.getContext(), binding.ivEmployeeProfile,
                            AppConstants.BASE_URL + AppConstants.IMAGE_URL + URLEncoder.encode(object.getString("image"), "UTF-8").replace("+", "%20"),
                            R.drawable.baseline_person_24, 150, 150);
                    binding.tvJobRole.setText(object.getString("job_role"));
                    binding.tvJobType.setText(object.getString("job_type"));
                    binding.tvName.setText(object.getString("name"));
                    binding.tvUsername.setText(object.getString("username"));
                    binding.tvSection.setText(object.getString("section"));

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

    @SuppressLint("ResourceType")
    private void addEditButtonInToolbar() {
        ImageButton editButton = new ImageButton(getContext());
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                Toolbar.LayoutParams.WRAP_CONTENT,
                Toolbar.LayoutParams.WRAP_CONTENT
        );
        params.gravity = android.view.Gravity.END;
        editButton.setLayoutParams(params);
        editButton.setId(13);
        editButton.setBackground(null);
        editButton.setImageResource(R.drawable.baseline_edit_24);
        if (getActivity() instanceof BaseActivity) {
            if (((BaseActivity) getActivity()).getToolbar().findViewById(13) == null)
                ((BaseActivity) getActivity()).getToolbar().addView(editButton);
            else if (((BaseActivity) getActivity()).getToolbar().findViewById(13).getVisibility() == 8) {
                ((BaseActivity) getActivity()).getToolbar().findViewById(13).setVisibility(View.VISIBLE);

            }
        }
        if (editButton != null) {
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString(AppConstants.FROM, "edit");
                        Intent intent = new Intent(context, AddSupervisorActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Log.d("error==>>", e.getMessage());
                    }
                }
            });
        }


    }

    @SuppressLint("ResourceType")
    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).getToolbar().findViewById(13).setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        if (v.getId() == binding.layoutLogout.getId()) {
            SharedPreferenceManager.setSingletonInstance(context);
            SharedPreferenceManager.getInstance().save(AppConstants.LOGIN_KEY, false);
            context.startActivity(new Intent(context, LoginActivity.class));
            this.getActivity().finish();
        }
    }
}