package com.app.industrialwatch.app.module.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.MyJSONObject;
import com.app.industrialwatch.app.module.ui.admin.main.AdminDashboardActivity;
import com.app.industrialwatch.app.network.ApiService;
import com.app.industrialwatch.app.network.RetrofitClient;
import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.AppUtils;
import com.app.industrialwatch.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener, Callback<ResponseBody> {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!AppUtils.validateEmptyEditText(binding.edtUserName)) {
            binding.edtUserName.setError("Please enter username");
            binding.edtPassword.requestFocus();
            return;
        }
        if (!AppUtils.validateEmptyEditText(binding.edtPassword)) {
            binding.edtPassword.setError("Please enter password");
            binding.edtPassword.requestFocus();
            return;
        }
        doGetRequest(AppConstants.LOGIN_URL, getServerParams(), this);

    }

    private Map<String, String> getServerParams() {
        Map<String, String> params = new HashMap<>();
        params.put("username", getValueFromField(binding.edtUserName));
        params.put("password", getValueFromField(binding.edtPassword));
        return params;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
            if (jsonObject.has("data") && !jsonObject.isNull("data") && jsonObject.get("data") instanceof JSONObject) {
                JSONObject data = jsonObject.getJSONObject("data");
                int id = data.getInt("id");
                String name = data.getString("name");
                showToast(name);
                startActivity(new Intent(getApplicationContext(), AdminDashboardActivity.class));
            }
        } catch (JSONException | IOException e) {
            Log.d("error==>>", e.getMessage());
        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d("error==>>", t.getMessage());

    }
}