package com.app.industrialwatch.app.module.ui.admin.production;

import static com.app.industrialwatch.common.utils.AppConstants.GET_ALL_INVENTORY;
import static com.app.industrialwatch.common.utils.AppConstants.GET_ALL_PRODUCT;
import static com.app.industrialwatch.common.utils.AppConstants.GET_ALL_RAW_MATERIAL;
import static com.app.industrialwatch.common.utils.AppConstants.INSERT_STOCK;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.BatchModel;
import com.app.industrialwatch.app.data.models.RawMaterialModel;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.data.models.StockModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.app.module.ui.admin.section.SectionActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityInventoryBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, OnRecyclerViewItemClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    ActivityInventoryBinding binding;
    ProductionAdapter adapter;
    List<RawMaterialModel> rawMaterialModelsList;
    int selectedRawMaterialId;
    String selectedUnit;
    List<BaseItem> inventoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setPrimaryActionBar(binding.includedLayout.sectionAppLayout.primaryToolbar, getString(R.string.inventory));
        initRecyclerView(binding.includedLayout.includedRcv.recyclerView);
        setRecyclerViewDivider();
        initView();
    }

    private void initView() {
        setRecyclerViewHeader("#", "Material", "Quantity", "");
        binding.includedLayout.btnAddSection.setText(getString(R.string.add_stock));
        binding.includedLayout.btnAddSection.setOnClickListener(this);
        rawMaterialModelsList = new ArrayList<>();
        inventoryList = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);
        doGetRequest(GET_ALL_INVENTORY, this);
        doGetRequest(GET_ALL_RAW_MATERIAL, this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(GET_ALL_INVENTORY)) {
                    JSONArray array = new JSONArray(response.body().string());
                    AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM=BaseItem.ITEM_INVENTORY;
                    inventoryList = new Gson().fromJson(array.toString(), new TypeToken<List<StockModel>>() {
                    }.getType());
                    if (adapter == null) {
                        adapter = new ProductionAdapter(inventoryList, this);
                        setAdapter(adapter);
                    } else {
                        adapter.clearItems();
                        adapter.addAll(inventoryList);
                    }

                } else if (call.request().url().url().toString().contains(GET_ALL_RAW_MATERIAL)) {

                    JSONArray array = new JSONArray(response.body().string());
                    rawMaterialModelsList = new Gson().fromJson(array.toString(), new TypeToken<List<RawMaterialModel>>() {
                    }.getType());

                } else if (call.request().url().url().toString().contains(INSERT_STOCK)) {
                    JSONObject object = new JSONObject(response.body().string());
                    showToast(object.getString("message"));
                    doGetRequest(GET_ALL_INVENTORY, this);

                }
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        if (inventoryList.size() > 0) {
            StockModel model = (StockModel) inventoryList.get(holder.getLayoutPosition());
            Bundle bundle = new Bundle();
            bundle.putString("name", model.getRaw_material_name());
            bundle.putInt(AppConstants.BUNDLE_KEY, model.getRaw_material_id());
            startActivity(bundle, InventroyDetailActivity.class);
        }
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }

    @Override
    public void onClick(View v) {
        if (v == binding.includedLayout.btnAddSection) {
            showAddStockDialogue();
        }
    }

    private void showAddStockDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(InventoryActivity.this);
        View customLayout = getLayoutInflater().inflate(R.layout.layout_dialoge_new_material, null);
        customLayout.findViewById(R.id.sp_unit).setVisibility(View.GONE);
        //customLayout.findTextViewById(R.id.tv_quantity).setText()
        if (rawMaterialModelsList != null && rawMaterialModelsList.size() > 0) {
            Spinner spinner = customLayout.findViewById(R.id.sp_name);
            spinner.setSelected(false);  // must
            spinner.setSelection(0, true);  //must
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter<RawMaterialModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rawMaterialModelsList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customLayout.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRawMaterialId != 0) {
                    JSONObject object = new JSONObject();
                    try {
                        object.put("raw_material_id", selectedRawMaterialId);
                        object.put("quantity", Integer.parseInt(getValueFromField(customLayout.findViewById(R.id.et_quantity))));
                        object.put("price_per_kg", Integer.parseInt(getValueFromField(customLayout.findViewById(R.id.et_price))));
                        RequestBody body = RequestBody.create(object.toString(), MediaType.get("application/json; charset=utf-8"));

                        doPostRequest(INSERT_STOCK, body, InventoryActivity.this);
                    } catch (JSONException e) {
                        Log.d("error==>>", e.getMessage());
                    }
                }
                dialog.dismiss();
            }
        });
        customLayout.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.sp_name) {
            selectedRawMaterialId = rawMaterialModelsList.get(position).getId();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}