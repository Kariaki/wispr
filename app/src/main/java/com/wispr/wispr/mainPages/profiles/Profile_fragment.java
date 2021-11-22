package com.wispr.wispr.mainPages.profiles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
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
import com.wispr.wispr.mainPages.ContentFeed;
import com.wispr.wispr.mainPages.EditProfile;
import com.wispr.wispr.mainPages.ViewProfilePicture;
import com.wispr.wispr.modals.DialogBox;
import com.wispr.wispr.modals.MyContentsOption;
import com.wispr.wispr.modals.ProfileOption;
import com.wispr.wispr.R;
import com.wispr.wispr.util.ImageExtension;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class Profile_fragment extends Fragment {


    public Profile_fragment() {
        // Required empty public constructor
    }

    public interface onClickEdit {
        void onEditClick();
    }

    private onClickEdit onClickEdit;

    public void setOnClickEdit(Profile_fragment.onClickEdit onClickEdit) {
        this.onClickEdit = onClickEdit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private FloatingActionButton new_content;
    private CircleImageView profileImage;
    private TextView displayName, userName, bio_text;
    private ImageView changeProfilePicture;
    private RecyclerView myContentList;
    private String profileUrl;
    private GeneralAdapter generalAdapter;
    private ImageView profileOption;
    private LinearLayout loader, uploading;
    private RelativeLayout namePlaceHolder;
    List<SuperEntity> myContents = new ArrayList<>();

    public interface onClickIcons {
        void onClickComment(Post post);

    }

    private ContentFeed.onClickIcons onClickIcons;

    public void setOnClickIcons(ContentFeed.onClickIcons onClickIcons) {
        this.onClickIcons = onClickIcons;
    }

    private String userID;
    private TextView namePlaceHolderText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        viewById(view);
        onClickEvents();
        generalAdapter = new GeneralAdapter();
        generalAdapter.setItems(myContents);
        generalAdapter.setViewHolderPlug(plug);
        generalAdapter.setClickListeners(postClickListeners);
        myContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        myContentList.setHasFixedSize(true);
        namePlaceHolder = view.findViewById(R.id.namePlaceHolder);
        myContentList.setAdapter(generalAdapter);
        namePlaceHolderText = view.findViewById(R.id.namePlaceHolderText);
        userID = User.getUserID(getContext().getApplicationContext());
        populateUserInformation(userID);
        insertItemsToList();

        fetchPost();


        return view;
    }

    private void insertItemsToList() {

        generalAdapter.notifyDataSetChanged();
    }

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

    PostClickListeners postClickListeners = new PostClickListeners() {
        @Override
        public void onProfileClick(int position) {

        }

        @Override
        public void onClickComment(int position) {

            onClickIcons.onClickComment((Post) myContents.get(position));
        }

        @Override
        public void onOptionClick(int position) {

            Post currentPost = (Post) myContents.get(position);
            MyContentsOption contentsOption = new MyContentsOption();
            DialogBox dialogBox = new DialogBox();

            dialogOptionListener(dialogBox, contentsOption, currentPost, position);
            contentsOption.show(getChildFragmentManager(), null);


        }

        @Override
        public void onClickItem(int position) {

        }
    };

    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("post");

    private void fetchPost() {

        postReference.keepSynced(false);
        postReference.orderByChild("postUserID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot post : snapshot.getChildren()) {
                        Post currentPost = post.getValue(Post.class);
                        myContents.add(currentPost);
                        generalAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void dialogOptionListener(DialogBox dialogBox, MyContentsOption contentsOption, Post currentPost, int position) {
        dialogBox.setListeners(new DialogBox.dialogButtons() {
            @Override
            public void onClickPositiveButton() {

                String postId = currentPost.getPostID();

                postReference.child(postId).removeValue();
                myContents.remove(position);
                generalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClickNegativeButton() {
                dialogBox.dismiss();
            }
        });
        //on click copy not implemented ye
        contentsOption.setOnOptionClickListener(new MyContentsOption.OptionClickListener() {
            @Override
            public void onClickDelete() {
                String deletePostMessage = "Are you sure you want to delete this post";
                dialogBox.setMessage(deletePostMessage);

                dialogBox.show(getChildFragmentManager(), null);
                contentsOption.dismiss();
            }

            @Override
            public void onClickCopy() {
                switch (currentPost.getType()) {
                    case ItemTypes.TEXT_POST:

                        break;
                    case ItemTypes.IMAGE_POST:
                        break;
                }

            }
        });
    }

    private void viewById(View view) {
        new_content = view.findViewById(R.id.new_content);
        profileImage = view.findViewById(R.id.profileImage);
        displayName = view.findViewById(R.id.displayName);
        changeProfilePicture = view.findViewById(R.id.changeProfilePicture);
        userName = view.findViewById(R.id.userName);
        bio_text = view.findViewById(R.id.bio_text);
        myContentList = view.findViewById(R.id.myContentList);
        profileOption = view.findViewById(R.id.profileOption);
        loader = view.findViewById(R.id.loader);
        uploading = view.findViewById(R.id.uploading);
    }

    private void onClickEvents() {
        new_content.setOnClickListener(v -> {

            onClickEdit.onEditClick();

        });
        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ViewProfilePicture.class);
            intent.putExtra("profile url", thisUser.getProfileUrl());
            startActivity(intent);

        });
        profileOption.setOnClickListener(v -> {
            ProfileOption profileOption = new ProfileOption();
            profileOption.setOptionSelection(new ProfileOption.OptionSelection() {
                @Override
                public void editProfile() {
                    Intent intent = new Intent(getActivity(), EditProfile.class);
                    intent.putExtra("user id", userID);
                    startActivity(intent);

                }

                @Override
                public void changeProfilePicture() {
                    pickImage();
                }
            });
            profileOption.show(getParentFragmentManager(), null);
        });
    }

    private final int PICK_IMAGE = 101;

    private void pickImage() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Select pictures"), PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            Uri uri = data.getData();


            Glide.with(this).load(uri).into(profileImage);

            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1, 1)
                    .setOutputCompressQuality(85)
                    .start(getContext(), this);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(this).load(resultUri).into(profileImage);
                uploadProfilePicture(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }

    }

    StorageReference profileStorage = FirebaseStorage.getInstance().getReference("profileUrls");
    DatabaseReference users = FirebaseDatabase.getInstance().getReference("users");

    private void uploadProfilePicture(Uri resultUri) {

        String extension = ImageExtension.fileExtension(resultUri.getPath());
        profileStorage = profileStorage.child(System.currentTimeMillis() + extension);
        UploadTask uploadTask = profileStorage.putFile(resultUri);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                uploading.setVisibility(View.VISIBLE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //StorageReference oldLink = FirebaseStorage.getInstance().getReferenceFromUrl(thisUser.getProfileUrl());

                        String imagLink = uri.toString();
                        String path = "profileUrl";
                        users.child(userID).child(path)
                                .setValue(imagLink);
                        Glide.with(getContext().getApplicationContext()).load(imagLink).into(profileImage);
                        Toast.makeText(getContext(), "profile image changed", Toast.LENGTH_SHORT).show();
                        uploading.setVisibility(View.GONE);

                    }
                });
            }
        });

    }

    private User thisUser;

    private void populateUserInformation(String userID) {
        DatabaseReference myInfo = users.child(userID);
        //YoYo.with(Techniques.Wave).duration(100).repeat(5).playOn(displayName);

        //YoYo.with(Techniques.Wave).duration(100).repeat(5).playOn(userName);

        myInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    thisUser = user;
                    String bio = user.getBio();
                    String display_name = user.getDisplayName();
                    String user_name = "@" + user.getUserName();
                    String profileUrl = user.getProfileUrl();
                    if (bio.isEmpty()) {
                        bio = "No bio added from this user";
                    }
                    bio_text.setText(bio);
                    displayName.setText(display_name);
                    userName.setText(user_name);
                    displayName.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    userName.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    if (profileUrl.isEmpty()) {
                        profileUrl = "https://www.clipartkey.com/mpngs/m/29-297748_round-profile-image-placeholder.png";
                        user.setProfileUrl(profileUrl);
                    }
                    Glide.with(Objects.requireNonNull(getContext())).load(profileUrl).into(profileImage);
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}