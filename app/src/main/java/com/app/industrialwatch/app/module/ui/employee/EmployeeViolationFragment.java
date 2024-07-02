package com.app.industrialwatch.app.module.ui.employee;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.ViolationModel;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.app.module.ui.adapter.EmployeeAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewFragment;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.FragmentEmployeeViolationBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeViolationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeViolationFragment extends BaseRecyclerViewFragment implements Callback<ResponseBody>, OnRecyclerViewItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FragmentEmployeeViolationBinding binding;
    Dialog dialog;
    EmployeeAdapter adapter;
    List<BaseItem> violationModelList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmployeeViolationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmployeeViolationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployeeViolationFragment newInstance(String param1, String param2) {
        EmployeeViolationFragment fragment = new EmployeeViolationFragment();
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
        binding=FragmentEmployeeViolationBinding.inflate(inflater, container, false);
        initRecyclerView(binding.includedRcv.recyclerView);
        binding.includedRcv.headerLayout.layoutHeaderWrapper.setVisibility(View.GONE);
        setToolbarTitle(getString(R.string.violations));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter=null;
        setAdapter(null);
        dialog = getProgressDialog(false);
        showProgressDialog(dialog,this.getActivity());
        doGetRequest(AppConstants.GET_ALL_VIOLATIONS, getParams("employee_id", SharedPreferenceManager.getInstance().read("id", 0)+""), this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_ALL_VIOLATIONS)) {
                    JSONArray array = new JSONArray(response.body().string());
                    violationModelList=new Gson().fromJson(array.toString(),new TypeToken<List<ViolationModel>>(){}.getType());
                    if (adapter==null){
                        adapter=new EmployeeAdapter(violationModelList,this,this.getContext()
                        );
                        setAdapter(adapter);
                    }else {
                        adapter.clearItems();
                        adapter.addAll(violationModelList);
                    }

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

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        ViolationModel model1=(ViolationModel) violationModelList.get(holder.getLayoutPosition());
        Bundle bundle1=new Bundle();
        bundle1.putInt(AppConstants.BUNDLE_KEY,model1.getViolationId());
        Intent intent=new Intent(getContext(), ViolationDetailActivity.class);
        intent.putExtras(bundle1);
        getContext().startActivity(intent);
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }
}