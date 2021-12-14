package com.ptrz.todolist_notepad.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ptrz.todolist_notepad.Model.Item;
import com.ptrz.todolist_notepad.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    public interface onLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface onClickListener {
        void onItemClicked(int position);
    }

    List<Item> list_item;
    onClickListener clickListener;
    onLongClickListener longClickListener;

    public ItemAdapter(List<Item> list_item, onLongClickListener longClickListener, onClickListener clickListener) {
        this.list_item = list_item;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // use layout inflater to inflate a view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        // wrap it inside a View Holder and return it
        return new ItemHolder(view);
    }

    // responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        // get the item at that position
        Item item = list_item.get(position);
        // bind the item to the specified view holder
        holder.bind(item);
    }

    // tell the RV how many items in the list
    @Override
    public int getItemCount() {
        // the size of list to view
        return list_item.size();
    }

    // container to provide easy access to views that represent each row of the list
    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;

        public ItemHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_textView);
            subtitle = itemView.findViewById(R.id.subtitle_textView);
        }

        // update the view inside the view holder with this data
        public void bind(Item item) {
            title.setText(item.getIsImportant() + item.getEvent());
            subtitle.setText("Due: " + item.getDate());

            title.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

            title.setOnLongClickListener(v -> {
                // Notify the listener which position was long pressed
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            });

            subtitle.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

            subtitle.setOnLongClickListener(v -> {
                // Notify the listener which position was long pressed
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            });
        }
    }
}