package com.guillot.go4lunch.mates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.guillot.go4lunch.databinding.ItemMatesBinding;
import com.guillot.go4lunch.model.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MatesListAdapter extends RecyclerView.Adapter<MatesListViewHolder> {
    private List<User> userList;

    public MatesListAdapter(List<User> users) {
        this.userList = users;
    }

    @NotNull
    @Override
    public MatesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        Context context = parent.getContext();
        return new MatesListViewHolder(ItemMatesBinding.inflate(layoutInflater), context);
    }

    @Override
    public void onBindViewHolder(MatesListViewHolder holder, int position) {
        holder.updateMatesInfo(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    public void update(List<User> users){
        this.userList = users;
        notifyDataSetChanged();
    }
}
