package com.wispr.wispr.adapter.postViewHolders;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rd.PageIndicatorView;
import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.postViewHolders.imagePost.ImagePostPagerAdapter;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.Post;
import com.wispr.wispr.R;
import com.wispr.wispr.util.LibraryFunction;
import com.wispr.wispr.util.TimeFactor;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImagePostViewHolder extends MainViewHolder {

    private TextView caption, post_time, ownerName, thumbsUpCount, thumbsDownCount, commentCount,ownerUserName;
    private ImageView optionIcon, comment;
    ViewPager postImage;
    private CircleImageView ownerImage;
    private ImagePostPagerAdapter adapter;
    private PageIndicatorView pageIndicator;
    AppCompatToggleButton thumbsUp,thumbsDown;

    public ImagePostViewHolder(@NonNull View itemView) {
        super(itemView);
        caption = itemView.findViewById(R.id.post_text);
        post_time = itemView.findViewById(R.id.post_time);
        ownerName = itemView.findViewById(R.id.ownerName);
        thumbsUpCount = itemView.findViewById(R.id.thumbsUpCount);
        thumbsDownCount = itemView.findViewById(R.id.thumbsDownCount);
        commentCount = itemView.findViewById(R.id.commentCount);
        pageIndicator = itemView.findViewById(R.id.pageIndicator);
        optionIcon = itemView.findViewById(R.id.optionIcon);
        ownerUserName=itemView.findViewById(R.id.ownerUserName);
        postImage = itemView.findViewById(R.id.postImage);
        thumbsUp = itemView.findViewById(R.id.thumbsUp);
        thumbsDown = itemView.findViewById(R.id.thumbsDown);
        comment = itemView.findViewById(R.id.comment);
        ownerImage = itemView.findViewById(R.id.ownerImage);

    }

    DatabaseReference postFoler = FirebaseDatabase.getInstance().getReference("post");

    private List<String> previewImages = new ArrayList<>();

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {
        Post post = (Post) types;
        adapter = new ImagePostPagerAdapter(context);
        String[] postUrl = post.getPostUrl().split(",");

        adapter.setPreviewImages(postUrl);
        postImage.setAdapter(adapter);

        PostClickListeners postClickListeners = (PostClickListeners) clickListeners;
        LibraryFunction.spanString(caption,post.getPostCaption(),"#");

        ownerImage.setOnClickListener(v->{
            postClickListeners.onProfileClick(getAdapterPosition());
        });
        ownerName.setOnClickListener(v->{
            postClickListeners.onProfileClick(getAdapterPosition());
        });
        comment.setOnClickListener(v -> {
            postClickListeners.onClickComment(getAdapterPosition());
        });
        LibraryFunction.fetchUserDetail(post.getPostUserID(),ownerName,ownerUserName,null,ownerImage,context);


        optionIcon.setOnClickListener(v -> {
            postClickListeners.onOptionClick(getAdapterPosition());
        });

        String current_time = TimeFactor.getDetailedDate(post.getPostTime(), System.currentTimeMillis());
        post_time.setText(current_time);

        //  pageIndicator.setCount(adapter.getCount());
        // pageIndicator.setSelection(1);
        postImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageIndicator.setSelection(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        LibraryFunction.commentCount(post.getPostID(), commentCount);
        LibraryFunction.likeCount(post.getPostID(), thumbsUpCount, true);

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
              //  YoYo.with(Techniques.Pulse).duration(200).playOn(thumbsUp);
                LibraryFunction.reaction(post.getPostID(), post.getPostUserID(), true, isChecked);

            }
        });



    }
}
