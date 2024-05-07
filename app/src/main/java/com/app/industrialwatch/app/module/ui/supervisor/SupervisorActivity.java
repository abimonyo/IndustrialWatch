package com.app.industrialwatch.app.module.ui.supervisor;

import static com.app.industrialwatch.common.utils.AppConstants.GET_ALL_SUPERVISOR;
import static com.app.industrialwatch.common.utils.AppConstants.OK;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.data.models.SupervisorModel;
import com.app.industrialwatch.app.module.ui.adapter.SupervisorAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivitySupervisorBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupervisorActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, View.OnClickListener, OnRecyclerViewItemClickListener {

    ActivitySupervisorBinding binding;
    SupervisorAdapter adapter;
    Dialog dialog;
    List<BaseItem> superVisorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupervisorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initRecyclerView(binding.includedLayout.includedRcv.recyclerView);
        setPrimaryActionBar(binding.includedLayout.sectionAppLayout.primaryToolbar, getString(R.string.supervisor));
        binding.includedLayout.btnAddSection.setVisibility(View.GONE);
        binding.includedLayout.includedRcv.headerLayout.layoutHeaderWrapper.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(GET_ALL_SUPERVISOR, this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {

                JSONArray jsonArray = new JSONArray(response.body().string());
                 superVisorList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SupervisorModel>>() {
                }.getType());
                if (adapter == null) {
                    adapter = new SupervisorAdapter(superVisorList, this);
                    setAdapter(adapter);
                }

            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        } else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        showToast("Failed: Try again.");
        cancelDialog(dialog);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), AddSupervisorActivity.class));
    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        SupervisorModel model=(SupervisorModel) superVisorList.get(holder.getLayoutPosition());
        Bundle bundle=new Bundle();
        bundle.putInt(AppConstants.BUNDLE_KEY,model.getId());
        startActivity(bundle,AddSupervisorActivity.class);
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }
}