package com.app.industrialwatch.app.module.ui.admin.production;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.RawMaterialModel;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.data.models.StockModel;
import com.app.industrialwatch.app.module.ui.adapter.ItemCheckBoxAdapter;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityProductBinding;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends BaseRecyclerViewActivity implements View.OnClickListener, Callback<ResponseBody>, AdapterView.OnItemSelectedListener {

    ActivityProductBinding binding;
    List<RawMaterialModel> rawMaterialModelsList;

    List<String> checkedAnglesList;
    Map<String, Object> rawMap;
    List<Map<String, Object>> selectedRawMaterilList;
    ProductionAdapter adapter;
    List<BaseItem> rawBaseList;
    String selectedMaterialName;
    Dialog dialog;
    List<BaseItem> anglesList ;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSecondaryActionBar(binding.layoutToolbar.secondaryToolbar, true, this);
        binding.button.setOnClickListener(this);
        setAnglesSpinner();
        selectedRawMaterilList = new ArrayList<>();
        rawBaseList = new ArrayList<>();
        setRecyclerViewHeader("#", "Material", "Quantity", "");
        initRecyclerView(binding.includedLayout.recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(AppConstants.GET_ALL_RAW_MATERIAL, this);
    }

    private void setAnglesSpinner() {
        rawMap = new HashMap<>();
        checkedAnglesList = new ArrayList<>();
         anglesList = new ArrayList<>();
        anglesList.add(new SectionModel(1, "1 Top"));
        anglesList.add(new SectionModel(2, "2 Flip"));
        anglesList.add(new SectionModel(3, "3 Right"));
        anglesList.add(new SectionModel(4, "4 Left"));
        anglesList.add(new SectionModel(5, "5 Front"));
        anglesList.add(new SectionModel(6, "6 Back"));
        ItemCheckBoxAdapter adapter = new ItemCheckBoxAdapter(this, anglesList, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = (String) v.getTag();
                if (Boolean.parseBoolean(result.split(",")[0])) {
                    checkAndInsertAngle(result.split(",")[1]);
                } else {
                    checkedAnglesList.remove(result.split(",")[1]);
                }
            }
        });
        binding.spAngles.setAdapter(adapter);
    }
    private void checkAndInsertAngle(String name) {
        if (checkedAnglesList.size()>0) {
            for (int i = 0; i < checkedAnglesList.size(); i++) {
                if (checkedAnglesList.get(i).equals(name))
                    return;

            }

        }
        checkedAnglesList.add(name);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_secondary_toolbar) {
            showAddMaterialDialogue();
        } else if (v.getId() == R.id.button) {
            String name = getValueFromField(binding.etName);
            JSONObject requestJson = new JSONObject();
            try {
                requestJson.put("name", name);
                requestJson.put("inspection_angles", String.join(", ", checkedAnglesList));
                JSONArray jsonArray = new JSONArray();
                for (Map<String, Object> map : selectedRawMaterilList) {
                    JSONObject jsonObject = new JSONObject(map);
                    jsonArray.put(jsonObject);
                }
                requestJson.put("materials", jsonArray);

                RequestBody body = RequestBody.create(requestJson.toString(), MediaType.get("application/json; charset=utf-8"));
                doPostRequest(AppConstants.INSERT_PRODUCT, body, this);
            } catch (JSONException e) {
                Log.d("error==>>", e.getMessage());
                showToast("error processing with request");
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private void showAddMaterialDialogue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
        View customLayout = getLayoutInflater().inflate(R.layout.layout_dialoge_new_material, null);
        customLayout.findViewById(R.id.layout_wrapper_price).setVisibility(View.GONE);
        if (rawMaterialModelsList != null && rawMaterialModelsList.size() > 0) {
            Spinner spinner = customLayout.findViewById(R.id.sp_name);
            spinner.setSelected(false);  // must
            spinner.setSelection(0, true);  //must
            spinner.setOnItemSelectedListener(this);
            ArrayAdapter<RawMaterialModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rawMaterialModelsList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
        Spinner spinnerUnit = customLayout.findViewById(R.id.sp_unit);
        spinnerUnit.setSelected(false);
        spinnerUnit.setSelection(0, true);
        spinnerUnit.setOnItemSelectedListener(this);
        spinnerUnit.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.unit_list)));
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customLayout.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rawMap.put("quantity", Integer.parseInt(getValueFromField(customLayout.findViewById(R.id.et_quantity))));
                Map<String, Object> newRawMap = new HashMap<>();
                newRawMap.put("name", rawMap.get("name"));
                newRawMap.put("raw_material_id", rawMap.get("raw_material_id"));
                newRawMap.put("quantity", Integer.parseInt(getValueFromField(customLayout.findViewById(R.id.et_quantity))));
                newRawMap.put("unit", rawMap.get("unit"));
                selectedRawMaterilList.add(newRawMap);
                StockModel model = new StockModel();
                model.setRaw_material_name((String) rawMap.get("name"));
                model.setTotal_quantity(getValueFromField(customLayout.findViewById(R.id.et_quantity)));
                rawBaseList.add(model);
                AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.ITEM_INVENTORY;
                adapter = new ProductionAdapter(rawBaseList, null);
                setAdapter(adapter);
                rawMap.clear();
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
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_ALL_RAW_MATERIAL)) {
                    JSONArray array = new JSONArray(response.body().string());
                    rawMaterialModelsList = new Gson().fromJson(array.toString(), new TypeToken<List<RawMaterialModel>>() {
                    }.getType());
                } else if (call.request().url().url().toString().contains(AppConstants.INSERT_PRODUCT)) {
                    JSONObject object = new JSONObject(response.body().string());
                    showToast(object.getString("message"));
                    finish();
                }

            } catch (IOException | JSONException e) {
                Log.d("error==>>", e.getMessage());
            }
        } else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("erro==>>", t.getMessage());
        cancelDialog(dialog);
        showToast("Failed: Try again.");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.sp_name) {
            rawMap.put("name", rawMaterialModelsList.get(position).getName());
            rawMap.put("raw_material_id", rawMaterialModelsList.get(position).getId());
        } else if (spinner.getId() == R.id.sp_unit) {
            rawMap.put("unit", spinner.getSelectedItem().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}