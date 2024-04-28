package com.app.industrialwatch.app.module.ui.admin.production;

import android.os.Bundle;
import android.util.Log;

import com.app.industrialwatch.R;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityBatchDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatchDetailActivity extends BaseActivity implements Callback<ResponseBody> {

    ActivityBatchDetailsBinding binding;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBatchDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras() != null)
            bundle = getIntent().getExtras();
        setPrimaryActionBar(binding.includedToolbar.primaryToolbar, bundle.getString(AppConstants.KEY_NUMBER, ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetRequest(AppConstants.GET_BATCHES_DETAILS, getServerParams(), this);
    }

    private Map<String, String> getServerParams() {
        Map<String, String> params = new HashMap<>();
        params.put("batch_number", bundle.getString(AppConstants.KEY_NUMBER, ""));
        return params;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                setData(object);
            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        }
    }

    private void setData(JSONObject object) {
        try {
            binding.tvDate.setText(object.getString("date"));
            binding.tvDefectedPiece.setText(String.valueOf(object.getInt("defected_piece")));
            binding.tvTotalPiece.setText(String.valueOf(object.getInt("total_piece")));
            binding.tvRejectionTolerance.setText(object.getDouble("rejection_tolerance") + "%");
            binding.tvYield.setText(object.getDouble("batch_yield") + "%");
            if (object.getInt("status") == 1) {
                binding.tvStatus.setText("Rejected");
                binding.tvStatus.setBackgroundResource(R.color.red);
            } else
                binding.tvStatus.setText("Accepted");

        } catch (JSONException e) {
            Log.d("error==>>", e.getMessage());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
    }
}