package com.app.industrialwatch.app.module.ui.admin.section;

import static com.app.industrialwatch.common.utils.AppConstants.GET_ALL_RULES_URL;
import static com.app.industrialwatch.common.utils.AppConstants.INSERT_SECTION;
import static com.app.industrialwatch.common.utils.AppUtils.validateEmptyEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.RulesModel;
import com.app.industrialwatch.app.data.models.SectionDetailsModel;
import com.app.industrialwatch.app.module.ui.adapter.RuleAdapter;
import com.app.industrialwatch.app.module.ui.adapter.SectionAdapter;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityAddUpdateSectionBinding;
import com.app.industrialwatch.databinding.ActivitySectionBinding;
import com.app.industrialwatch.databinding.LayoutRecyclerviewBinding;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUpdateSectionActivity extends BaseRecyclerViewActivity implements OnRecyclerViewItemClickListener, Callback<ResponseBody>, View.OnClickListener {
    ActivityAddUpdateSectionBinding binding;
    SectionAdapter adapter;
    List<RulesModel> insertedRuleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUpdateSectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setPrimaryActionBar(binding.toolbar.primaryToolbar,getString(R.string.add_section));
        initRecyclerView(binding.recyclerView.recyclerView);
        initView();
    }

    private void initView() {
        binding.btnAddSection.setOnClickListener(this);
        insertedRuleList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);
        doGetRequest(GET_ALL_RULES_URL, this);
    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        RulesModel model = (RulesModel) adapter.getItemAt(holder.getLayoutPosition());
        EditText etFine = holder.itemView.findViewById(R.id.et_rule_item_fine);
        EditText etAllowedTime = holder.itemView.findViewById(R.id.et_rule_item_time);
        CheckBox checkBox = holder.itemView.findViewById(R.id.cb_rule_item);
        if (!validateEmptyEditText(etFine) || !validateEmptyEditText(etAllowedTime))
            checkBox.setChecked(false);
        if (checkBox.isChecked()) {
            model.setAllowedTime(getValueFromField(etAllowedTime));
            model.setFine(Double.parseDouble(getValueFromField(etFine)));
            insertedRuleList.add(model);
        } else {
            insertedRuleList.remove(model);
        }

        showToast(model.getName() + " " + etFine.getText().toString().trim() + " " + etAllowedTime.getText().toString().trim());
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                JSONObject object = new JSONObject(response.body().string());
                if (object.has("code") && object.getInt("code") == AppConstants.OK) {
                    AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.ITEM_RULE;
                    JSONArray array = object.getJSONArray("data");
                    List<BaseItem> ruleNameList = new Gson().fromJson(array.toString(), new TypeToken<List<RulesModel>>() {
                    }.getType());
                    if (adapter == null) {
                        adapter = new SectionAdapter(ruleNameList, this);
                        setAdapter(adapter);
                    }
                }
            } catch (IOException | JSONException e) {
                Log.d("error==>", e.getMessage());
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>", t.getMessage());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnAddSection.getId()) {
            String sectionName=getValueFromField(binding.etSectionName);
            JSONObject requestJson=new JSONObject();
            try {
                requestJson.put("name",sectionName);
                String rulesString=new Gson().toJson(insertedRuleList);
                JsonArray jsonArray = new Gson().fromJson(rulesString, JsonArray.class);
                requestJson.put("rules",jsonArray);
                RequestBody body = RequestBody.create(requestJson.toString(),MediaType.get("application/json; charset=utf-8"));

                insertSection(body);
            } catch (JSONException e) {
                Log.d("error==>>",e.getMessage());
            }

        }
    }
    private void insertSection(RequestBody object){
        doPostRequest(INSERT_SECTION, object, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()){
                try {
                    JSONObject responseObject=new JSONObject(response.body().string());
                    showToast(responseObject.getString("message"));
                    finish();
                } catch (JSONException|IOException e) {
                    Log.d("error==>>",e.getMessage());
                }
            }
            else
                Log.d("error==>>","not success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.d("error==>>",t.getMessage());
            }
        });
    }
}