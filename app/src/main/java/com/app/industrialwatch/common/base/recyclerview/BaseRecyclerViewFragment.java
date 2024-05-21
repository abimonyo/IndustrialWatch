package com.app.industrialwatch.common.base.recyclerview;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.industrialwatch.common.base.BaseFragment;

public class BaseRecyclerViewFragment extends BaseFragment {
    RecyclerView recyclerView;
    public void initRecyclerView(RecyclerView recyclerView){
        this.recyclerView=recyclerView;
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
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
