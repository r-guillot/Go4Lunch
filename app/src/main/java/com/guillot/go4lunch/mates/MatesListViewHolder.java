package com.guillot.go4lunch.mates;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guillot.go4lunch.databinding.ItemMatesBinding;
import com.guillot.go4lunch.model.User;

public class MatesListViewHolder extends RecyclerView.ViewHolder {

    private ItemMatesBinding binding;
    private Context context;

    MatesListViewHolder(@NonNull ItemMatesBinding binding, Context context) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = context;
    }

    public void updateMatesInfo(User user){
    }
}
