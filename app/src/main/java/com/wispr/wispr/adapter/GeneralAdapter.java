package com.wispr.wispr.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GeneralAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private List<SuperEntity> items=new ArrayList<>();

    public void setItems(List<SuperEntity> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    public interface viewHolderPlug{
        MainViewHolder setPlug(ViewGroup group,int viewType);
    }
    private SuperClickListeners superClickListeners;

    public void setClickListeners(SuperClickListeners superClickListeners) {
        this.superClickListeners = superClickListeners;
    }

    private Context CONTEXT;
    private viewHolderPlug viewHolderPlug;

    public void setViewHolderPlug(GeneralAdapter.viewHolderPlug viewHolderPlug) {
        this.viewHolderPlug = viewHolderPlug;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        CONTEXT=parent.getContext();
        return viewHolderPlug.setPlug(parent,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        holder.bindPostType(items.get(position),CONTEXT,superClickListeners);

    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
