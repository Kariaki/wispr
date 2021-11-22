package com.wispr.wispr.mainPages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.adapter.GeneralAdapter;
import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.postViewHolders.EmptyViewHolder;
import com.wispr.wispr.adapter.postViewHolders.ImagePostViewHolder;
import com.wispr.wispr.adapter.postViewHolders.PostClickListeners;
import com.wispr.wispr.adapter.postViewHolders.TextPostViewHolder;
import com.wispr.wispr.adapter.postViewHolders.VideoPostViewHolder;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.ItemTypes;
import com.wispr.wispr.entities.Post;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.modals.PostOption;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContentFeed extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public interface onClickIcons {
        void onClickComment(Post post);
        void onClickProfile(Post post);

    }



    private onClickIcons onClickIcons;

    public void setOnClickIcons(ContentFeed.onClickIcons onClickIcons) {
        this.onClickIcons = onClickIcons;
    }

    GeneralAdapter adapter;
    List<SuperEntity> postItems = new ArrayList<>();

    private RecyclerView feed_list;

    private LinearLayout post_loading;

    private DatabaseReference myFeed;
    private DatabaseReference allUser = FirebaseDatabase.getInstance().getReference("users");
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_feed, container, false);
        feed_list = view.findViewById(R.id.feed_list);
        adapter = new GeneralAdapter();
        post_loading = view.findViewById(R.id.post_loading);
        userId = User.getUserID(getContext());

        myFeed = allUser.child(
                userId

        ).child("feed");

        adapter.setItems(postItems);


        adapter.setViewHolderPlug(plug);
        adapter.setClickListeners(postClickListeners);

        LinearLayoutManager layout = new LinearLayoutManager(getContext());

        feed_list.setLayoutManager(layout);
        feed_list.setHasFixedSize(true);

        feed_list.setAdapter(adapter);
        fetchPostToFeed();

        postModelView();

        return view;
    }

    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("post");

    private void fetchPostToFeed() {

        postReference.keepSynced(false);
        postReference
                .orderByChild("postTime").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot post : snapshot.getChildren()) {
                        Post currentPost = post.getValue(Post.class);
                        if (currentPost != null) {
                          //  checkTags(currentPost);
                            if(!postIds.contains(currentPost.getPostID())){
                                postItems.add(currentPost);
                                adapter.notifyDataSetChanged();
                                postIds.add(currentPost.getPostID());
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private List<String> postTags = new ArrayList<>();
    private List<String> myTags = new ArrayList<>();

    private void checkTags(Post post) {

        postReference.child(post.getPostID()).child("tags")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot currentTag : snapshot.getChildren()) {
                                String tag = currentTag.getValue(String.class);
                                postTags.add(tag);
                            }
                            processTags(postTags, post.getPostID());
                            postTags.clear();
                        } else {
                            addToFeed(post.getPostID());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    private void processTags(List<String> pTags, String postId) {
        //work on process tags
        addToFeed(postId);
    }

    private List<String> postIds = new ArrayList<>();

    private void postModelView() {
        myFeed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot newPost : snapshot.getChildren()) {

                        String postId = newPost.getKey();
                        String currentPostId = newPost.getValue(String.class);
                        if (!postIds.contains(postId)) {

                            postReference.child(currentPostId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                Post currentPost = snapshot.getValue(Post.class);
                                                postIds.add(newPost.getKey());
                                                postItems.add(0, currentPost);
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                newPost.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                        }
                                    });
                        }
                        post_loading.setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void addToFeed(String postId) {
        myFeed.child(postId).setValue(postId);
    }

    PostClickListeners postClickListeners = new PostClickListeners() {

        @Override
        public void onProfileClick(int position) {
            onClickIcons.onClickProfile((Post)postItems.get(position));

        }

        @Override
        public void onClickComment(int position) {


            onClickIcons.onClickComment((Post) postItems.get(position));


        }

        @Override
        public void onOptionClick(int position) {
            PostOption postOption = new PostOption();
            postOption.show(getChildFragmentManager(), null);

        }

        @Override
        public void onClickItem(int position) {

        }
    };
    GeneralAdapter.viewHolderPlug plug = new GeneralAdapter.viewHolderPlug() {
        @Override
        public MainViewHolder setPlug(ViewGroup group, int viewType) {

            switch (viewType) {
                case ItemTypes
                        .TEXT_POST:
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.text_post_design, group, false);
                    return new TextPostViewHolder(view);
                case ItemTypes
                        .IMAGE_POST:
                    view = LayoutInflater.from(getContext()).inflate(R.layout.image_post_design, group, false);
                    return new ImagePostViewHolder(view);
                case ItemTypes
                        .VIDEO_POST:
                    view = LayoutInflater.from(getContext()).inflate(R.layout.video_post, group, false);
                    return new VideoPostViewHolder(view);
                default:
                    view = LayoutInflater.from(getContext()).inflate(R.layout.empty_view_holder, group, false);
                    return new EmptyViewHolder(view);
            }
        }
    };
}