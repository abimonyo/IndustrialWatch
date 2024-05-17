package com.app.industrialwatch.app.module.ui.adapter;

import static com.app.industrialwatch.app.business.BaseItem.ITEM_EMPLOYEE_ATTENDANCE;
import static com.app.industrialwatch.app.business.BaseItem.ITEM_EMPLOYEE_RANKING;
import static com.app.industrialwatch.app.business.BaseItem.ITEM_EMPLOYEE_RECORD;
import static com.app.industrialwatch.app.business.BaseItem.ITEM_RAW_METERIAL;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.EmployeeModel;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.PicassoUtils;
import com.app.industrialwatch.databinding.LayoutItemAttendanceBinding;
import com.app.industrialwatch.databinding.LayoutItemEmployeeRecordBinding;
import com.app.industrialwatch.databinding.LayoutItemRankingBinding;
import com.app.industrialwatch.databinding.LayoutItemRuleBinding;
import com.app.industrialwatch.databinding.LayoutRulesTableBinding;
import com.app.industrialwatch.databinding.LayoutSectionItemBinding;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class EmployeeAdapter extends BaseRecyclerViewAdapter {
    Context context;

    public EmployeeAdapter(List<BaseItem> items, OnRecyclerViewItemClickListener itemClickListener, Context context) {
        super(items, itemClickListener);
        this.context = context;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseRecyclerViewHolder holder = null;
        if (viewType == ITEM_EMPLOYEE_RECORD) {
            LayoutItemEmployeeRecordBinding binding = LayoutItemEmployeeRecordBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new EmployeeRecordHolder(binding);
        } else if (viewType == ITEM_EMPLOYEE_ATTENDANCE) {
            LayoutItemAttendanceBinding binding = LayoutItemAttendanceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new EmployeeAttendanceHolder(binding);
        } else if (viewType == ITEM_EMPLOYEE_RANKING) {
            LayoutItemRankingBinding binding = LayoutItemRankingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            holder = new EmployeeRankingHolder(binding);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof EmployeeRecordHolder) {
            EmployeeModel model = (EmployeeModel) getItemAt(position);
            ((EmployeeRecordHolder) holder).binding.tvEmployeeName.setText(model.getName());
            ((EmployeeRecordHolder) holder).binding.tvEmployeeRole.setText(model.getJobRole());
            try {
                PicassoUtils.picassoLoadImageOrPlaceHolder(context, ((EmployeeRecordHolder) holder).binding.ivEmployeeRecord,
                        AppConstants.BASE_URL + AppConstants.IMAGE_URL + URLEncoder.encode(model.getImageUrl(), "UTF-8").replace("+", "%20"),
                        R.drawable.baseline_person_24, 159, 125);
            } catch (UnsupportedEncodingException e) {
                Log.d("error==>>", e.getMessage());
            }
            ((EmployeeRecordHolder) holder).binding.employeeProductivity.setText(String.valueOf(model.getProductivity() + "%"));
        } else if (holder instanceof EmployeeAttendanceHolder) {
            EmployeeModel model = (EmployeeModel) getItemAt(position);
            ((EmployeeAttendanceHolder) holder).binding.tvDate.setText(model.getAttendanceDate());
            ((EmployeeAttendanceHolder) holder).binding.tvStatus.setText(model.getStatus());
        } else if (holder instanceof EmployeeRankingHolder) {
            EmployeeModel model = (EmployeeModel) getItemAt(position);
            ((EmployeeRankingHolder) holder).setData(model, position);
        }

    }

    public class EmployeeRecordHolder extends BaseRecyclerViewHolder {
        LayoutItemEmployeeRecordBinding binding;

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public EmployeeRecordHolder(LayoutItemEmployeeRecordBinding view) {
            super(view.getRoot(), true);
            binding = view;
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (getItemClickListener() != null)
                getItemClickListener().onRecyclerViewItemClick(this);
        }
    }

    public class EmployeeAttendanceHolder extends BaseRecyclerViewHolder {
        LayoutItemAttendanceBinding binding;

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public EmployeeAttendanceHolder(LayoutItemAttendanceBinding view) {
            super(view.getRoot(), true);
            binding = view;
        }
    }

    public class EmployeeRankingHolder extends BaseRecyclerViewHolder {
        LayoutItemRankingBinding binding;

        @Override
        protected BaseRecyclerViewHolder populateView() {
            return null;
        }

        public EmployeeRankingHolder(LayoutItemRankingBinding view) {
            super(view.getRoot(), true);
            binding = view;
        }

        public void setData(EmployeeModel model, int pos) {
            if (pos == 0)
                binding.ivRank.setImageDrawable(context.getDrawable(R.drawable.img_rank_1));
            else if (pos == 1)
                binding.ivRank.setImageDrawable(context.getDrawable(R.drawable.img_rank_2));
            else if (pos == 2)
                binding.ivRank.setImageDrawable(context.getDrawable(R.drawable.img_rank_3));
            else
                binding.ivRank.setVisibility(View.GONE);
            binding.tvEmployeeName.setText(model.getName());
            binding.tvProductivity.setText(model.getProductivity() + "%");
            try {
                PicassoUtils.picassoLoadImageOrPlaceHolder(context, binding.ivEmployeeImage,
                        AppConstants.BASE_URL + AppConstants.IMAGE_URL + URLEncoder.encode(model.getImageUrl(), "UTF-8").replace("+", "%20"),
                        R.drawable.img_place_holder_ranking, 52, 52);
            } catch (UnsupportedEncodingException e) {
                Log.d("error==>>", e.getMessage());
            }
        }
    }

    public int getItemViewType(int position) {
        return getItemAt(position).getItemType();
    }

}
