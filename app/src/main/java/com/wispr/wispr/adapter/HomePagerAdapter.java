package com.wispr.wispr.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentStateAdapter {


    List<Fragment> pages=new ArrayList<>();

    public void setPages(List<Fragment> pages) {
        this.pages = pages;
    }

    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        // Our object is just an integer :-P
        Fragment output=pages.get(position);
        return output;
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }
}
