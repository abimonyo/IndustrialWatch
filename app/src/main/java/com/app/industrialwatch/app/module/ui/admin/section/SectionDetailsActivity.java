package com.app.industrialwatch.app.module.ui.admin.section;

import static com.app.industrialwatch.common.utils.AppConstants.GET_SECTION_DETAIL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.RulesModel;
import com.app.industrialwatch.app.data.models.SectionDetailsModel;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.module.ui.adapter.SectionAdapter;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivitySectionBinding;
import com.app.industrialwatch.databinding.ActivitySectionDetailsBinding;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SectionDetailsActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody>, View.OnClickListener {

    ActivitySectionDetailsBinding binding;
    Bundle bundle;
    SectionAdapter adapter;
    int sectionId;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySectionDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getIntent().getExtras() != null)
            bundle = getIntent().getExtras();
        binding.sectionRulesRcv.headerLayout.layoutHeaderWrapper.setVisibility(View.GONE);
        setPrimaryActionBar(binding.sectionAppLayout.primaryToolbar, "Section Name");
        initRecyclerView(binding.sectionRulesRcv.recyclerView);
        binding.button.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);
        dialog=getProgressDialog(false);
        if (bundle != null) {
            showProgressDialog(dialog);
            doGetRequest(GET_SECTION_DETAIL, getServerParams(), this);
        }
    }

    private Map<String, String> getServerParams() {
        Map<String, String> params = new HashMap<>();
        params.put("section_id", bundle.getInt("id", 0) + "");
        return params;
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.isSuccessful()) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.SECTION_DETAILS;
                sectionId = object.getInt("id");
                binding.sectionAppLayout.toolbarTitle.setText(object.getString("name"));
                JSONArray jsonArray = object.getJSONArray("rules");
                List<BaseItem> rulesList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<RulesModel>>() {
                }.getType());
                if (adapter == null) {
                    adapter = new SectionAdapter(rulesList, null);
                    setAdapter(adapter);
                }

            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        } else
            showErrorMessage(response);
        cancelDialog(dialog);
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());
        showToast("Failed: Try again.");
        cancelDialog(dialog);

    }

    @Override
    public void onClick(View v) {
        if (v == binding.button) {
            bundle.putString(AppConstants.FROM,"SectionDetails");
            startActivity(bundle,AddUpdateSectionActivity.class);
        }
    }
}