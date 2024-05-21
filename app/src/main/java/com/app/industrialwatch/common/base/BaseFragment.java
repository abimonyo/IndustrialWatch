package com.app.industrialwatch.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.preferences.SharedPreferenceManager;
import com.app.industrialwatch.app.network.ApiService;
import com.app.industrialwatch.app.network.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseFragment extends Fragment {
    /*public void setToolbarTitle(String title){
        Toolbar toolbar=this.getActivity().findViewById(R.id.included_toolbar).findViewById(R.id.primary_toolbar);
        if (toolbar!=null) {
            TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);
            if (tvTitle != null && title != null)
                tvTitle.setText(title);
        }
    }*/
    public void setToolbarTitle(String title){
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).setToolbarTitle(title);
        }
    }
    public Map<String, String> getParams(String key,String value){
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        return params;
    }
    public void doGetRequest(String url, Map<String, String> params, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(ApiService.class).doGetRequest(url, params);
        call.enqueue(callback);
    }

    public void doGetRequest(String url, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(ApiService.class).doGetRequest(url);
        call.enqueue(callback);
    }

    public Dialog getProgressDialog(boolean cancelable) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_app_dialoge);
        dialog.setCancelable(cancelable);
        return dialog;
    }

    public void cancelDialog(Dialog dialog, Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    public void showProgressDialog(Dialog dialog, Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        });
    }

    public void showErrorMessage(Response<ResponseBody> response) {
        try {
            JSONObject object = new JSONObject(response.errorBody().string());
            showToast(object.getString("message"));
        } catch (IOException | JSONException e) {
            Log.d("error==>>", e.getMessage());
        }

    }

    public void showToast(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void setRecyclerViewHeader(String first, String Second, String third, String four, View view) {
        findTextViewById(R.id.tv_index_item, view).setText(first);
        findTextViewById(R.id.tv_top_item, view).setText(Second);
        findTextViewById(R.id.tv_four_item, view).setText(third);
        findTextViewById(R.id.tv_end_item, view).setText(four);
    }

    public TextView findTextViewById(int resourceId, View view) {
        return (TextView) view.findViewById(resourceId);
    }

}
