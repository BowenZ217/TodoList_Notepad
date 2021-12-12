package com.ptrz.todolist_notepad;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// responsible for displaying data from the model into a row of recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface onLongClickListener {
        void onItemLongClicked(int position);
    }
    public interface onClickListener {
        void onItemClicked(int position);
    }

    List<String> items;
    onClickListener clickListener;
    onLongClickListener longClickListener;
    public ItemsAdapter(List<String> items, onLongClickListener longClickListener, onClickListener clickListener) {
        this.items = items;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // use layout inflater to inflate a view
        View todoView = LayoutInflater.from(viewGroup.getContext())
                .inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        // wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }

    // responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // get the item at that position
        String item = items.get(i);
        // bind the item to the specified view holder
        viewHolder.bind(item);
    }

    // tell the RV how many items in the list
    @Override
    public int getItemCount() {
        // the size of list to view
        return items.size();
    }

    // container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // update the view inside the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);

            tvItem.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

            tvItem.setOnLongClickListener(v -> {
                // Notify the listener which position was long pressed
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            });
        }
    }
}
