package com.app.industrialwatch.common.base.recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

abstract public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    /**
     * Initialize view if necessary set their click listeners.
     *
     * @return view holder
     */
    protected abstract BaseRecyclerViewHolder populateView();

    /**
     * Default constructor
     *
     * @param view {@link View}
     */
    public BaseRecyclerViewHolder(View view) {
        this(view, false);
    }
    public BaseRecyclerViewHolder(ViewDataBinding view) {
        this(view.getRoot(), false);
    }

    public BaseRecyclerViewHolder(ViewDataBinding view,boolean isViewClickable) {
        this(view.getRoot());
        if (isViewClickable) {
            View itemView=view.getRoot();
            itemView.setOnClickListener(this);
        }
    }

    /**
     * Set click listen of given view
     *
     * @param view            view
     * @param isViewClickable true clickable view, else not
     */
    public BaseRecyclerViewHolder(View view, boolean isViewClickable) {
        super(view);
        if (isViewClickable)
            view.setOnClickListener(this);
    }

    /**
     * Override this method if you want to implement click listener for your recycler view items
     * a.k.a recycler view single item click listener
     *
     * @param v view
     */
    @Override
    public void onClick(View v) {

    }


    public TextView findTextViewById(View view, int resourceId) {
        return (TextView) view.findViewById(resourceId);
    }
}
