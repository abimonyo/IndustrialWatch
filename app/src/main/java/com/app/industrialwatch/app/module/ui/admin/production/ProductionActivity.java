package com.app.industrialwatch.app.module.ui.admin.production;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.BatchModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.common.base.recyclerview.BaseRecyclerViewHolder;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;
import com.app.industrialwatch.databinding.ActivityAdminDashboardBinding;
import com.app.industrialwatch.databinding.ActivityProductionBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductionActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    ActivityProductionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production);
//        binding = ActivityProductionBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(false);
        List<BaseItem> baseItems = new ArrayList<>();
        baseItems.add(new BatchModel());
        recyclerView.setAdapter(new ProductionAdapter(baseItems, this));

        findViewById(R.id.btn_creat_batch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddBatchActivity.class));
            }
        });
    }

    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {

    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }
}