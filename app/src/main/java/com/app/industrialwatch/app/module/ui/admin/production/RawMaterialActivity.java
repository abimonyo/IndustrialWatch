package com.app.industrialwatch.app.module.ui.admin.production;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.business.MyJSONObject;
import com.app.industrialwatch.app.data.models.RawMaterialModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityRawMaterialBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RawMaterialActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, View.OnClickListener {
    ActivityRawMaterialBinding binding;
    ProductionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRawMaterialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initRecyclerView(binding.includedLayout.includedRcv.recyclerView);
    }

    private void initView() {
        setPrimaryActionBar(binding.includedLayout.sectionAppLayout.primaryToolbar, getString(R.string.raw_materials));
        binding.includedLayout.btnAddSection.setText(getString(R.string.add_material));
        binding.includedLayout.btnAddSection.setOnClickListener(this);
        hideView(binding.includedLayout.includedRcv.headerLayout.layoutHeaderWrapper);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);
        doGetRequest(AppConstants.GET_ALL_RAW_MATERIAL, this);
    }

    @SuppressLint("MissingInflatedId")
    private void showCreateBatchDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RawMaterialActivity.this);
        View customLayout = getLayoutInflater().inflate(R.layout.layout_dialogue_raw_material, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customLayout.findViewById(R.id.btn_add_raw_material).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                psotRawMaterial(getValueFromField((EditText) dialog.findViewById(R.id.et_raw_material)));
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

    private void psotRawMaterial(String valueFromField) {
        doPostRequest(AppConstants.INSERT_RAW_MATERIAL, getServerParams(valueFromField), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        showToast(jsonObject.getString("message"));
                        doGetRequest(AppConstants.GET_ALL_RAW_MATERIAL, this);


                    } catch (IOException | JSONException e) {
                        Log.d("error==>>", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("error==>>", t.getMessage());
            }
        });
    }

    private Map<String, String> getServerParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                // JSONObject object=new JSONObject(response.body().string());
                JSONArray array = new JSONArray(response.body().string());
                List<BaseItem> rawMaterialList = new Gson().fromJson(array.toString(), new TypeToken<List<RawMaterialModel>>() {
                }.getType());
                if (adapter != null)
                    adapter.clearItems();
                adapter = new ProductionAdapter(rawMaterialList, null);
                setAdapter(adapter);
                if (rawMaterialList.size() > 0) {
                    adapter.clearItems();
                    adapter.addAll(rawMaterialList);
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
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_section) {
            showCreateBatchDialogue();
        }
    }
}