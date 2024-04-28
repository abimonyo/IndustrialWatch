package com.app.industrialwatch.app.module.ui.admin.production;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.BatchModel;
import com.app.industrialwatch.app.data.models.RawMaterialModel;
import com.app.industrialwatch.app.network.RetrofitClient;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.AppUtils;
import com.app.industrialwatch.databinding.ActivityLinkProductBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.Key;
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

public class LinkProductActivity extends BaseActivity implements View.OnClickListener, Callback<ResponseBody>, AdapterView.OnItemSelectedListener {

    ActivityLinkProductBinding binding;
    List<BatchModel> productList;
    String selectedProductNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinkProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        setPrimaryActionBar(binding.layoutToolbar.primaryToolbar, getString(R.string.link_product));
        binding.btnSave.setOnClickListener(this);
        binding.spProduct.setOnItemSelectedListener(this);
        productList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetRequest(AppConstants.UNLINK_PRODUCT, this);
    }

    @Override
    public void onClick(View v) {
        if (!AppUtils.validateEmptyEditText(binding.etPacksBatch)) {
            binding.etPacksBatch.requestFocus();
            binding.etPacksBatch.setError("Please enter value");
            return;
        }
        if (!AppUtils.validateEmptyEditText(binding.etPacksPack)) {
            binding.etPacksPack.requestFocus();
            binding.etPacksPack.setError("Please enter value");
            return;
        }
        if (!AppUtils.validateEmptyEditText(binding.etRejTolerance)) {
            binding.etRejTolerance.requestFocus();
            binding.etRejTolerance.setError("Please enter value");
            return;
        }
        JSONObject object = new JSONObject();
        try {
            object.put("product_number", selectedProductNumber);
            object.put("packs_per_batch", Integer.parseInt(getValueFromField(binding.etPacksBatch)));
            object.put("piece_per_pack", Integer.parseInt(getValueFromField(binding.etPacksPack)));
            object.put("rejection_tolerance", Float.parseFloat(getValueFromField(binding.etRejTolerance)));
            RequestBody body = RequestBody.create(object.toString(), MediaType.get("application/json; charset=utf-8"));
            doPostRequest(AppConstants.LINK_PRODUCT, body, this);

        } catch (JSONException e) {
            Log.d("error==>>", e.getMessage());
        }


    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.UNLINK_PRODUCT)) {
                    JSONArray array = new JSONArray(response.body().string());
                    productList = new Gson().fromJson(array.toString(), new TypeToken<List<BatchModel>>() {
                    }.getType());
                    setSpinnerAdapter(productList);
                } else {
                    JSONObject object = new JSONObject(response.body().string());
                    showToast(object.getString("message"));
                    finish();
                }
            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        }
    }

    private void setSpinnerAdapter(List<BatchModel> batchModels) {
        ArrayAdapter<BatchModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, batchModels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spProduct.setAdapter(adapter);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (productList.size() > 0) {
            selectedProductNumber = productList.get(position).getProduct_number();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}