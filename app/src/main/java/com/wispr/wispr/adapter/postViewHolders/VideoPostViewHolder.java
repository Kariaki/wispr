package com.wispr.wispr.adapter.postViewHolders;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.Post;
import com.wispr.wispr.R;
import com.wispr.wispr.util.LibraryFunction;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoPostViewHolder extends MainViewHolder {

    private CircleImageView ownerImage;
    private TextView ownerName, thumbsUpCount, thumbsDownCount, commentCount, playTime, post_time;
    private ImageView comment, playState, optionIcon;
    private SeekBar playProgress;
    private TextView post_caption;
    private AppCompatToggleButton thumbsUp, thumbsDown;

    public VideoPostViewHolder(@NonNull View itemView) {
        super(itemView);
        viewById(itemView);

    }

    private void viewById(View view) {
        ownerImage = view.findViewById(R.id.ownerImage);
        thumbsUp = view.findViewById(R.id.thumbsUp);
        thumbsDownCount = view.findViewById(R.id.thumbsDownCount);
        optionIcon = view.findViewById(R.id.optionIcon);
        ownerName = view.findViewById(R.id.ownerName);
        playTime = view.findViewById(R.id.playTime);
        post_time = view.findViewById(R.id.post_time);
        playProgress = view.findViewById(R.id.playProgress);
        playState = view.findViewById(R.id.playState);
        comment = view.findViewById(R.id.comment);
        post_caption = view.findViewById(R.id.post_caption);
        commentCount = view.findViewById(R.id.commentCount);
        thumbsUpCount = view.findViewById(R.id.thumbsUpCount);
        thumbsDown = view.findViewById(R.id.thumbsDown);
    }

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {

        PostClickListeners postClickListeners = (PostClickListeners) clickListeners;
        Post post = (Post) types;
        comment.setOnClickListener(v -> {
            postClickListeners.onClickComment(getAdapterPosition());
        });

        LibraryFunction.commentCount(post.getPostID(), commentCount);
        LibraryFunction.likeCount(post.getPostID(), thumbsUpCount, true);

        LibraryFunction.spanString(post_caption, post.getPostCaption(), "#");
        LibraryFunction.likeCount(post.getPostID(), thumbsDownCount, false);
        thumbsDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                YoYo.with(Techniques.Pulse).duration(200).playOn(thumbsUp);
                LibraryFunction.reaction(post.getPostID(), post.getPostUserID(), false, isChecked);

            }
        });
        thumbsUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                YoYo.with(Techniques.Pulse).duration(200).playOn(thumbsUp);
                LibraryFunction.reaction(post.getPostID(), post.getPostUserID(), true, isChecked);

            }
        });

    }
}
