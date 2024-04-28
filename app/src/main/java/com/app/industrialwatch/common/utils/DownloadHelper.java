package com.app.industrialwatch.common.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.app.industrialwatch.app.network.ApiService;
import com.app.industrialwatch.app.network.RetrofitClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DownloadHelper {
    private final ApiService apiService;

    public DownloadHelper() {
        // Create Retrofit instance
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        // Create ApiService instance
        apiService = retrofit.create(ApiService.class);
    }

    public void downloadFile(Context context, String url, String productNumber) {
        // Make API call to download the file
        Call<ResponseBody> call = apiService.downloadFile(url, getServerParam(productNumber));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // File downloaded successfully
                    saveFile(context, response.body(), productNumber);
                } else {
                    // Handle unsuccessful response
                    Log.e("DownloadManager", "Download failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                Log.e("DownloadManager", "Download failed: " + t.getMessage());
            }
        });
    }

    private void saveFile(Context context, ResponseBody body, String productNumber) {
        try {
            // Get the destination directory
            String destinationDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            String fileName = destinationDirectory + productNumber + ".zip";
            String mFileName = fileName.replace(" ", "_")
                    .replaceAll("[.]+", ".");

            InputStream fileData = body.byteStream();
            try {
                FileOutputStream output = context.openFileOutput(mFileName, Context.MODE_PRIVATE);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileData.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e("DownloadManager", "Error saving file: " + e.getMessage());
        }
    }

    private Map<String, String> getServerParam(String number) {
        Map<String, String> params = new HashMap<>();
        params.put("product_number", number);
        return params;
    }
}
