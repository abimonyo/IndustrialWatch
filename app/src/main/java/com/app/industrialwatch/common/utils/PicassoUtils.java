package com.app.industrialwatch.common.utils;

import android.content.Context;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class PicassoUtils {
    public static boolean isAllowedPicassoRequest(Context context, ImageView imageView, String url) {
        return context == null || imageView == null || !AppUtils.ifNotNullEmpty(url);
    }

    public static RequestCreator getPicassoRequestCreator(Context context, String url, int fallbackResourceId) {
        RequestCreator picassoRequestCreator = Picasso.get().load(url);
        if (fallbackResourceId > 0) {
            picassoRequestCreator = picassoRequestCreator.error(fallbackResourceId).placeholder(fallbackResourceId);
        }
        return picassoRequestCreator.memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE);
        // return picassoRequestCreator.memoryPolicy(MemoryPolicy.NO_CACHE);
    }
    public static void picassoLoadImageFromUrl(Context context, final ImageView imageView, final String url,
                                               final int errorResId) {
        if (isAllowedPicassoRequest(context, imageView, url)) return;
        getPicassoRequestCreator(context, url, errorResId).into(imageView);
    }
    public static void picassoLoadImageOrPlaceHolder(Context context, final ImageView imageView, final String url,
                                                     final int errorResId, int width,int height) {
        if (!URLUtil.isValidUrl(url) || width <= 0|| height <= 0) {
            imageView.setImageResource(errorResId);
        } else {
            RequestCreator picassoRequestCreator = getPicassoRequestCreator(context, url, errorResId);
            picassoRequestCreator = picassoRequestCreator.resize(width, height);
            picassoRequestCreator.into(imageView);
        }
    }
}
