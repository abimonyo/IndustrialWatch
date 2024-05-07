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
import com.app.industrialwatch.app.data.models.BatchModel;
import com.app.industrialwatch.app.data.models.RawMaterialModel;
import com.app.industrialwatch.app.data.models.StockModel;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.databinding.LayoutInventoryItmeBinding;
import com.app.industrialwatch.databinding.LayoutItemBatchBinding;
import com.app.industrialwatch.databinding.LayoutItemInventoryDetailsBinding;
import com.app.industrialwatch.databinding.LayoutItemRuleBinding;
import com.app.industrialwatch.databinding.LayoutRulesTableBinding;

import java.util.List;
import java.util.Map;
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
            LayoutItemBatchBinding inflated = LayoutItemBatchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new ProductBatchViewHolder(inflated);
        } else if (viewType == BaseItem.ITEM_INVENTORY) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            LayoutInventoryItmeBinding binding = LayoutInventoryItmeBinding.inflate(inflater, parent, false);
            holder = new InventoryViewHolder(binding);
        } else if (viewType == BaseItem.ITEM_INVENTORY_DETAIL) {
            LayoutItemInventoryDetailsBinding binding = LayoutItemInventoryDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new InventoryDetailViewHolder(binding);
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
        } else if (holder instanceof ProductBatchViewHolder) {
            BatchModel batchModel = (BatchModel) getItemAt(position);
            ((ProductBatchViewHolder) holder).filterData(batchModel);
        } else if (holder instanceof InventoryDetailViewHolder) {
            StockModel model = (StockModel) getItemAt(position);
            ((InventoryDetailViewHolder) holder).setData(model);
            ((InventoryDetailViewHolder) holder).binding.cbItem.setChecked(model.isChecked());
            ((InventoryDetailViewHolder) holder).binding.cbItem.setOnCheckedChangeListener((v, isChk) -> {
                model.setChecked(isChk);
                View view = v.getRootView();
                String result = ((InventoryDetailViewHolder) holder).binding.cbItem.isChecked() + "," + model;
                view.setTag(result);
                holder.onClick(view);

            });
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

    public class InventoryDetailViewHolder extends BaseRecyclerViewHolder {
        LayoutItemInventoryDetailsBinding binding;

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public InventoryDetailViewHolder(LayoutItemInventoryDetailsBinding view) {
            super(view, true);
            binding = view;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (getItemClickListener() != null)
                getItemClickListener().onRecyclerViewItemClick(ProductionAdapter.InventoryDetailViewHolder.this);
        }

        @SuppressLint("ResourceAsColor")
        public void setData(StockModel model) {
            binding.tvIndexItem.setText(model.getStock_number());
            binding.tvTopItem.setText(String.valueOf(model.getTotal_quantity()));
            binding.tvFourItem.setText(String.valueOf(model.getPrice_per_unit()));
            binding.tvEndItem.setText(model.getPurchased_date());
            binding.tvEndItem.setTextColor(R.color.black);
            binding.cbItem.setChecked(model.isChecked());
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
                binding.tvEndItem.setText(model.getTotal_quantity().contains("KG") ? "Choose stock" : "Details");
                binding.tvEndItem.getPaint().setUnderlineText(true);
                binding.tvEndItem.setOnClickListener(this);

            } else if (model.getPurchased_date() != null) {
                binding.tvIndexItem.setText(model.getStock_number());
                binding.tvTopItem.setText(String.valueOf(model.getTotal_quantity()));
                binding.tvFourItem.setText(String.valueOf(model.getPrice_per_unit()));
                binding.tvEndItem.setText(model.getPurchased_date());
                binding.tvEndItem.setTextColor(R.color.black);

            } else {
                binding.tvTopItem.setText(model.getRaw_material_name());
                binding.tvFourItem.setText(String.valueOf(model.getTotal_quantity()));
            }
        }
    }

    public class ProductBatchViewHolder extends BaseRecyclerViewHolder {
        LayoutItemBatchBinding binding;

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public ProductBatchViewHolder(LayoutItemBatchBinding view) {
            super(view, true);
            binding = view;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (getItemClickListener() != null)
                getItemClickListener().onRecyclerViewItemClick(this);
        }

        public void filterData(BatchModel batchModel) {
            if (batchModel.getBatch_number()!=null){
                if (batchModel.getStatus()==1)
                    binding.layoutWrapper.setBackgroundResource(R.color.red);
                else if (batchModel.getStatus()==0)
                    binding.layoutWrapper.setBackgroundResource(R.color.green);
                binding.tvItemName.setText(batchModel.getBatch_number());
            }
            else {
                binding.tvItemName.setText(batchModel.getName());
            }

            binding.ivIcon.setOnClickListener(this);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return getItemAt(position).getItemType();
    }
}
