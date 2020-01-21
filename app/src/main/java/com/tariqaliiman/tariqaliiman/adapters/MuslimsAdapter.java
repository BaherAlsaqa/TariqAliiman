package com.tariqaliiman.tariqaliiman.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.classes.Muslim;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener;

import java.util.List;

/**
 * Created by baher on 16/11/2017.
 */

public class MuslimsAdapter extends RecyclerView.Adapter<MuslimsAdapter.ViewHolder> {
    List<Muslim> muslimList;
    Context context;
    private OnItemClickListener listener1;

    public MuslimsAdapter(List<Muslim> muslimList, Context context) {
        this.muslimList = muslimList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.muslim_item_style, viewGroup, false);
        ViewHolder holderR = new ViewHolder(view);
        return holderR;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(MuslimsAdapter.ViewHolder holder, int position) {
        final Muslim muslim = muslimList.get(position);

        holder.title.setText(muslim.getName());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(muslim);
            }
        };

        holder.cardView.setOnClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return muslimList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener1 = listener;
    }

}
