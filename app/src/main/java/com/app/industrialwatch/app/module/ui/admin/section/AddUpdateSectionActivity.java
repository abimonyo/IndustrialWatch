package com.app.industrialwatch.app.module.ui.admin.section;

import static com.app.industrialwatch.common.utils.AppConstants.GET_ALL_RULES_URL;
import static com.app.industrialwatch.common.utils.AppConstants.GET_SECTION_DETAIL;
import static com.app.industrialwatch.common.utils.AppConstants.INSERT_SECTION;
import static com.app.industrialwatch.common.utils.AppConstants.UPDATE_SECTION;
import static com.app.industrialwatch.common.utils.AppUtils.validateEmptyEditText;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.RulesModel;
import com.app.industrialwatch.app.module.ui.adapter.SectionAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewActivity;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.ActivityAddUpdateSectionBinding;
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
import java.util.Objects;
import java.util.function.Predicate;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUpdateSectionActivity extends BaseRecyclerViewActivity implements OnRecyclerViewItemClickListener, Callback<ResponseBody>, View.OnClickListener, TextWatcher {
    ActivityAddUpdateSectionBinding binding;
    SectionAdapter adapter;
    List<RulesModel> insertedRuleList;
    Bundle bundle;
    EditText etAllowedTime;
    List<BaseItem> ruleNameList;
    private boolean isManualChange = false;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUpdateSectionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setPrimaryActionBar(binding.toolbar.primaryToolbar, getString(R.string.add_section));
        initRecyclerView(binding.recyclerView.recyclerView);
        initView();
        if (getIntent().getExtras() != null)
            bundle = getIntent().getExtras();
    }

    private void initView() {
        binding.recyclerView.headerLayout.layoutHeaderWrapper.setVisibility(View.GONE);
        binding.btnAddSection.setOnClickListener(this);
        insertedRuleList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = null;
        setAdapter(null);
        dialog = getProgressDialog(false);
        showProgressDialog(dialog);
        doGetRequest(GET_ALL_RULES_URL, this);
        if (bundle != null) {
            binding.toolbar.toolbarTitle.setText(getString(R.string.edit_section));

        }
    }

    private Map<String, String> getServerParams() {
        Map<String, String> params = new HashMap<>();
        params.put("section_id", bundle.getInt("id", 0) + "");
        return params;
    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        RulesModel model = (RulesModel) adapter.getItemAt(holder.getLayoutPosition());
        EditText etFine = holder.itemView.findViewById(R.id.et_rule_item_fine);
        etAllowedTime = holder.itemView.findViewById(R.id.et_rule_item_time);
       /* etAllowedTime.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        etAllowedTime.addTextChangedListener(this);*/
        CheckBox checkBox = holder.itemView.findViewById(R.id.cb_rule_item);
        if (model.getAllowedTime() != null && model.getFine() != null) {
            if (checkBox.isChecked()) {
                if (!isRuleExist(model))
                    insertedRuleList.add(model);
            } else
                insertedRuleList.remove(model);
        } else if (validateEmptyEditText(etFine) && validateEmptyEditText(etAllowedTime)) {
            if (checkBox.isChecked()) {
                model.setAllowedTime(getValueFromField(etAllowedTime));
                model.setFine(Double.parseDouble(getValueFromField(etFine)));
                insertedRuleList.add(model);
            } else {
                insertedRuleList.remove(model);
            }
        } else {
            showToast("Please fill required fields");
            checkBox.setChecked(false);
        }

    }


    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try {
                if (call.request().url().url().toString().contains(GET_ALL_RULES_URL)) {
                    AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.ITEM_RULE;
                    JSONArray array = new JSONArray(response.body().string());
                    ruleNameList = new Gson().fromJson(array.toString(), new TypeToken<List<RulesModel>>() {
                    }.getType());
                    if (adapter == null) {
                        adapter = new SectionAdapter(ruleNameList, this);
                        setAdapter(adapter);
                    }
                    if (bundle != null)
                        doGetRequest(GET_SECTION_DETAIL, getServerParams(), this);
                } else if (call.request().url().url().toString().contains(GET_SECTION_DETAIL)) {
                    JSONObject object = new JSONObject(response.body().string());
                    binding.etSectionName.setText(object.getString("name"));
                    AppConstants.VIEW_FOR_DETAIL_OR_FOR_ITEM = BaseItem.ITEM_RULE;
                    JSONArray array = object.getJSONArray("rules");
                    List<BaseItem> ruleNameList = new Gson().fromJson(array.toString(), new TypeToken<List<RulesModel>>() {
                    }.getType());
                    checkAndMapRules(ruleNameList);
                }
            } catch (JSONException | IOException e) {
                Log.d("error==>", e.getMessage());
            }

        } else
            showErrorMessage(response);
        cancelDialog(dialog);

    }


    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>", t.getMessage());
        cancelDialog(dialog);
        showToast("Failed: try again.");
    }

    private void checkAndMapRules(List<BaseItem> list) {
        if (ruleNameList != null && adapter != null && ruleNameList.size() > 0) {
            for (int i = 0; i < ruleNameList.size(); i++) {
                RulesModel rule1 = (RulesModel) ruleNameList.get(i);
                for (int j = 0; j < list.size(); j++) {
                    RulesModel rule2 = (RulesModel) list.get(j);
                    if (rule1.getName().equals(rule2.getName())) {
                        rule2.setChecked(true);
                        ruleNameList.set(i, rule2);
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    private boolean isRuleExist(RulesModel model) {
        Predicate<RulesModel> predicate = existingModel -> existingModel.getId() == model.getId();
        return insertedRuleList.stream().anyMatch(predicate);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnAddSection.getId()) {
            if (insertedRuleList.size() > 0) {
                String sectionName = getValueFromField(binding.etSectionName);
                JSONObject requestJson = new JSONObject();
                try {

                    requestJson.put("name", sectionName);
                    JSONArray jsonArray = new JSONArray();
                    for (RulesModel rules : insertedRuleList) {
                        JSONObject object = new JSONObject();
                        object.put("rule_id", rules.getId());
                        object.put("allowed_time", rules.getAllowedTime());
                        object.put("fine", rules.getFine());
                        jsonArray.put(object);
                    }
                    requestJson.put("rules", jsonArray);
                    if (bundle == null) {
                        RequestBody body = RequestBody.create(requestJson.toString(), MediaType.get("application/json; charset=utf-8"));
                        showProgressDialog(dialog);
                        insertSection(body, INSERT_SECTION);
                    } else if (Objects.requireNonNull(bundle.getString(AppConstants.FROM)).equals("SectionDetails")) {
                        requestJson.put("id", bundle.getInt("id", 0));
                        RequestBody body = RequestBody.create(requestJson.toString(), MediaType.get("application/json; charset=utf-8"));
                        showProgressDialog(dialog);
                        updateSection(body, UPDATE_SECTION);

                    }
                } catch (JSONException e) {
                    Log.d("error==>>", e.getMessage());
                }
            } else
                showToast("No, rule selected.");
        }

    }

    private void updateSection(RequestBody body, String url) {
        doPutRequest(url, body, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObject = new JSONObject(response.body().string());
                        showToast(responseObject.getString("message"));
                        finish();
                    } catch (IOException | JSONException e) {
                        Log.d("error==>>", e.getMessage());
                    }
                } else
                    showErrorMessage(response);
                cancelDialog(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                cancelDialog(dialog);
                Log.d("error==>>", t.getMessage());
                showToast("Failed: Try again.");

            }
        });
    }

    private void insertSection(RequestBody object, String url) {
        doPostRequest(url, object, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObject = new JSONObject(response.body().string());
                        showToast(responseObject.getString("message"));
                        finish();
                    } catch (JSONException | IOException e) {
                        Log.d("error==>>", e.getMessage());
                    }
                } else {
                    Log.d("error==>>", "not success");
                    showErrorMessage(response);
                }
                cancelDialog(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("error==>>", t.getMessage());
                cancelDialog(dialog);
                showToast("Failed: Try again.");
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
    /*    if (s.length() == 2) {
            String string = etAllowedTime.getText().toString();
            string = string.concat(":");
            etAllowedTime.setText(string);
            etAllowedTime.setSelection(string.length());

        }*/

    }
}