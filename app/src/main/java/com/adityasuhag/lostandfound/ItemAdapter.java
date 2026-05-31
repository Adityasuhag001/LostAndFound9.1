package com.adityasuhag.lostandfound;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    // Lets the Activity react when a row is tapped
    public interface OnItemClickListener {
        void onItemClick(LostFoundItem item);
    }

    private List<LostFoundItem> items;
    private final OnItemClickListener clickListener;

    public ItemAdapter(List<LostFoundItem> items, OnItemClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;
    }

    // Called by the Activity to swap in a new list (e.g. after filtering)
    public void updateItems(List<LostFoundItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    // Inflates the row layout (called only when new rows scroll into view)
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new ItemViewHolder(row);
    }

    // Puts one item's data into the row's TextViews
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final LostFoundItem item = items.get(position);
        holder.textPostType.setText(item.getPostType());
        holder.textName.setText(item.getName());
        holder.textDescription.setText(item.getDescription());
        holder.textCategory.setText(item.getCategory());

        // Turn the millis timestamp into "2 days ago" / "5 mins ago" style
        CharSequence relative = DateUtils.getRelativeTimeSpanString(
                item.getTimestamp(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS);
        holder.textTimeAgo.setText(relative);

        // Tap on the row → fire the callback with this item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Holds references to the views in one row so we don't call findViewById on every scroll
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textPostType, textName, textDescription, textCategory, textTimeAgo;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textPostType = itemView.findViewById(R.id.textPostType);
            textName = itemView.findViewById(R.id.textName);
            textDescription = itemView.findViewById(R.id.textDescription);
            textCategory = itemView.findViewById(R.id.textCategory);
            textTimeAgo = itemView.findViewById(R.id.textTimeAgo);
        }
    }
}
