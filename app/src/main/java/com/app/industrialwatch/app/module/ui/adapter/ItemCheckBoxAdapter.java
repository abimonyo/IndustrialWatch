package com.app.industrialwatch.app.module.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.app.industrialwatch.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCheckBoxAdapter extends ArrayAdapter<String> {
    View.OnClickListener listener;
    String[] itemList;
    Context context;
    Map<Integer, Boolean> itemStates;

    public ItemCheckBoxAdapter(Context context, String[] itemList, View.OnClickListener listener1) {
        super(context, R.layout.layout_item_program, itemList);
        this.context = context;
        listener = listener1;
        this.itemList = itemList;
        itemStates = new HashMap<>();
        // Initialize all items as unchecked initially
        for (int i = 0; i < itemList.length; i++) {
            itemStates.put(i, false);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rowView = inflater.inflate(R.layout.layout_item_program, parent, false);

        TextView textView = rowView.findViewById(R.id.text);
        CheckBox checkBox = rowView.findViewById(R.id.checkbox);

        final String currentItem = itemList[position];

        // Set text to TextView
        textView.setText(currentItem);
        checkBox.setChecked(itemStates.get(position));


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox checkBox = (CheckBox) buttonView;
                itemStates.put(position, isChecked);
                View view = buttonView.getRootView();
                String result = checkBox.isChecked() + "," + currentItem;
                view.setTag(result);
                listener.onClick(view);
                notifyDataSetChanged();

            }
        });

        return rowView;
    }
}
