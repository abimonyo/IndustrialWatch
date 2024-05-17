package com.app.industrialwatch.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.app.network.ApiService;
import com.app.industrialwatch.app.network.RetrofitClient;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.LayoutToolbarBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;

    public void setPrimaryActionBar(Toolbar toolbar, String title) {
        TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);
        if (tvTitle != null && title != null)
            tvTitle.setText(title);
        findViewById(R.id.arrow_button).setOnClickListener(view -> {
            finish();
        });
    }

    public void setPrimaryActionBar(Toolbar toolbar, String title, View.OnClickListener callBack) {
        TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);
        if (tvTitle != null && title != null)
            tvTitle.setText(title);
        findViewById(R.id.arrow_button).setOnClickListener(callBack);
    }

    public void setSecondaryActionBar(Toolbar toolbar, String title) {
        TextView tvTitle = toolbar.findViewById(R.id.tv_title_secondary);
        if (tvTitle != null && title != null)
            tvTitle.setText(title);
        findViewById(R.id.iv_back_toolbar).setOnClickListener(view -> {
            finish();
        });

    }

    public void setDashboardActionBar() {

    }

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public void setRecyclerViewHeader(String first, String Second, String third, String four) {
        findTextViewById(R.id.tv_index_item).setText(first);
        findTextViewById(R.id.tv_top_item).setText(Second);
        findTextViewById(R.id.tv_four_item).setText(third);
        findTextViewById(R.id.tv_end_item).setText(four);
    }

    public TextView findTextViewById(int resourceId) {
        return (TextView) findViewById(resourceId);
    }


    public void setSecondaryActionBar(Toolbar toolbar, boolean hideTitle, View.OnClickListener onClickListener) {
        if (hideTitle)
            toolbar.findViewById(R.id.tv_title_secondary).setVisibility(View.GONE);
        toolbar.findViewById(R.id.btn_secondary_toolbar).setOnClickListener(onClickListener);
        findViewById(R.id.iv_back_toolbar).setOnClickListener(view -> {
            finish();
        });

    }

    public void setPrimaryActionBar() {
        toolbar = findViewById(R.id.primary_toolbar);
        if (toolbar != null) {
            toolbar.findViewById(R.id.toolbar_title).setVisibility(View.GONE);
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void startActivity(Bundle bundle, Class<? extends Activity> activityToGo) {
        Intent intent = new Intent(this, activityToGo);
        if (bundle != null) {
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    public Dialog getProgressDialog(boolean cancelable) {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_app_dialoge);
        dialog.setCancelable(cancelable);
        return dialog;
    }

    public void cancelDialog(Dialog dialog) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    public void showProgressDialog(Dialog dialog) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public String getValueFromField(EditText etValue) {
        return etValue.getText().toString().trim();
    }

    public void doGetRequest(String url, Map<String, String> params, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(ApiService.class).doGetRequest(url, params);
        call.enqueue(callback);
    }

    public void doGetRequest(String url, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(ApiService.class).doGetRequest(url);
        call.enqueue(callback);
    }

    public void doPostRequest(String url, RequestBody requestObject, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(ApiService.class).doPostRequest(url, requestObject);
        call.enqueue(callback);
    }  public void doPutRequest(String url, RequestBody requestObject, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(ApiService.class).doPutRequest(url, requestObject);
        call.enqueue(callback);
    }

    public void doPostRequest(String url, Map<String, Object> params, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(ApiService.class).doPostRequest(url, params);
        call.enqueue(callback);
    }

    public void doPostRequestWithMapBody(String url, Map<String, RequestBody> parts, List<MultipartBody.Part> imageParts, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(ApiService.class).doPostRequestWithMapBody(url, parts, imageParts);
        call.enqueue(callback);
    }

    public void showErrorMessage(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.errorBody().string());
            showToast(object.getString("message"));
        } catch (IOException | JSONException e) {
            Log.d("error==>>", e.getMessage());
        }

    }
    public Map<String, String> getParams(String key,String value){
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        return params;
    }
    public void setSpinnerAdapter(List<SectionModel> batchModels, Spinner spinner, AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter<SectionModel> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, batchModels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }

}
