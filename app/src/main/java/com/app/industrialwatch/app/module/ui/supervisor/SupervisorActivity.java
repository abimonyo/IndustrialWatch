package com.app.industrialwatch.app.module.ui.supervisor;

import static com.app.industrialwatch.common.utils.AppConstants.GET_ALL_SUPERVISOR;
import static com.app.industrialwatch.common.utils.AppConstants.OK;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.SupervisorModel;
import com.app.industrialwatch.app.module.ui.adapter.SupervisorAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.databinding.ActivitySupervisorBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupervisorActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, View.OnClickListener {

    ActivitySupervisorBinding binding;
    SupervisorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySupervisorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initRecyclerView(binding.includedLayout.includedRcv.recyclerView);
        binding.includedLayout.btnAddSection.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter=null;
        setAdapter(null);
        doGetRequest(GET_ALL_SUPERVISOR,this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()){
            try {
                JSONObject responseJson=new JSONObject(response.body().string());
                if (responseJson.getInt("code")==OK){
                    JSONArray jsonArray=responseJson.getJSONArray("data");
                    List<BaseItem> superVisorList=new Gson().fromJson(jsonArray.toString(),new TypeToken<List<SupervisorModel>>(){}.getType());
                    if (adapter==null){
                        adapter=new SupervisorAdapter(superVisorList,null);
                        setAdapter(adapter);
                    }
                }
            } catch (JSONException | IOException e) {
                Log.d("error==>>",e.getMessage());
            }

        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>",t.getMessage());

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), AddSupervisorActivity.class));
    }
}