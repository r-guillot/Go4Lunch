package com.guillot.go4lunch.mates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.guillot.go4lunch.databinding.ItemMatesBinding;
import com.guillot.go4lunch.model.User;

import java.util.List;

public class MatesListAdapter extends RecyclerView.Adapter<MatesListViewHolder> {
    private List<User> userList;
    private Context context;

    public MatesListAdapter(List<User> users) {
        this.userList = users;
    }

    @Override
    public MatesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        return new MatesListViewHolder(ItemMatesBinding.inflate(layoutInflater), context);
    }

    @Override
    public void onBindViewHolder(MatesListViewHolder holder, int position) {
        final User user = userList.get(position);
        holder.updateMatesInfo(user);
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }

    void update(List<User> users){
        this.userList = users;

    }
}