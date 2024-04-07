package com.app.industrialwatch.app.module.ui.admin.production;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.business.BaseItem;
import com.app.industrialwatch.app.data.models.RawMaterialModel;
import com.app.industrialwatch.app.module.ui.adapter.ProductionAdapter;
import com.app.industrialwatch.common.base.recyclerview.OnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class AddBatchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_batch);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        /*List<BaseItem> baseItems = new ArrayList<>();
        baseItems.add(new RawMaterialModel("Iron", "16 KG", "100 G", 100));
        baseItems.add(new RawMaterialModel("Copper", "4 KG", "500 G", 5000));
        recyclerView.setAdapter(new ProductionAdapter(baseItems, null));
*/
        findViewById(R.id.btn_add_batch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateBatchDialogue();
            }
        });
    }

    private void showCreateBatchDialogue(){
        AlertDialog.Builder builder=new AlertDialog.Builder(AddBatchActivity.this);
        View customLayout = getLayoutInflater().inflate(R.layout.layout_dialoge_new_material, null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}