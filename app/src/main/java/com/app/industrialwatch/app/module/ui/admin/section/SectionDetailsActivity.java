package com.app.industrialwatch.app.module.ui.admin.section;

import static com.app.industrialwatch.common.utils.AppConstants.GET_RULES_FOR_SECTION_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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

public class SectionDetailsActivity extends BaseRecyclerViewActivity implements Callback<ResponseBody> {

    ActivitySectionDetailsBinding binding;
    Bundle bundle;
    SectionAdapter adapter;
    int sectionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_details);
        binding = ActivitySectionDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (getIntent().getExtras() != null)
            bundle = getIntent().getExtras();
        setPrimaryActionBar(binding.sectionAppLayout.primaryToolbar, "Section Name");
        initRecyclerView(binding.sectionRulesRcv.recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);
        if (bundle != null)
            doGetRequest(GET_RULES_FOR_SECTION_URL, getServerParams(), this);
    }

    private Map<String, String> getServerParams() {
        Map<String, String> params = new HashMap<>();
        params.put("id", bundle.getInt("id", 0) + "");
        return params;
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        if (response.isSuccessful()) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                if (object.has("code") && object.getInt("code") == AppConstants.OK) {
                    AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.SECTION_DETAILS;
                    sectionId = object.getJSONObject("data").getInt("id");
                    JSONArray jsonArray = object.getJSONObject("data").getJSONArray("rules");
                    List<BaseItem> rulesList = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<RulesModel>>() {
                    }.getType());
                    if (adapter == null) {
                        adapter = new SectionAdapter(rulesList, null);
                        setAdapter(adapter);
                    }
                }
            } catch (JSONException | IOException e) {
                Log.d("error==>>", e.getMessage());
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());

    }

}