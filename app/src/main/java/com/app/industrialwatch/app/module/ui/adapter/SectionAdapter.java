package com.app.industrialwatch.app.module.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.RulesModel;
import com.app.industrialwatch.app.data.models.SectionDetailsModel;
import com.app.industrialwatch.app.data.models.SectionModel;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.databinding.LayoutItemRuleBinding;
import com.app.industrialwatch.databinding.LayoutRulesTableBinding;
import com.app.industrialwatch.databinding.LayoutSectionItemBinding;

import java.util.List;

public class SectionAdapter extends BaseRecyclerViewAdapter {


    public SectionAdapter(List<BaseItem> items, OnRecyclerViewItemClickListener itemClickListener) {
        super(items, itemClickListener);
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        BaseRecyclerViewHolder holder = null;
        if (viewType == BaseItem.SECTION_DETAILS) {
            LayoutRulesTableBinding binding = LayoutRulesTableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new ViewRulesHolder(binding);
        } else if (viewType == BaseItem.SECTION) {
            LayoutSectionItemBinding binding = LayoutSectionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new SectionHolder(binding);
        } else if (viewType == BaseItem.ITEM_RULE) {
            LayoutItemRuleBinding binding = LayoutItemRuleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new RuleHolder(binding);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof SectionHolder) {
            ((SectionHolder) holder).binding.setModel((SectionModel) getItemAt(position));
            ((SectionHolder) holder).binding.executePendingBindings();
        } else if (holder instanceof ViewRulesHolder) {
            RulesModel model = (RulesModel) getItemAt(position);
            ((ViewRulesHolder) holder).binding.tvIndexItem.setText(String.valueOf(position));
            ((ViewRulesHolder) holder).binding.tvTopItem.setText(model.getName());
            ((ViewRulesHolder) holder).binding.tvBelowItem.setText(model.getTimeAsTime());
            ((ViewRulesHolder) holder).binding.tvEndItem.setText(model.getFineAsRS());
            ((ViewRulesHolder) holder).binding.executePendingBindings();
        } else if (holder instanceof RuleHolder) {
            ((RuleHolder) holder).checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                RulesModel model = (RulesModel) getItemAt(position);
                model.setChecked(isChecked);
                holder.onClick(buttonView);
            });
           /* if ((RulesModel) getItemAt(position) != null) {
                ((RuleHolder) holder).fillData((RulesModel) getItemAt(position));
            }*/
            ((RuleHolder) holder).binding.setModel((RulesModel) getItemAt(position));
            ((RuleHolder) holder).binding.executePendingBindings();
        }
    }

    public class SectionHolder extends BaseRecyclerViewHolder {
        LayoutSectionItemBinding binding;

        public SectionHolder(LayoutSectionItemBinding view) {
            super(view.getRoot(), true);
            binding = view;
            view.getRoot().setOnClickListener(this);

        }

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (getItemClickListener() != null)
                getItemClickListener().onRecyclerViewItemClick(SectionHolder.this);
        }
    }

    public class ViewRulesHolder extends BaseRecyclerViewHolder {
        LayoutRulesTableBinding binding;

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public ViewRulesHolder(LayoutRulesTableBinding view) {
            super(view.getRoot());
            binding = view;
        }
    }

    public class RuleHolder extends BaseRecyclerViewHolder {
        LayoutItemRuleBinding binding;
        EditText etFine, etTime;
        CheckBox checkBox;

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public RuleHolder(LayoutItemRuleBinding view) {
            super(view);
            binding = view;
            etFine = binding.etRuleItemFine;
            etTime = binding.etRuleItemTime;
            checkBox = binding.cbRuleItem;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (getItemClickListener() != null)
                getItemClickListener().onRecyclerViewItemClick(RuleHolder.this);
        }

    }

    public int getItemViewType(int position) {
        return getItemAt(position).getItemType();
    }

}
