package com.app.industrialwatch.app.module.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.app.industrialwatch.BR;
import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.RawMaterialModel;
import com.app.industrialwatch.app.data.models.StockModel;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.databinding.LayoutInventoryItmeBinding;
import com.app.industrialwatch.databinding.LayoutItemRuleBinding;
import com.app.industrialwatch.databinding.LayoutRulesTableBinding;

import java.util.List;
import java.util.Objects;

public class ProductionAdapter extends BaseRecyclerViewAdapter {


    public ProductionAdapter(List<BaseItem> items, OnRecyclerViewItemClickListener itemClickListener) {
        super(items, itemClickListener);
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        BaseRecyclerViewHolder holder = null;
        if (viewType == BaseItem.ITEM_RAW_METERIAL) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ViewDataBinding viewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_item_raw_materials, parent, false);
            holder = new RawMaterialViewHolder(viewDataBinding);
        } else if (viewType == BaseItem.ITEM_BATCH_NUMBER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_batch, parent, false);
            //holder = new BatchViewHolder(view);
        } else if (viewType == BaseItem.ITEM_INVENTORY) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            LayoutInventoryItmeBinding binding = LayoutInventoryItmeBinding.inflate(inflater, parent, false);
            holder = new InventoryViewHolder(binding);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof RawMaterialViewHolder) {
            RawMaterialViewHolder batchViewHolder = (RawMaterialViewHolder) holder;
            batchViewHolder.bind((RawMaterialModel) getItemAt(position));
        } else if (holder instanceof InventoryViewHolder) {
            StockModel model = (StockModel) getItemAt(position);
            ((InventoryViewHolder) holder).filterData(model);
        }
    }

    public class RawMaterialViewHolder extends BaseRecyclerViewHolder {
        ViewDataBinding binding;


        public RawMaterialViewHolder(ViewDataBinding view) {
            super(view, true);
            binding = view;
        }

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return RawMaterialViewHolder.this;
        }

        public void bind(Object item) {
            Objects.requireNonNull(item);
            binding.setVariable(BR.model, item);
            binding.executePendingBindings();
        }

    }

    public class InventoryViewHolder extends BaseRecyclerViewHolder {
        LayoutInventoryItmeBinding binding;


        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public InventoryViewHolder(LayoutInventoryItmeBinding view) {
            super(view, false);
            binding = view;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (getItemClickListener() != null)
                getItemClickListener().onRecyclerViewItemClick(ProductionAdapter.InventoryViewHolder.this);
        }

        @SuppressLint("ResourceAsColor")
        public void filterData(StockModel model) {
            binding.tvIndexItem.setText(String.valueOf(this.getLayoutPosition() + 1));
            if (model.getRaw_material_id() > 0) {
                binding.tvTopItem.setText(model.getRaw_material_name());
                binding.tvFourItem.setText(String.valueOf(model.getTotal_quantity()));
                binding.tvEndItem.setText("Details");
                binding.tvEndItem.getPaint().setUnderlineText(true);
                binding.tvEndItem.setOnClickListener(this);

            } else if (model.getPurchased_date()!=null){
                binding.tvTopItem.setText(String.valueOf(model.getQuantity()));
                binding.tvFourItem.setText(String.valueOf(model.getPrice_per_unit()));
                binding.tvEndItem.setText(model.getPurchased_date());
                binding.tvEndItem.setTextColor(R.color.black);

            }else {
                binding.tvTopItem.setText(model.getRaw_material_name());
                binding.tvFourItem.setText(String.valueOf(model.getTotal_quantity()));
            }
        }
    }


    @Override
    public int getItemViewType(int position) {
        return getItemAt(position).getItemType();
    }
}
