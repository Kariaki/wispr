package com.wispr.wispr.adapter.postViewHolders;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;

import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.Post;
import com.wispr.wispr.R;
import com.wispr.wispr.util.LibraryFunction;
import com.wispr.wispr.util.TimeFactor;

import de.hdodenhof.circleimageview.CircleImageView;

public class TextPostViewHolder extends MainViewHolder {

    private TextView post_text, ownerName, post_time, commentCount, thumbsDownCount, thumbsUpCount, ownerUserName;
    private CircleImageView ownerImage;
    private ImageView optionIcon, comment;
    private AppCompatToggleButton thumbsUp,thumbsDown;

    public TextPostViewHolder(@NonNull View itemView) {
        super(itemView);
        post_text = itemView.findViewById(R.id.post_text);
        ownerImage = itemView.findViewById(R.id.ownerImage);
        optionIcon = itemView.findViewById(R.id.optionIcon);
        post_time = itemView.findViewById(R.id.post_time);
        comment = itemView.findViewById(R.id.comment);
        thumbsUp = itemView.findViewById(R.id.thumbsUp);
        thumbsUpCount = itemView.findViewById(R.id.thumbsUpCount);
        ownerUserName = itemView.findViewById(R.id.ownerUserName);
        thumbsDownCount = itemView.findViewById(R.id.thumbsDownCount);
        thumbsDown = itemView.findViewById(R.id.thumbsDown);
        commentCount = itemView.findViewById(R.id.commentCount);
        ownerName = itemView.findViewById(R.id.ownerName);
    }

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {

        PostClickListeners postClickListeners = (PostClickListeners) clickListeners;
        itemView.setOnClickListener(v -> {
            clickListeners.onClickItem(getAdapterPosition());

        });
        Post post = (Post) types;
       // post_text.setText(post.getPostCaption());
        //spanning caption to display

        ownerImage.setOnClickListener(v->{
            postClickListeners.onProfileClick(getAdapterPosition());
        });
        ownerName.setOnClickListener(v->{
            postClickListeners.onProfileClick(getAdapterPosition());
        });
        LibraryFunction.spanString(post_text,post.getPostCaption(),"#");

        String current_time = TimeFactor.getDetailedDate(post.getPostTime(), System.currentTimeMillis());
        post_time.setText(current_time);

        comment.setOnClickListener(v -> {
            postClickListeners.onClickComment(getAdapterPosition());
        });
        optionIcon.setOnClickListener(v -> {
            postClickListeners.onOptionClick(getAdapterPosition());
        });
        LibraryFunction.commentCount(post.getPostID(), commentCount);
        LibraryFunction.likeCount(post.getPostID(), thumbsUpCount, true);
        LibraryFunction.fetchUserDetail(post.getPostUserID(), ownerName, ownerUserName, null, ownerImage, context);


        LibraryFunction.likeCount(post.getPostID(), thumbsDownCount, false);
        thumbsDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               // YoYo.with(Techniques.Pulse).duration(200).playOn(thumbsUp);
                LibraryFunction.reaction(post.getPostID(), post.getPostUserID(), false, isChecked);

            }
        });
        thumbsUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               // YoYo.with(Techniques.Pulse).duration(200).playOn(thumbsUp);
                LibraryFunction.reaction(post.getPostID(), post.getPostUserID(), true, isChecked);

            }
        });

    }

}
