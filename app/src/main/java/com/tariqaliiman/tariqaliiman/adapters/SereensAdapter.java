package com.tariqaliiman.tariqaliiman.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.classes.Sereen;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener1;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener2;

import java.util.List;

/**
 * Created by baher on 16/11/2017.
 */

public class SereensAdapter extends RecyclerView.Adapter<SereensAdapter.ViewHolder> {
    List<Sereen> sereenList;
    Context context;
    private OnItemClickListener1 listener1;
    private OnItemClickListener2 listener3;

    public SereensAdapter(List<Sereen> sereenList, Context context) {
        this.sereenList = sereenList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sereen_item_style, viewGroup, false);
        ViewHolder holderR = new ViewHolder(view);
        return holderR;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final SereensAdapter.ViewHolder holder, int position) {
        final Sereen sereen = sereenList.get(position);

        holder.title.setText(sereen.getTitle());
        holder.content.setText(sereen.getContent());

        holder.linearContent.setVisibility(View.GONE);

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.linearContent.getVisibility() == View.GONE){
                    holder.linearContent.setVisibility(View.VISIBLE);
                }else{
                    holder.linearContent.setVisibility(View.GONE);
                }
            }
        });*/

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener1.onItemClick(sereen);
            }
        };
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener3.onItemClick(sereen);
            }
        };

        holder.share.setOnClickListener(listener);
        holder.cardView.setOnClickListener(listener2);
    }

    @Override
    public int getItemCount() {
        return sereenList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        LinearLayout linearContent;
        CardView cardView;
        ImageView share;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            linearContent = itemView.findViewById(R.id.linearcontent);
            cardView = itemView.findViewById(R.id.cardview);
            share = itemView.findViewById(R.id.share);
        }
    }

    public void setOnClickListener(OnItemClickListener1 listener) {
        this.listener1 = listener;
    }
    public void setOnClickListener(OnItemClickListener2 listener3) {
        this.listener3 = listener3;
    }

}
