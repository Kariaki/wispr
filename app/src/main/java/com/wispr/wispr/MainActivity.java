package com.wispr.wispr;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wispr.wispr.contentCreator.Create_content;
import com.wispr.wispr.entities.ItemTypes;
import com.wispr.wispr.entities.Post;
import com.wispr.wispr.entities.PostChannel;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.LoginAndRegistration.CompleteRegistration;
import com.wispr.wispr.mainPages.CommentFragment;
import com.wispr.wispr.mainPages.ContentFeed;
import com.wispr.wispr.adapter.HomePagerAdapter;
import com.wispr.wispr.mainPages.MessagingPage;
import com.wispr.wispr.mainPages.profiles.Profile_fragment;
import com.wispr.wispr.mainPages.profiles.SocialProfile;
import com.wispr.wispr.util.DatabaseHelper;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ViewPager2 pager;
    BottomNavigationView navigation;
    HomePagerAdapter pagerAdapter;
    List<Fragment> fragmentList = new ArrayList<>();
    String[] allRequiredPermisions = {Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA};

    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("post");

    StorageReference storage = FirebaseStorage.getInstance().getReference("postFiles");
    DatabaseReference allUsers = FirebaseDatabase.getInstance().getReference("users");
    DatabaseReference chatChannels = FirebaseDatabase.getInstance().getReference("ChatChannels");

    DatabaseReference chatGroups = FirebaseDatabase.getInstance().getReference("categories");

    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewByID();
        pagerAdapter = new HomePagerAdapter(this);

        pagerAdapter.setPages(getFragmentList());
        pager.setAdapter(pagerAdapter);

        checkPermission();
        userID=User.getUserID(this);

        checkFirstTime();
        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 1:
                        navigation.setSelectedItemId(R.id.home_page);
                        break;
                    case 0:
                        navigation.setSelectedItemId(R.id.groups);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.blank);


                }
            }
        });


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_page:
                        pager.setCurrentItem(1);
                        break;
                    case R.id.blank:
                        pager.setCurrentItem(2);
                        break;
                    case R.id.groups:
                        pager.setCurrentItem(0);
                       /* Intent intent = new Intent(MainActivity.this, ChatPageJava.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);

                        */
                        break;
                }
                return true;
            }
        });

        pager.setCurrentItem(1);

        new Thread(new Runnable() {
            @Override
            public void run() {
                connect();
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // pager.setCurrentItem(1);
    }

    private void checkPermission() {
        if (!allPermissionGranted()) {
            requestNeededPermision();
        }
    }

    private void viewByID() {
        pager = findViewById(R.id.pager);
        navigation = findViewById(R.id.navigation);
        pager.setUserInputEnabled(false);
    }


    DatabaseReference allUser = FirebaseDatabase.getInstance().getReference("users");

    private List<Fragment> getFragmentList() {

        ContentFeed contentFeed = new ContentFeed();

        contentFeed.setOnClickIcons(new ContentFeed.onClickIcons() {
            @Override
            public void onClickProfile(Post post) {
                String ownerId=post.getPostUserID();
                SocialProfile socialProfile=new SocialProfile();
                socialProfile.setOwner(ownerId);
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

                transaction.addToBackStack(null).add(R.id.mainRoot,socialProfile).commit();
            }

            @Override
            public void onClickComment(Post post) {
                CommentFragment commentFragment = new CommentFragment();
                commentFragment.setPost(post);
                getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainRoot, commentFragment).commit();

            }
        });

        fragmentList.add(new MessagingPage());
        fragmentList.add(contentFeed);

        Profile_fragment profile_fragment = new Profile_fragment();
        profile_fragment.setOnClickIcons(new ContentFeed.onClickIcons() {
            @Override
            public void onClickProfile(Post post) {
                String ownerId=post.getPostUserID();
                SocialProfile socialProfile=new SocialProfile();
                socialProfile.setOwner(ownerId);
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

                transaction.addToBackStack(null).add(R.id.mainRoot,socialProfile).commit();
            }

            @Override
            public void onClickComment(Post post) {
                CommentFragment commentFragment = new CommentFragment();
                commentFragment.setPost(post);
                getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.mainRoot, commentFragment).commit();

            }
        });
        Create_content create_content = new Create_content();

        create_content.setUploadClick(new Create_content.onUploadClick() {
            @Override
            public void onUploadClick(Post post, List<String> links) {
                onBackPressed();
                List<String> urls = links;

                switch (post.getType()) {
                    case ItemTypes.TEXT_POST:
                        DatabaseReference new_post = postReference.push();
                        post.setPostID(new_post.getKey());
                        post.setPostUserID(User.getUserID(MainActivity.this));

                        new_post.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "post uploaded", Toast.LENGTH_SHORT).show();
                                processTags(post.getPostCaption(), post.getPostID());
                            }
                        });

                        break;
                    case ItemTypes.IMAGE_POST:
                        for (String link : urls) {
                            File file = new File(link);
                            Uri uri = Uri.fromFile(file);
                            storage = storage.child(String.valueOf(System.currentTimeMillis()));
                            UploadTask uploadTask = storage.putFile(uri);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            uploadedImages.add(uri.toString());
                                            if (uploadedImages.size() == urls.size()) {

                                                DatabaseReference new_post = postReference.push();
                                                post.setPostID(new_post.getKey());
                                                post.setPostUserID(User.getUserID(MainActivity.this));

                                                StringBuilder output = new StringBuilder();
                                                for (String uploadedImageLink : uploadedImages) {

                                                    output.append(uploadedImageLink).append(",");
                                                }
                                                post.setPostUrl(output.toString());
                                                new_post.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(MainActivity.this, "post uploaded", Toast.LENGTH_SHORT).show();
                                                        processTags(post.getPostCaption(), post.getPostID());

                                                    }
                                                });

                                            }


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, "upload failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                        break;

                }

            }


        });
        profile_fragment.setOnClickEdit(new Profile_fragment.onClickEdit() {
            @Override
            public void onEditClick() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainRoot, create_content).addToBackStack(null).commit();
            }
        });
        fragmentList.add(profile_fragment);


        return fragmentList;
    }

    List<String> uploadedImages = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();



    }

    DatabaseReference tagReference;

    private void processTags(String caption, String postId) {
        tagReference = postReference.child("postChannel");
        String[] allTags = caption.split("#");
        if (allTags.length > 0) {
            for (String tag : allTags) {

                add_to_tags(tagReference, postId, tag);

            }
        }

    }

    private void add_to_tags(DatabaseReference tagFolder, String postId, String tag) {
        tagFolder.orderByChild("tag").equalTo(tag)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot currentTag : snapshot.getChildren()) {
                                PostChannel thisTag = currentTag.getValue(PostChannel.class);
                                if (thisTag != null) {
                                    tagFolder.child(thisTag.getTagId()).child("post")
                                            .child(postId).setValue(postId);
                                }
                            }
                        } else {
                            DatabaseReference newTag = tagFolder.push();
                            PostChannel createNewTag = new PostChannel(tag, tag, newTag.getKey());
                            newTag.setValue(createNewTag);
                            newTag.child("post").child(postId).setValue(postId);

                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    private boolean allPermissionGranted() {

        for (String permission : allRequiredPermisions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void requestNeededPermision() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, allRequiredPermisions, PackageManager.PERMISSION_GRANTED);

        }

    }


    private void checkFirstTime() {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String userID = sharedPreferences.getString("user id", "");
        String school = sharedPreferences.getString("school", "");
        if (school.isEmpty()) {
            CompleteRegistration completeRegistration = new CompleteRegistration();
            completeRegistration.setUserID(userID);
            getSupportFragmentManager().beginTransaction().add(R.id.mainRoot, completeRegistration).addToBackStack(null).commit();

        }

    }

    private void connect() {
       DatabaseReference myChats = allUsers.child(userID).child("chats");

        allUsers.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            User innerUser = snapshot.getValue(User.class);

                            myChats.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull @NotNull DataSnapshot snapshot) {
                                    if(!snapshot.exists()){

                                        DatabaseHelper.connect(innerUser,userID);
                                    }
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull @NotNull DatabaseError error) {

                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull @NotNull DatabaseError error) {

                    }
                });

    }
}
