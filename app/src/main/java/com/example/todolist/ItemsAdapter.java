package com.example.todolist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    public interface OnClickListener{
        void onItemClicked(int position);
    }
    public interface OnLongClickListener{
        void onItemLongClicked(int position);
    }
    private List<String> items;
    private OnLongClickListener longClickListener;
    private OnClickListener clickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener olcl, OnClickListener onClickListener) {
        this.items = items;
        this.longClickListener = olcl;
        this.clickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        //wrap it inside a viewholder and return it
        return new ViewHolder(todoView);
    }

    //responsible for binding data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Grab the item on the position

        String item= items.get(position);

        //Bind the item into the specified viewholder
        holder.bind(item);

    }

    //Tells the RV how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //a container to provide easy access to a view that represents each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //update the view inside of the view holder with this data
        public void bind(String item)
        {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener()
            {
               @Override
               public void onClick(View v)
                {
                    clickListener.onItemClicked(getAdapterPosition());
                }

            });

            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //notify the position of the item that was long clicked
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }

            });

        }
    }

}

