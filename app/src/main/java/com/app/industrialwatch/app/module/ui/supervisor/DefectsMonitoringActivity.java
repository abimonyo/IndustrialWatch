package com.app.industrialwatch.app.module.ui.supervisor;

import static com.app.industrialwatch.common.utils.AppConstants.DEFECT_MONITORING;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.BatchModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.common.base.TakePhotoActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.FileUtils;
import com.app.industrialwatch.databinding.ActivityDefectsMonitoringBinding;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefectsMonitoringActivity extends TakePhotoActivity implements Callback<ResponseBody>, AdapterView.OnItemSelectedListener, View.OnClickListener {

    ActivityDefectsMonitoringBinding binding;
    Dialog dialog;
    Bundle bundle;
    ClipData clipData;
    List<BaseItem> linkedProductList;
    List<BaseItem> bacthesProductList;
    String selectedProductNumber,selectBatchNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDefectsMonitoringBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }
    private void initView(){
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar,"Defect Monitoring");

        linkedProductList=new ArrayList<>();
        bacthesProductList=new ArrayList<>();
        linkedProductList.add(new BatchModel("Select Product Number"));
        bacthesProductList.add(new BatchModel("Select Batch"));
        binding.spPrdouct.setOnItemSelectedListener(this);
        binding.spBatch.setOnItemSelectedListener(this);
        setSpinnerAdapter(linkedProductList, binding.spPrdouct, this);
        setSpinnerAdapter(bacthesProductList, binding.spBatch, this);
        binding.btnStartDetecting.setOnClickListener(this);
        initializeImagePicker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(AppConstants.GET_LINK_PRODUCTS, this);
       // doGetRequest(AppConstants.GET_ALL_BATCHES, this);
    }

    private void makeDefectsPredictionApiCall() {
        if (clipData != null) {
            List<MultipartBody.Part> imagePart = new ArrayList<>();
            Map<String, RequestBody> bodyMap = new HashMap<>();

            for (int i = 0; i < clipData.getItemCount(); i++) {
                File file = new File(Objects.requireNonNull(FileUtils.getPath(DefectsMonitoringActivity.this, clipData.getItemAt(i).getUri())));
                if (file.exists()) {
                    RequestBody requestFile = RequestBody.create(file, MediaType.parse("image/*"));
                    MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);
                    imagePart.add(body);

                }
            }
            RequestBody batchBody = RequestBody.create(selectBatchNumber, MediaType.parse("text/plain"));
            RequestBody productBody = RequestBody.create(selectedProductNumber, MediaType.parse("text/plain"));
            bodyMap.put("product_number", productBody);
            bodyMap.put("batch_number", batchBody);
           // showProgressDialog(dialog);
            showToast("Processing Start");
            doPostRequestWithMapBody(DEFECT_MONITORING, bodyMap, imagePart, this);
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            if (response.isSuccessful()) {
                if (call.request().url().url().toString().contains(AppConstants.GET_LINK_PRODUCTS)) {
                    JSONArray array = new JSONArray(response.body().string());
                    linkedProductList.addAll(new Gson().fromJson(array.toString(), new TypeToken<List<BatchModel>>() {
                    }.getType()));

                } else if (call.request().url().url().toString().contains(AppConstants.GET_ALL_BATCHES)) {
                    JSONArray array = new JSONArray(response.body().string());
                    List<BaseItem> filteredList=new Gson().fromJson(array.toString(), new TypeToken<List<BatchModel>>() {
                    }.getType());
//                     bacthesProductList.addAll( filteredList.stream()
//                            .filter(batch -> batch.getStatus() == 0)
//                            .collect(Collectors.toList()));
                    bacthesProductList.addAll(filteredList);

                } else if (call.request().url().url().toString().contains(DEFECT_MONITORING)) {
                    JSONObject object=new JSONObject(response.body().string());
                    binding.tvTotalPiece.setText(object.getInt("total_items")+"");
                    binding.tvDefectedPiece.setText(object.getInt("total_defected_items")+"");
                    JSONArray array=object.getJSONArray("defects");
                    /*binding.tvCasting.setText(array.getJSONObject(0).getInt("casting")+"");
                    binding.tvMilling.setText(array.getJSONObject(1).getInt("milling")+"");
                    binding.tvTooling.setText(array.getJSONObject(2).getInt("tooling")+"");
                    */setData(array);
//                    dialog.dismiss();

                }
            }else
                showErrorMessage(response);
            cancelDialog(dialog);
        } catch (IOException | JSONException e) {
            Log.d("error==>>", e.getMessage());
            cancelDialog(dialog);
        }

    }

    private void setData(JSONArray defectsArray) throws JSONException {
        // Get defects array

// Initialize variables to store defect counts
        int castingCount = 0;
        int millingCount = 0;
        int toolingCount = 0;
        int capCount = 0;
        int labelCount = 0;

// Iterate through the defects array and accumulate counts based on key names
        for (int i = 0; i < defectsArray.length(); i++) {
            JSONObject defectObj = defectsArray.getJSONObject(i);

            // Check for each possible key and accumulate counts
            if (defectObj.has("casting")) {
                castingCount = defectObj.getInt("casting");
                binding.tvCasting.setText(castingCount + "");
            } else if (defectObj.has("milling")) {
                millingCount = defectObj.getInt("milling");
                binding.tvMilling.setText(millingCount + "");

            } else if (defectObj.has("tooling")) {
                toolingCount = defectObj.getInt("tooling");
                binding.tvTooling.setText(toolingCount + "");

            } else if (defectObj.has("cap")) {
                capCount = defectObj.getInt("cap");
                binding.tvCasting.setText(capCount + "");
                binding.tvCastingHint.setText("Cap");
                binding.tvToolingHint.setText("--");
            } else if (defectObj.has("label")) {
                labelCount = defectObj.getInt("label");
                binding.tvMilling.setText(labelCount + "");
                binding.tvMillingHint.setText("Label");
                binding.tvToolingHint.setText("--");

            }else if (defectObj.has("hole")){
                labelCount = defectObj.getInt("hole");
                binding.tvMillingHint.setText("hole");
                binding.tvMilling.setText(labelCount+"");
                binding.tvToolingHint.setText("--");

            } else if (defectObj.has("yarn defect")){
                labelCount = defectObj.getInt("yarn defect");
                binding.tvCastingHint.setText("yarn defect");
                binding.tvCasting.setText(labelCount+"");
                binding.tvToolingHint.setText("--");

            }
            /*else {
                binding.tvToolingHint.setText("--");
                binding.tvMillingHint.setText("--");
                binding.tvCastingHint.setText("--");
            }*/
        }

// Set the accumulated counts to respective TextViews
      /*  binding.tvCasting.setText(castingCount + "");
        binding.tvMilling.setText(millingCount + "");
        binding.tvTooling.setText(toolingCount + "");
        binding.tvCap.setText(capCount + "");
        binding.tvLabel.setText(labelCount + "");*/
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
    showToast("Failed: Try Again");
    Log.d("error==>>",t.getMessage());
    cancelDialog(dialog);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == binding.spPrdouct.getId()) {
            if (linkedProductList != null && linkedProductList.size() > 0 && position != 0) {
                BatchModel model=(BatchModel) linkedProductList.get(position);
                selectedProductNumber = model.getProduct_number();
                showProgressDialog(dialog);
                doGetRequest(AppConstants.GET_ALL_BATCHES, getParams("product_number",selectedProductNumber),this);
            }
        } else if (bacthesProductList != null && bacthesProductList.size() > 0 && position != 0) {
            BatchModel model=(BatchModel) bacthesProductList.get(position);
            selectBatchNumber = model.getBatch_number();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==binding.btnStartDetecting.getId()){
            if (selectedProductNumber.equals("")|| selectBatchNumber.equals("") || selectedProductNumber==null || selectBatchNumber==null){
                showToast("Select Product or batch");
                return;
            }
            showChoosePhotoDialog(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                clipData = data.getClipData();
                if (clipData != null && clipData.getItemCount() < 0) {
                    showToast("please select images.");
                    return;
                }
                makeDefectsPredictionApiCall();
            }
        } else {
            selectedPhotoUri = null;
            showToast("Error, Try again selecting images");
        }


    }
}