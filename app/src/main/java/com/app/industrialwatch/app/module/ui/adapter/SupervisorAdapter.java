package com.app.industrialwatch.app.module.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.SupervisorModel;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.databinding.LayoutItemRuleBinding;
import com.app.industrialwatch.databinding.LayoutItemSupervisorBinding;
import com.app.industrialwatch.databinding.LayoutRulesTableBinding;

import java.util.List;

public class SupervisorAdapter extends BaseRecyclerViewAdapter {
    public SupervisorAdapter(List<BaseItem> items, OnRecyclerViewItemClickListener itemClickListener) {
        super(items, itemClickListener);
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        BaseRecyclerViewHolder holder = null;
        if (viewType == BaseItem.ITEM_SUPERVISOR) {
            LayoutItemSupervisorBinding binding = LayoutItemSupervisorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new SupervisorHolder(binding);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {

        if (holder instanceof SupervisorHolder){
            SupervisorModel model=(SupervisorModel) getItemAt(position);
            ((SupervisorHolder) holder).binding.setModel(model);
            ((SupervisorHolder) holder).binding.executePendingBindings();
        }

    }
    public class SupervisorHolder extends BaseRecyclerViewHolder {
        LayoutItemSupervisorBinding binding;
        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public SupervisorHolder(LayoutItemSupervisorBinding view) {
            super(view.getRoot(), true);
            binding=view;
        }
        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (getItemClickListener() != null)
                getItemClickListener().onRecyclerViewItemClick(this);
        }
    }
    public int getItemViewType(int position) {
        return getItemAt(position).getItemType();
    }

}
