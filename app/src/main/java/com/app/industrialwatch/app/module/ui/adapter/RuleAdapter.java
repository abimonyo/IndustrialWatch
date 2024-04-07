package com.app.industrialwatch.app.module.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;

import java.util.List;

public class RuleAdapter extends BaseRecyclerViewAdapter {
    public RuleAdapter(List<BaseItem> items, OnRecyclerViewItemClickListener itemClickListener) {
        super(items, itemClickListener);
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rule,parent,false);
        return new RuleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {

    }
    private class RuleHolder extends BaseRecyclerViewHolder {

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public RuleHolder(View view) {
            super(view);
        }
    }
    public int getItemViewType(int position) {
        return getItemAt(position).getItemType();
    }

}
