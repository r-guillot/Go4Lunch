package com.guillot.go4lunch.common;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class ItemClickListener {

    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
            }
        }
    };

    private ItemClickListener(RecyclerView recyclerView, int itemID) {
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(itemID, this);
        RecyclerView.OnChildAttachStateChangeListener attachListener = new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NotNull View view) {
                if (mOnItemClickListener != null) {
                    view.setOnClickListener(mOnClickListener);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(@NotNull View view) {

            }
        };
        mRecyclerView.addOnChildAttachStateChangeListener(attachListener);
    }

    public static ItemClickListener addTo(RecyclerView view, int itemID) {
        ItemClickListener support = (ItemClickListener) view.getTag(itemID);
        if (support == null) {
            support = new ItemClickListener(view, itemID);
        }
        return support;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {

        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }
}
