package com.app.industrialwatch.common.base.recyclerview;

import android.util.Log;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.industrialwatch.common.base.BaseActivity;
import com.app.industrialwatch.databinding.LayoutRecyclerviewBinding;

public class BaseRecyclerViewActivity extends BaseActivity {
    RecyclerView recyclerView;
    public void initRecyclerView(RecyclerView recyclerView){
        this.recyclerView=recyclerView;
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.setHasFixedSize(true);
    }

    public void initRecyclerViewForGrid(RecyclerView recyclerView,int spanCount) {
        this.recyclerView = recyclerView;
        this.recyclerView.setLayoutManager(new GridLayoutManager(this,spanCount));
        this.recyclerView.setHasFixedSize(true);
    }
    public void setRecyclerViewDivider(){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
    public void setAdapter(BaseRecyclerViewAdapter adapter){
        if (recyclerView!=null)
            recyclerView.setAdapter(adapter);
    }
}
