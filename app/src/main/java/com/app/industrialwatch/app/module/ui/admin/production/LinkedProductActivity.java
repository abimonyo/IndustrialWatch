package com.app.industrialwatch.app.module.ui.admin.production;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.BatchModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.AppUtils;
import com.app.industrialwatch.databinding.ActivityLinkedProductBinding;
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

public class LinkedProductActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, OnRecyclerViewItemClickListener, View.OnClickListener {

    ActivityLinkedProductBinding binding;
    ProductionAdapter adapter;
    List<BaseItem> linkedProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinkedProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setPrimaryActionBar(binding.includedLayout.sectionAppLayout.primaryToolbar, getString(R.string.product));
        initRecyclerView(binding.includedLayout.includedRcv.recyclerView);
        binding.includedLayout.btnAddSection.setText(getString(R.string.link_product));
        binding.includedLayout.btnAddSection.setOnClickListener(this);
        linkedProductList = new ArrayList<>();
        binding.includedLayout.includedRcv.headerLayout.layoutHeaderWrapper.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(adapter);
        doGetRequest(AppConstants.GET_LINK_PRODUCTS, this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                JSONArray array = new JSONArray(response.body().string());
                linkedProductList = new Gson().fromJson(array.toString(), new TypeToken<List<BatchModel>>() {
                }.getType());
                if (adapter == null) {
                    adapter = new ProductionAdapter(linkedProductList, this);
                    setAdapter(adapter);
                }
            } catch (IOException | JSONException e) {
                Log.d("error==>>", e.getMessage());
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());

    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        BatchModel model = (BatchModel) linkedProductList.get(holder.getLayoutPosition());
        showToast(model.getName());
        Bundle bundle=new Bundle();
        bundle.putString(AppConstants.KEY_NUMBER,model.getProduct_number());
        bundle.putString(AppConstants.BUNDLE_KEY,model.getName());
        startActivity(bundle, BatchActivity.class);
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(),LinkProductActivity.class));
    }
}