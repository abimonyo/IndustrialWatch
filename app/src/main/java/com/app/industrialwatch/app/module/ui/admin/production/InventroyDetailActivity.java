package com.app.industrialwatch.app.module.ui.admin.production;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.StockModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityInventroyDetailBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventroyDetailActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, OnRecyclerViewItemClickListener, View.OnClickListener {

    ActivityInventroyDetailBinding binding;
    Bundle bundle;
    ProductionAdapter productionAdapter;

    List<BaseItem> inventoryList;
    List<String> selectedStockList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventroyDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            if (bundle != null) {
                if (bundle.getString(AppConstants.FROM,"").equals("AddBatch")) {
                    binding.btnConfirm.setVisibility(View.VISIBLE);
                    binding.btnConfirm.setOnClickListener(this);
                }
            }
        }
        initView();
    }

    private void initView() {
        inventoryList = new ArrayList<>();
        selectedStockList = new ArrayList<>();
        initRecyclerView(binding.includedLayout.recyclerView);
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, bundle.getString("name")==null?"Stocks":bundle.getString("name"), this);
        setRecyclerViewHeader("Stock #", "Qty", "Price/kg", "Date");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (productionAdapter == null) {
            setAdapter(null);
            doGetRequest(AppConstants.GET_INVENTORY_DETAIL_BY_RAW_MATERIAL, getServerParams(), this);
        } else
            setAdapter(productionAdapter);
    }

    private Map<String, String> getServerParams() {
        Map<String, String> params = new HashMap<>();
        params.put("id", bundle.getInt(AppConstants.BUNDLE_KEY, 0) + "");
        return params;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                JSONArray jsonArray = new JSONArray(response.body().string());
                AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.ITEM_INVENTORY;
                if (bundle.getString(AppConstants.FROM, "").equals("AddBatch"))
                    AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.ITEM_INVENTORY_DETAIL;
                inventoryList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<StockModel>>() {
                }.getType());
                if (productionAdapter == null) {
                    productionAdapter = new ProductionAdapter(inventoryList, this);
                    setAdapter(productionAdapter);
                } else
                    productionAdapter.addAll(inventoryList);

            } catch (Exception e) {
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
        StockModel model = (StockModel) inventoryList.get(holder.getLayoutPosition());
        if (model.isChecked())
            selectedStockList.add(model.getStock_number());
        else
            selectedStockList.remove(model.getStock_number());
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }

    private void sendDataToPrevious() {
        try {
            Intent intent = new Intent();
            JSONArray array = new JSONArray(selectedStockList);
            JSONObject object = new JSONObject();
            object.put(bundle.getInt(AppConstants.BUNDLE_KEY, 0) + "", array);
            intent.putExtra("stringList", object.toString());
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (JSONException e) {
            Log.d("error==>>", e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.arrow_button)
            finish();
        else if (v.getId() == R.id.btn_confirm)
            sendDataToPrevious();
    }

}