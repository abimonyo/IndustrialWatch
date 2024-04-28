package com.app.industrialwatch.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.network.ApiService;
import com.app.industrialwatch.app.network.RetrofitClient;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.databinding.LayoutToolbarBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

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
    }

    public void doPostRequest(String url, Map<String, Object> params, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(ApiService.class).doPostRequest(url, params);
        call.enqueue(callback);
    }

    public void showErrorMessage(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.errorBody().string());
            showToast(object.getString("message"));
        } catch (IOException|JSONException e) {
            Log.d("error==>>", e.getMessage());
        }

    }

}
