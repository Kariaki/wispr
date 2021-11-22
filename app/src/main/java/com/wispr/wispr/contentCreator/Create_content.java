package com.wispr.wispr.contentCreator;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wispr.wispr.adapter.GeneralAdapter;
import com.wispr.wispr.adapter.postViewHolders.CreatePostGalleryViewHolder;
import com.wispr.wispr.adapter.postViewHolders.CreatePostImageListener;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.ItemTypes;
import com.wispr.wispr.entities.Post;
import com.wispr.wispr.entities.User;
import com.wispr.wispr.mainPages.Camera;
import com.wispr.wispr.mainPages.GalleryFragment;
import com.wispr.wispr.mainPages.GalleryItem;
import com.wispr.wispr.R;
import com.wispr.wispr.util.Gallery;
import com.wispr.wispr.util.GalleryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Create_content extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public interface onUploadClick {

        void onUploadClick(Post post, List<String> links);


    }

    onUploadClick uploadClick;
    private Post outputPost;


    public void setOutputPost(Post outputPost) {
        this.outputPost = outputPost;
    }

    public Post getOutputPost() {
        return outputPost;
    }

    public void setUploadClick(onUploadClick uploadClick) {
        this.uploadClick = uploadClick;
    }

    private ImageView back_button;
    private Button uploadPost;
    private ImageView openCamera, insertMedia;
    private int root = R.id.create_content_root;
    private AutoCompleteTextView post_caption;
    private RecyclerView added_images;
    GeneralAdapter generalAdapter;
    List<SuperEntity> items = new ArrayList<>();

    DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("post");
    List<String> urls = new ArrayList<>();

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {

        return urls;
    }

    private final String hashConstant = "#";

    private String[] suggestion = {"#UAT", "#UAT-COMP", "#NDU-COMP"};
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.create_content, container, false);
        back_button = view.findViewById(R.id.back_button);
        uploadPost = view.findViewById(R.id.uploadPost);
        post_caption = view.findViewById(R.id.post_caption);
        added_images = view.findViewById(R.id.added_images);
        insertMedia = view.findViewById(R.id.insertMedia);
        openCamera = view.findViewById(R.id.openCamera);



        generalAdapter = new GeneralAdapter();

        generalAdapter.setViewHolderPlug(viewHolderPlug);

        generalAdapter.setItems(items);
        added_images.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, suggestion);

        post_caption.setDropDownHeight(500);
        post_caption.setDropDownWidth(300);


        post_caption.setAdapter(adapter);


        added_images.setHasFixedSize(true);
        generalAdapter.setClickListeners(new CreatePostImageListener() {
            @Override
            public void removeImage(int position) {

                if (items.size() == 1) {

                    items.remove(position);
                    // generalAdapter.notifyDataSetChanged();
                    added_images.setVisibility(View.GONE);
                } else {

                    items.remove(position);

                }
                generalAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClickItem(int position) {

            }
        });
        added_images.setAdapter(generalAdapter);

        List<GalleryItem> images = new ArrayList<>();
        Gallery deviceMedia = new Gallery(getActivity());
        deviceMedia.setGalleryType(GalleryType.ALL);

        images = deviceMedia.getImages();

        //   generalAdapter.notifyDataSetChanged();
        if (generalAdapter.getItemCount() > 0) {
            added_images.setVisibility(View.VISIBLE);
        }


        back_button.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        post_caption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               String convertToString = s.toString();
                if (!convertToString.isEmpty()) {
                    char[] sChar = convertToString.toCharArray();
                    char lastChar = sChar[sChar.length - 1];
                    if (convertToString.length() > 1 && String.valueOf(lastChar).equals(hashConstant)) {
                        //Toast.makeText(getContext(), "hash has been pressed", Toast.LENGTH_SHORT).show();
                        post_caption.showDropDown();

                    }
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        uploadPost.setOnClickListener(v -> {

            uploadProcess();

        });


        openCamera.setOnClickListener(v -> {

            Camera camera = new Camera();
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(root, camera).addToBackStack(null).commit();

        });

        insertMedia.setOnClickListener(v -> {
            GalleryFragment galleryFragment = new GalleryFragment();
            galleryFragment.setOnClickGalleryImage(new GalleryFragment.onClickDone() {
                @Override
                public void onClickDone(List<GalleryItem> i) {
                    Objects.requireNonNull(getActivity()).onBackPressed();

                    // items.clear();
                    items.addAll(galleryFragment.getImages());
                    if (items.size() > 0) {
                        added_images.setVisibility(View.VISIBLE);
                    } else {
                        added_images.setVisibility(View.GONE);
                    }
                    generalAdapter.notifyDataSetChanged();

                }
            });
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(root, galleryFragment).addToBackStack(null).commit();

        });

        return view;
    }

    GeneralAdapter.viewHolderPlug viewHolderPlug = (group, viewType) -> {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_image_post, group, false);
        return new CreatePostGalleryViewHolder(view);
    };

    private void uploadProcess(){
        Post post = new Post(System.currentTimeMillis(),
                post_caption.getText().toString(), User.getUserID(getContext()), ItemTypes.IMAGE_POST, null);


        for (SuperEntity item : items) {
            GalleryItem galleryItem = (GalleryItem) item;
            urls.add(galleryItem.getFileURL());
        }

        //upload links to cloud storage
        if (urls.size() > 0) {
            post.setType(ItemTypes.IMAGE_POST);

               /* if (urls.size() == 1) {
                    if (items.get(0).getType() == FileType.FILE_TYPE_IMAGE) {


                    } else {
                        post.setType(ItemTypes.VIDEO_POST);
                    }
                }

                */

        } else {
            post.setType(ItemTypes.TEXT_POST);
        }
        items.clear();
        post_caption.getText().clear();
        generalAdapter.notifyDataSetChanged();

        uploadClick.onUploadClick(post, urls);

    }
    @Override
    public void onDetach() {
        super.onDetach();

        items.clear();
        post_caption.getText().clear();
        generalAdapter.notifyDataSetChanged();

    }

}