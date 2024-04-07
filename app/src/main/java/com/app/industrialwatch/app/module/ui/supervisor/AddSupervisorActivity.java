package com.app.industrialwatch.app.module.ui.supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.module.ui.adapter.SectionAdapter;
import com.app.industrialwatch.app.module.ui.admin.section.SectionActivity;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityAddSupervisorBinding;
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

public class AddSupervisorActivity extends BaseActivity implements Callback<ResponseBody> {

    ActivityAddSupervisorBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddSupervisorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //binding.sectionSpinner.setAdapter(new ArrayAdapter<String>());
    }

    @Override
    protected void onResume() {
        super.onResume();
        doGetRequest(AppConstants.SECTION_URL,this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.body().string());
            if (object.has("code") && object.getInt("code") == AppConstants.OK) {
                JSONArray jsonArray = object.getJSONArray("data");
                List<BaseItem> sectionList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<SectionModel>>() {
                }.getType());

            }
        } catch (JSONException | IOException e) {
            Log.d("error==>>", e.getMessage());
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }
}