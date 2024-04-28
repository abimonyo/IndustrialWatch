package com.app.industrialwatch.app.module.ui.admin.section;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.data.models.StockModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.app.module.ui.adapter.SectionAdapter;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivitySectionBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionActivity extends BaseRecyclerViewActivity implements OnRecyclerViewItemClickListener, View.OnClickListener, Callback<ResponseBody> {

    ActivitySectionBinding binding;
    SectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();

    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);

        doGetRequest(AppConstants.SECTION_URL, this);

    }

    private void initView() {

        binding.btnAddSection.setOnClickListener(this);
        hideView(binding.includedRcv.headerLayout.layoutHeaderWrapper);
        setPrimaryActionBar(binding.sectionAppLayout.primaryToolbar, getString(R.string.section));
        initRecyclerView(binding.includedRcv.recyclerView);
    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        SectionModel model = (SectionModel) adapter.getItemAt(holder.getLayoutPosition());
        showToast(model.getSectionName());
        Bundle bundle = new Bundle();
        bundle.putInt("id", model.getId());
        startActivity(bundle, SectionDetailsActivity.class);
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnAddSection.getId()) {

            startActivity(new Intent(getApplicationContext(), AddUpdateSectionActivity.class));
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.SECTION_URL)) {

                    JSONArray jsonArray = new JSONArray(response.body().string());
                    List<BaseItem> sectionList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SectionModel>>() {
                    }.getType());
                    if (adapter == null) {
                        adapter = new SectionAdapter(sectionList, SectionActivity.this);
                        setAdapter(adapter);
                    }

                }
            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        } else {
            showErrorMessage(response);
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
    }
}