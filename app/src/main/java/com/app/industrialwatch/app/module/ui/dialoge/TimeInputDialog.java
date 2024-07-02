package com.app.industrialwatch.app.module.ui.dialoge;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.industrialwatch.R;

public class TimeInputDialog {

    private Context context;
    private TimeInputListener listener;

    public interface TimeInputListener {
        void onTimeInput(String time);
    }

    public TimeInputDialog(Context context, TimeInputListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public void show() {
        // Create the dialog builder
        StringBuilder sb=new StringBuilder();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.layout_dialog_hours_minute, null);

        // Get references to the EditText fields
         EditText editTextHours = dialogView.findViewById(R.id.et_hours);
        EditText editTextMinutes = dialogView.findViewById(R.id.et_minutes);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editTextSeconds = dialogView.findViewById(R.id.et_seconds);

        // Set the custom layout as the dialog's view
        builder.setView(dialogView)
                .setTitle("Enter Time")
                .setPositiveButton("OK", (dialog, id) -> {
                    // Retrieve the input values
                    String hours = editTextHours.getText().toString();
                    String minutes = editTextMinutes.getText().toString();
                    String seconds = editTextSeconds.getText().toString();
                    sb.append(hours);
                    sb.append(":");
                    sb.append(minutes);
                    sb.append(":");
                    sb.append(seconds);
                    if (listener != null && !sb.toString().equals("")) {
                        listener.onTimeInput(sb.toString());
                    }
                    // Handle the input values (e.g., show a toast)
                    //Toast.makeText(context, "Hours: " + hours + ", Minutes: " + minutes, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog
                    dialog.dismiss();
                });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
