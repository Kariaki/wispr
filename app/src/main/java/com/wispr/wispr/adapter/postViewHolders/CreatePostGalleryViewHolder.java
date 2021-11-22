package com.wispr.wispr.adapter.postViewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.mainPages.GalleryItem;
import com.wispr.wispr.R;


public class CreatePostGalleryViewHolder extends MainViewHolder {
    private ImageView makepostImage, MakePostGalleryMark;

    View view;
    public CreatePostGalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        makepostImage = itemView.findViewById(R.id.makepostImage);
        MakePostGalleryMark = itemView.findViewById(R.id.MakePostGalleryMark);
        view=itemView;
    }

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {

        CreatePostImageListener listener = (CreatePostImageListener) clickListeners;
        GalleryItem galleryItem=(GalleryItem) types;
        Glide.with(context).load(galleryItem.getFileURL()).into(makepostImage);
        MakePostGalleryMark.setOnClickListener(v -> {
            listener.removeImage(getAdapterPosition());
        });
        makepostImage.setOnClickListener(v -> {
            listener.onClickItem(getAdapterPosition());
        });

    }
}
