package com.app.industrialwatch.app.module.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.industrialwatch.R;
import com.app.industrialwatch.app.data.models.ViolationModel;
import com.app.industrialwatch.common.utils.AppConstants;
import com.app.industrialwatch.common.utils.PicassoUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<ViolationModel.ImageModel> imageList;
    private LayoutInflater inflater;
    Context context;
    public SliderAdapter(Context context, List<ViolationModel.ImageModel> imageList) {
        this.imageList = imageList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view = inflater.inflate(R.layout.layout_item_slider, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        String imageUrl = imageList.get(position).getImageUrl();
        String correctedImageUrl = imageUrl.replace("\\", "/");
        Picasso.get().load(AppConstants.BASE_URL + AppConstants.VIOLATION_IMAGES + correctedImageUrl).into(holder.imageView);
       /* PicassoUtils.picassoLoadImageOrPlaceHolder(context,holder.imageView,
                AppConstants.BASE_URL + AppConstants.VIOLATION_IMAGES + correctedImageUrl,
                R.drawable.img_place_holder_ranking,300,300);*/
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item_slider);
        }
    }
}
