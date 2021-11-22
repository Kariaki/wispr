package com.wispr.wispr.adapter.postViewHolders.imagePost;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.wispr.wispr.R;

public class ImagePostPagerAdapter extends PagerAdapter {

    String[]previewImages=new String[0];

    public void setPreviewImages(String[] previewImages) {
        this.previewImages = previewImages;
    }

    public String[] getPreviewImages() {
        return previewImages;
    }
    Context CONTEXT;
    public ImagePostPagerAdapter(Context context){
        this.CONTEXT=context;
    }

    @Override
    public int getCount() {
        return previewImages.length;
    }

    private ImageView image;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(CONTEXT).inflate(R.layout.image_page, container, false);
        image = view.findViewById(R.id.previewIMage);
        //Glide.with(container.getContext()).load(previewImages.get(position)).into(image);
        Picasso.get().load(Uri.parse(previewImages[position])).into(image);

        container.addView(view);

        return view;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view.equals((View)object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
