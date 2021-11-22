package com.wispr.wispr.mainPages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wispr.wispr.contentCreator.FileType;
import com.wispr.wispr.R;
import com.wispr.wispr.util.Gallery;
import com.wispr.wispr.util.GalleryType;
import com.wispr.wispr.util.MakePostAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GalleryFragment extends Fragment {

    public GalleryFragment() {
        // Required empty public constructor
    }

    ImageView back;
    RecyclerView gallery;
    Button doneButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public interface onClickDone {
        void onClickDone(List<GalleryItem> items);
    }

    private onClickDone onClickDone;

    public void setOnClickGalleryImage(GalleryFragment.onClickDone onClickGalleryImage) {
        this.onClickDone = onClickGalleryImage;
    }

    List<GalleryItem> galleryItems = new ArrayList<>();
    List<GalleryItem> images = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.gallery, container, false);

        back = view.findViewById(R.id.back);
        gallery = view.findViewById(R.id.gallery);
        doneButton = view.findViewById(R.id.doneButton);
        back.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).onBackPressed();
        });


        Gallery deviceMedia = new Gallery(getActivity());
        deviceMedia.setGalleryType(GalleryType.IMAGE_ONLY);

        images = deviceMedia.getImages();
        MakePostAdapter makePostAdapter = new MakePostAdapter(getContext(), images);

        makePostAdapter.setOnItemClickListener(new MakePostAdapter.OnclickListener() {
            @Override
            public void onClickImage(int position, ImageView MakePostGalleryMark) {

                //onClickGalleryImage.onClickGalleryImage(position, MakePostGalleryMark);
                GalleryItem galleryItem = images.get(position);
                String fileUrl = galleryItem.fileURL;

                if (galleryItem.getFileType() != FileType.FILE_TYPE_IMAGE) {
                    galleryItems.clear();
                    galleryItems.add(galleryItem);
                    setImages(galleryItems);
                    onClickDone.onClickDone(getImages());
                } else {

                    if (markedImages.contains(fileUrl)) {
                        galleryItems.remove(position);
                        markedImages.remove(position);
                        MakePostGalleryMark.setVisibility(View.GONE);
                    } else {

                        galleryItems.add(images.get(position));
                        markedImages.add(galleryItem.getFileURL());
                        MakePostGalleryMark.setVisibility(View.VISIBLE);
                    }

                }

            }
        });
        makePostAdapter.setShowMarkings(true);
        makePostAdapter.setOnMarkClickListener(new MakePostAdapter.onMarkClickListener() {
            @Override
            public void onMarkClick(int position, ImageView isChecked) {


            }
        });

        gallery.setLayoutManager(new GridLayoutManager(getContext(), 3));
        gallery.setHasFixedSize(true);
        gallery.setAdapter(makePostAdapter);
        doneButton.setOnClickListener(v -> {
            setImages(galleryItems);
            onClickDone.onClickDone(getImages());
        });


        return view;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setImages(List<GalleryItem> images) {
        this.images = images;
    }

    List<String> markedImages = new ArrayList<>();

    public List<GalleryItem> getImages() {
        return galleryItems;
    }
}