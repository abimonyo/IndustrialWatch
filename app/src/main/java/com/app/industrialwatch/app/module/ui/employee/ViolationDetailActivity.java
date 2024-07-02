package com.app.industrialwatch.app.module.ui.employee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.ViolationModel;
import com.app.industrialwatch.app.module.ui.adapter.EmployeeAdapter;
import com.app.industrialwatch.app.module.ui.adapter.SliderAdapter;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityViolationDetailBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViolationDetailActivity extends BaseActivity implements Callback<ResponseBody> {

    ActivityViolationDetailBinding binding;
    Bundle bundle;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViolationDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getExtras()!=null){
            bundle=getIntent().getExtras();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetRequest(bundle.getString(AppConstants.FROM).toString().equals("guest")?AppConstants.GET_GUEST_VIOLATIONS_DETAILS:AppConstants.GET_VIOLATION_DETAILS,getParams("violation_id",bundle.getInt(AppConstants.BUNDLE_KEY,0)+""),this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(AppConstants.GET_VIOLATION_DETAILS)||call.request().url().url().toString().contains(AppConstants.GET_GUEST_VIOLATIONS_DETAILS)) {
                    JSONObject object=new JSONObject(response.body().string());
                    List<ViolationModel.ImageModel> violationsImages=new Gson().fromJson(object.getJSONArray("images").toString(),new TypeToken<List<ViolationModel.ImageModel>>(){}.getType());
                    SliderAdapter adapter=new SliderAdapter(ViolationDetailActivity.this,violationsImages);
                    binding.viewPager.setAdapter(adapter);

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
        Log.d("error==>>", t.getMessage());
        showToast("Failed, Try again.");
        cancelDialog(dialog);
    }
}