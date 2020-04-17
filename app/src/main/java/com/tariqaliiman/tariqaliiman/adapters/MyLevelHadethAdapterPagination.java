package com.tariqaliiman.tariqaliiman.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tariqaliiman.tariqaliiman.Models.LevelHadeth.LevelHadeth;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener5;

import java.util.ArrayList;

public class MyLevelHadethAdapterPagination extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<LevelHadeth> CategoryList;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener5 listener1;

    public MyLevelHadethAdapterPagination(Context context) {
        this.context = context;
        CategoryList = new ArrayList<>();
    }

    public ArrayList<LevelHadeth> getCategoryList() {
        return CategoryList;
    }

    public void setCategoryList(ArrayList<LevelHadeth> CategoryList) {
        this.CategoryList = CategoryList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.level_hadeth_item_style, parent, false);
        viewHolder = new Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final LevelHadeth LevelHadeth = CategoryList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final Holder holder1 = (Holder) holder;

                holder1.title.setText(LevelHadeth.getTitleAr());
                holder1.pageNumber.setText(LevelHadeth.getPageNoAr()+"");

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener1.onItemClick(LevelHadeth);
                    }
                };

                holder1.cardView.setOnClickListener(listener);

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return CategoryList == null ? 0 : CategoryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == CategoryList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(LevelHadeth r) {
        CategoryList.add(r);
        notifyItemInserted(CategoryList.size() - 1);
    }

    public void addAll(ArrayList<LevelHadeth> CategoryList) {
        for (LevelHadeth result : CategoryList) {
            add(result);
        }
    }

    public void remove(LevelHadeth r) {
        int position = CategoryList.indexOf(r);
        if (position > -1) {
            CategoryList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new LevelHadeth());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = CategoryList.size() - 1;
        LevelHadeth result = getItem(position);

        if (result != null) {
            CategoryList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public LevelHadeth getItem(int position) {
        return CategoryList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class Holder extends RecyclerView.ViewHolder {

        TextView title, pageNumber;
        CardView cardView;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.hadethtitle);
            pageNumber = itemView.findViewById(R.id.pagenumber);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }

    public void setOnClickListener(OnItemClickListener5 listener) {
        this.listener1 = listener;
    }

    protected static class LoadingVH extends RecyclerView.ViewHolder {

        LoadingVH(View itemView) {
            super(itemView);
        }
    }

}
