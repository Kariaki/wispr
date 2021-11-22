package com.wispr.wispr.LoginAndRegistration;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wispr.wispr.adapter.GeneralAdapter;
import com.wispr.wispr.adapter.MainViewHolder;
import com.wispr.wispr.adapter.SuperClickListeners;
import com.wispr.wispr.adapter.SuperEntity;
import com.wispr.wispr.entities.Categories;
import com.wispr.wispr.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class CompleteRegistration extends Fragment {

    OnNextClick onNextClick;

    public void setOnNextClick(OnNextClick onNextClick) {
        this.onNextClick = onNextClick;
    }

    OnCancelClick onCancelClick;

    public void setOnCancelClick(OnCancelClick onCancelClick) {
        this.onCancelClick = onCancelClick;
    }

    private String userID;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    private EditText school, faculty, department;
    private Button nextButton;
    ImageView backButton;
    private List<SuperEntity> autoCompleteNameList = new ArrayList<>();
    private RecyclerView automCompleteSchool;
    private GeneralAdapter generalAdapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.complete_registration, container, false);
        school = view.findViewById(R.id.school);
        backButton = view.findViewById(R.id.backButton);
        department = view.findViewById(R.id.department);
        faculty = view.findViewById(R.id.faculty);
        generalAdapter = new GeneralAdapter();
        generalAdapter.setItems(autoCompleteNameList);
        generalAdapter.setViewHolderPlug(viewHolderPlug);
        automCompleteSchool = view.findViewById(R.id.automCompleteSchool);
        generalAdapter.setClickListeners(superClickListeners);
        automCompleteSchool.setLayoutManager(new LinearLayoutManager(getActivity()));
        automCompleteSchool.setHasFixedSize(true);
        automCompleteSchool.setAdapter(generalAdapter);


        nextButton = view.findViewById(R.id.nextButton);
        automCompleteSchool = view.findViewById(R.id.automCompleteSchool);
        nextButton.setOnClickListener(v -> {
            updateFinalUserInformation();
        });
        backButton.setOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).onBackPressed();
        });

        registerTextWatcher(school);


        return view;
    }

    private DatabaseReference categories = FirebaseDatabase.getInstance().getReference("categories");

    private List<String> addedAutoComplete = new ArrayList<>();

    private void registerTextWatcher(EditText textInput) {
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                if (s.toString().isEmpty()) {
                    automCompleteSchool.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String resultString = s.toString();
                if (!resultString.isEmpty()) {

                    autoCompleteNameList.clear();
                    addedAutoComplete.clear();
                    categories.orderByChild("name")
                            .startAt(resultString)
                            .endAt(s + "\uf8ff")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        for (DataSnapshot resultSnapShot : snapshot.getChildren()) {
                                            if (!addedAutoComplete.contains(resultSnapShot.getKey())) {
                                                addedAutoComplete.add(resultSnapShot.getKey());

                                                automCompleteSchool.setVisibility(View.VISIBLE);
                                                Categories categories = resultSnapShot.getValue(Categories.class);
                                                autoCompleteNameList.add(new AutoCompleteEntity(categories.getName()));
                                                generalAdapter.notifyDataSetChanged();
                                            }

                                        }
                                    } else {
                                        automCompleteSchool.setVisibility(View.INVISIBLE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                } else {

                    automCompleteSchool.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    DatabaseReference allUser = FirebaseDatabase.getInstance().getReference("users");

    GeneralAdapter.viewHolderPlug viewHolderPlug = new GeneralAdapter.viewHolderPlug() {
        @Override
        public MainViewHolder setPlug(ViewGroup group, int viewType) {
            View output = LayoutInflater.from(getContext()).inflate(R.layout.auto_complte_text, group, false);
            return new AutoCompleteViewHolder((output));
        }
    };
    SuperClickListeners superClickListeners = new SuperClickListeners() {
        @Override
        public void onClickItem(int position) {
            AutoCompleteEntity entity = (AutoCompleteEntity) autoCompleteNameList.get(position);
            school.setText(entity.getCompleteText());

        }
    };

    private void updateFinalUserInformation() {
        Map<String, Object> update = new HashMap<>();
        update.put("school", school.getText().toString());
        update.put("faculty", faculty.getText().toString());
        update.put("department", department.getText().toString());
        allUser.child(userID).child("school").setValue(school.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        allUser.child(userID).child("department").setValue(department.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        allUser.child(userID).child("faculty").setValue(faculty.getText().toString())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences("user", Context.MODE_PRIVATE).edit();
                                                        editor.putString("school", school.getText().toString());

                                                        editor.apply();
                                                        Objects.requireNonNull(getActivity()).onBackPressed();
                                                    }
                                                });
                                    }
                                });

                    }
                });

    }

}