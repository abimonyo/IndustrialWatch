package com.app.industrialwatch.app.module.ui.admin.production;

import static com.app.industrialwatch.common.utils.AppConstants.GET_PRODUCT_FORMULA;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.RawMaterialModel;
import com.app.industrialwatch.app.data.models.StockModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.AppUtils;
import com.app.industrialwatch.common.utils.JsonUtil;
import com.app.industrialwatch.databinding.ActivityAddBatchBinding;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBatchActivity extends BaseRecyclerViewActivity implements View.OnClickListener, Callback<ResponseBody>, OnRecyclerViewItemClickListener {

    ActivityAddBatchBinding binding;
    Bundle bundle;
    ProductionAdapter adapter;

    List<BaseItem> formulaList;
    JSONArray stockList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBatchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null)
            bundle = getIntent().getExtras();
        initView();
    }


    private void initView() {
        setPrimaryActionBar(binding.toolbarLayout.primaryToolbar, bundle.getString(AppConstants.BUNDLE_KEY));
        initRecyclerView(binding.includedLayout.recyclerView);
        setRecyclerViewHeader("#", "Material", "Quantity", "");
        binding.btnAddBatch.setOnClickListener(this);
        formulaList = new ArrayList<>();
        stockList = new JSONArray();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(adapter);
        doGetRequest(GET_PRODUCT_FORMULA, getServerParam(), this);
    }

    private Map<String, String> getServerParam() {
        Map<String, String> params = new HashMap<>();
        params.put("product_number", bundle.getString(AppConstants.KEY_NUMBER));
        return params;
    }

    @Override
    public void onClick(View v) {
        if (!AppUtils.validateEmptyEditText(binding.etBachPerDay)) {
            binding.etBachPerDay.setError("Please enter value");
            binding.etBachPerDay.requestFocus();
            return;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("batch_per_day", Integer.parseInt(getValueFromField(binding.etBachPerDay)));
            object.put("product_number", bundle.getString(AppConstants.KEY_NUMBER));
            object.put("stock_list", JsonUtil.renameObjectsKeyInAllArray(stockList, "stocks"));
            Log.d("op==>>", object.toString());
            creatAndCallApi(object);
        } catch (JSONException e) {
            Log.d("error==>>", e.getMessage());
        }

    }

    private void creatAndCallApi(JSONObject object) {
        RequestBody body = RequestBody.create(object.toString(), MediaType.get("application/json; charset=utf-8"));
        doPostRequest(AppConstants.CREATE_BATCH, body, this);

    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_PRODUCT_FORMULA)) {
                    JSONArray array = new JSONArray(response.body().string());
                    AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.ITEM_INVENTORY;
                    formulaList = new Gson().fromJson(array.toString(), new TypeToken<List<StockModel>>() {
                    }.getType());
                    if (adapter == null) {
                        adapter = new ProductionAdapter(formulaList, this);
                        setAdapter(adapter);
                    }

                } else if (call.request().url().url().toString().contains(AppConstants.CREATE_BATCH)) {
                    JSONObject object = new JSONObject(response.body().string());
                    showToast(object.getString("message"));
                }
            } catch (IOException | JSONException e) {
                Log.d("error==>>", e.getMessage());
            }
        }else {
            try {
                JSONObject object = new JSONObject(response.errorBody().string());
                showToast(object.getString("message"));
            } catch (IOException |JSONException e) {
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
        StockModel model = (StockModel) formulaList.get(holder.getLayoutPosition());
        bundle.putInt(AppConstants.BUNDLE_KEY, model.getRaw_material_id());
        bundle.putString(AppConstants.FROM, "AddBatch");
        Intent intent = new Intent(getApplicationContext(), InventroyDetailActivity.class);
        intent.putExtras(bundle);
        startChildActivityLauncher.launch(intent);
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }

    private final ActivityResultLauncher<Intent> startChildActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("stringList")) {
                        try {
                            JSONObject object = new JSONObject(data.getStringExtra("stringList"));
                            String key = object.keys().next();
                            String id = bundle.getInt(AppConstants.BUNDLE_KEY, 0) + "";
                            insertToStock(object);
                            showToast(key + id);
                        } catch (JSONException e) {
                            Log.d("error==>>", e.getMessage());
                        }
                    }
                }
            }
    );

    private void insertToStock(JSONObject object) {
        String key = object.keys().next();
        try {
            for (int i = 0; i < stockList.length(); i++) {
                JSONObject arrObj = stockList.getJSONObject(i);
                String arrObjKey = arrObj.keys().next();
                if (arrObjKey.equals(key)) {
                    // Remove the existing object at index i
                    stockList.remove(i);
                    break; // Exit the loop after removing the object
                }
            }
        } catch (JSONException e) {
            Log.d("error==>>", e.getMessage());
        }
        // Add the new object to the end of the array
        stockList.put(object);
    }
}