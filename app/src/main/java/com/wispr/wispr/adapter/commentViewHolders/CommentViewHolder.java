package com.wispr.wispr.adapter.commentViewHolders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.Comment;
import com.wispr.wispr.R;
import com.wispr.wispr.util.LibraryFunction;
import com.wispr.wispr.util.TimeFactor;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends MainViewHolder {
    private CircleImageView ownerImage;
    private TextView senderName, commentContent, commentTime, reply;
    private ImageView thumbsUp, thumbsDown;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        ownerImage = itemView.findViewById(R.id.ownerImage);
        senderName = itemView.findViewById(R.id.senderName);
        commentContent = itemView.findViewById(R.id.commentContent);
        commentTime = itemView.findViewById(R.id.commentTime);
        reply = itemView.findViewById(R.id.reply);
        thumbsUp = itemView.findViewById(R.id.thumbsUp);
        thumbsDown = itemView.findViewById(R.id.thumbsDown);
    }

    @Override
    public void bindPostType(SuperEntity types, Context context, SuperClickListeners clickListeners) {

        Comment comment = (Comment) types;
        commentContent.setText(comment.getCommentContent());

        String currentTime = TimeFactor.getDetailedDate(comment.getCommentTime(), System.currentTimeMillis());
        commentTime.setText(currentTime);
        LibraryFunction.fetchUserDetail(comment.getCommentOwnerID(), senderName, senderName, null, ownerImage, context);

        CommentClickListener commentClickListener = (CommentClickListener) clickListeners;
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                commentClickListener.onClickItem(getAdapterPosition());
                return false;
            }
        });
        ownerImage.setOnClickListener(v -> {
            commentClickListener.onUserClick(getAdapterPosition());
        });
        senderName.setOnClickListener(v -> {
            commentClickListener.onUserClick(getAdapterPosition());
        });

    }
}
