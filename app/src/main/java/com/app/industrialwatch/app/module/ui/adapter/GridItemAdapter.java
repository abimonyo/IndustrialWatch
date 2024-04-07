package com.app.industrialwatch.app.module.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.GridItemModel;

import java.util.ArrayList;

public class GridItemAdapter extends ArrayAdapter<GridItemModel> {
    View.OnClickListener listener;
    public GridItemAdapter(@NonNull Context context, ArrayList<GridItemModel> itemModelArrayList,View.OnClickListener clickListener) {
        super(context, 0, itemModelArrayList);
        this.listener=clickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_grid_item, parent, false);
        }
        GridItemModel gridItemModel = getItem(position);
        TextView gridItemTV = listitemView.findViewById(R.id.tv_grid_item);
        ImageView gridItemIV = listitemView.findViewById(R.id.iv_grid_item);
        gridItemTV.setText(gridItemModel.getGridItemName());
        gridItemIV.setImageResource(gridItemModel.getGridItemImage());

        gridItemIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(position);
                listener.onClick(v);
            }
        });
        return listitemView;
    }
}
