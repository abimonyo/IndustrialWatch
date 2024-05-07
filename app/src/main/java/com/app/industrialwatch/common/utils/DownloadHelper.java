package com.app.industrialwatch.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.network.ApiService;
import com.app.industrialwatch.app.network.RetrofitClient;
import com.app.industrialwatch.common.base.BaseActivity;

import java.io.File;
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

public class DownloadHelper extends BaseActivity {
    private final ApiService apiService;

    public DownloadHelper() {
        // Create Retrofit instance
        Retrofit retrofit = RetrofitClient.getRetrofitInstance();
        // Create ApiService instance
        apiService = retrofit.create(ApiService.class);
    }

    public void downloadFile(Context context, String url, String productNumber) {
        // Make API call to download the file
        Toast.makeText(context, "Download Started.", Toast.LENGTH_SHORT).show();
        Call<ResponseBody> call = apiService.downloadFile(url, getServerParam(productNumber));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // File downloaded successfully
                    saveFile(context, response.body(), productNumber);
                } else {
                    // Handle unsuccessful response
                    showErrorMessage(response);
                    Log.e("DownloadManager", "Download failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                Toast.makeText(DownloadHelper.this, "Download failed:Try again.", Toast.LENGTH_SHORT).show();
               // showToast("");

                Log.e("DownloadManager", "Download failed: " + t.getMessage());
            }
        });
    }

    private void saveFile(Context context, ResponseBody body, String productNumber) {
        try {
            // Get the destination directory
            String destinationDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

            // Clean up the product number for use in file name
            String cleanedProductNumber = productNumber.replaceAll("[^a-zA-Z0-9.-]", "_"); // Replace special characters with underscores

            String fileName = destinationDirectory + "/" + cleanedProductNumber + ".zip";

            File file = new File(fileName);
            InputStream fileData = body.byteStream();

            // Prepare notification
            prepareNotification(context);

            try (FileOutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                long totalBytesRead = 0;
                long fileSize = body.contentLength(); // Get total file size

                while ((bytesRead = fileData.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                    // Calculate progress percentage
                    int progress = (int) ((totalBytesRead * 100) / fileSize);

                    // Update notification progress
                    updateNotificationProgress(context, progress);

                    // Simulate download progress for demonstration
                    //Thread.sleep(10); // Simulate download speed

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("op==>>", "completed");
            // Show notification for completion
            Toast.makeText(context, "Download Completed.", Toast.LENGTH_SHORT).show();
            showDownloadCompleteNotification(context, cleanedProductNumber);

        } catch (Exception e) {
            Log.e("DownloadManager", "Error saving file: " + e.getMessage());
        }
    }

    @SuppressLint("MissingPermission")
    private void prepareNotification(Context context) {
        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Download Channel";
            String description = "Channel for file download notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("download_channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Build initial notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "download_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Downloading File")
                .setContentText("Download in progress")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true) // Notification cannot be dismissed by the user
                .setProgress(100, 0, false); // Initial progress set to 0%

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(123, builder.build());
    }

    @SuppressLint("MissingPermission")
    private void updateNotificationProgress(Context context, int progress) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "download_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Downloading File")
                .setContentText("Download in progress: " + progress + "%")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true) // Notification cannot be dismissed by the user
                .setProgress(100, progress, false); // Update progress

        // Show updated notification
        notificationManager.notify(123, builder.build());
    }

    @SuppressLint("MissingPermission")
    private void showDownloadCompleteNotification(Context context, String cleanedProductNumber) {
        // Build notification for download completion
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(123);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "download_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("File Downloaded")
                .setContentText("File " + cleanedProductNumber + ".zip has been downloaded successfully.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Allow the user to dismiss the notification

        // Show the notification
        // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(123 + 1, builder.build());
    }


    private Map<String, String> getServerParam(String number) {
        Map<String, String> params = new HashMap<>();
        params.put("product_number", number);
        return params;
    }
}
