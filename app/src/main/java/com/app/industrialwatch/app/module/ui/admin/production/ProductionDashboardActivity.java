package com.app.industrialwatch.app.module.ui.admin.production;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.GridItemModel;
import com.app.industrialwatch.app.module.ui.adapter.GridItemAdapter;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.databinding.ActivityProductionDashboardBinding;

import java.util.ArrayList;

public class ProductionDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityProductionDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductionDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initGridView();
    }

    private void initGridView() {
        binding.gridLayout.girdView.setAdapter(new GridItemAdapter(this, fillGridItems(), this));
    }

    private ArrayList<GridItemModel> fillGridItems() {
        ArrayList<GridItemModel> gridItemModels = new ArrayList<>();
        gridItemModels.add(new GridItemModel(getString(R.string.material), R.drawable.ic_raw_materials));
        gridItemModels.add(new GridItemModel(getString(R.string.add_product), R.drawable.ic_add_product));
        gridItemModels.add(new GridItemModel(getString(R.string.inventory), R.drawable.ic_inventory));
        gridItemModels.add(new GridItemModel(getString(R.string.batch), R.drawable.ic_batch));
        return gridItemModels;

    }

    @Override
    public void onClick(View v) {

        if ((int) v.getTag() == 0) {
            startActivity(new Intent(getApplicationContext(), RawMaterialActivity.class));
        } else if ((int) v.getTag() == 1) {
            startActivity(new Intent(getApplicationContext(), ProductActivity.class));
        } else if ((int) v.getTag() == 2) {
            startActivity(new Intent(getApplicationContext(), InventoryActivity.class));
        } else if ((int) v.getTag() == 3) {
            startActivity(new Intent(getApplicationContext(), ProductionActivity.class));
        }
    }
}