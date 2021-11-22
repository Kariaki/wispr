package com.wispr.wispr.mainPages;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.adapter.commentViewHolders.CommentClickListener;
import com.wispr.wispr.adapter.commentViewHolders.CommentViewHolder;
import com.wispr.wispr.adapter.GeneralAdapter;
import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.Comment;
import com.wispr.wispr.entities.ItemTypes;
import com.wispr.wispr.entities.Post;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.R;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {

    public CommentFragment() {
        // Required empty public constructor
    }

    RecyclerView commentList;
    GeneralAdapter generalAdapter;
    List<SuperEntity> allComments = new ArrayList<>();
    ImageView back_button;
    DatabaseReference postFolder = FirebaseDatabase.getInstance().getReference("post");
    DatabaseReference commentFolder;

    private Post post;

    public void setPost(Post post) {
        this.post = post;
    }

    public Post getPost() {

        return post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.comment, container, false);
        viewById(view);
        generalAdapter = new GeneralAdapter();
        generalAdapter.setViewHolderPlug(viewHolderPlug);
        commentFolder = postFolder.child(post.getPostID()).child("comments");

        CommentClickListener commentClickListener = new CommentClickListener() {
            @Override
            public void onUserClick(int position) {

            }

            @Override
            public void onReplyClick(int position) {

            }

            @Override
            public void onClickItem(int position) {

            }
        };
        generalAdapter.setClickListeners(commentClickListener);

        generalAdapter.setItems(allComments);
        commentList.setHasFixedSize(true);
        commentList.setLayoutManager(new LinearLayoutManager(getContext()));
        commentList.setAdapter(generalAdapter);

        back_button.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        sendButton.setEnabled(false);
        commentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                enableDisableButton(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableDisableButton(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableDisableButton(s.toString());
            }
        });

        loadComments();

        sendButton.setOnClickListener(v -> {
            sendComment();
        });
        return view;
    }

    EditText commentEdit;
    Button sendButton;

    private void viewById(View view) {
        commentList = view.findViewById(R.id.commentList);
        back_button = view.findViewById(R.id.back_button);
        commentEdit = view.findViewById(R.id.commentEdit);
        sendButton = view.findViewById(R.id.sendButton);
    }

    private void enableDisableButton(String s) {
        if (s.isEmpty()) {
            sendButton.setEnabled(false);
        } else {
            sendButton.setEnabled(true);
        }
    }

    private void loadComments() {
        commentFolder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot cloudComment : snapshot.getChildren()) {
                        Comment comment = cloudComment.getValue(Comment.class);
                        allComments.add(comment);
                        generalAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    GeneralAdapter.viewHolderPlug viewHolderPlug = new GeneralAdapter.viewHolderPlug() {
        @Override
        public MainViewHolder setPlug(ViewGroup group, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.comment_design, group, false);
            return new CommentViewHolder(view);
        }
    };

    private void sendComment() {
        String typedComment = commentEdit.getText().toString();
        String commentId = commentFolder.push().getKey();
        Comment comment = new Comment(System.currentTimeMillis(), typedComment, User.getUserID(getContext()), commentId, ItemTypes.COMMENT);
        commentFolder.child(commentId).setValue(comment);
        commentEdit.getText().clear();
        sendButton.setEnabled(false);


    }

}