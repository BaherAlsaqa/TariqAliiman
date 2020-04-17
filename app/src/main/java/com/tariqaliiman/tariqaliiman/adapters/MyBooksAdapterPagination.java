package com.tariqaliiman.tariqaliiman.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Models.books.Book;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.listeners.OnItemClickListener3;

import java.util.ArrayList;

public class MyBooksAdapterPagination extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<Book> CategoryList;
    private Context context;

    private boolean isLoadingAdded = false;
    private OnItemClickListener3 listener1;
    private OnItemClickListener3 listener4;

    public MyBooksAdapterPagination(Context context) {
        this.context = context;
        CategoryList = new ArrayList<>();
    }

    public ArrayList<Book> getCategoryList() {
        return CategoryList;
    }

    public void setCategoryList(ArrayList<Book> CategoryList) {
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
        View v1 = inflater.inflate(R.layout.books_item_style, parent, false);
        viewHolder = new Holder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Book Book = CategoryList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final Holder holder1 = (Holder) holder;

                holder1.title.setText(Book.getNameAr());
                holder1.auther.setText(Book.getAuthorAr());

                Picasso.get().load(Constants.imageURL+Book.getImage()).into(holder1.bookImage);

                View.OnClickListener listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener1.onItemClick(Book);
                    }
                };
                View.OnClickListener listener2 = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener4.onItemClick(Book);
                    }
                };

                holder1.cardView.setOnClickListener(listener);
                holder1.bookinfo.setOnClickListener(listener2);

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

    public void add(Book r) {
        CategoryList.add(r);
        notifyItemInserted(CategoryList.size() - 1);
    }

    public void addAll(ArrayList<Book> CategoryList) {
        for (Book result : CategoryList) {
            add(result);
        }
    }

    public void remove(Book r) {
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
        add(new Book());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = CategoryList.size() - 1;
        Book result = getItem(position);

        if (result != null) {
            CategoryList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Book getItem(int position) {
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

        TextView title, auther;
        CardView cardView;
        ImageView bookImage, bookinfo;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.booktitle);
            auther = itemView.findViewById(R.id.auther);
            cardView = itemView.findViewById(R.id.cardview);
            bookImage = itemView.findViewById(R.id.bookimage);
            bookinfo = itemView.findViewById(R.id.bookinfo);
        }
    }

    public void setOnClickListener(OnItemClickListener3 listener) {
        this.listener1 = listener;
    }
    public void setOnClickListenerBookInfo(OnItemClickListener3 listener) {
        this.listener4 = listener;
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public void setFilter(ArrayList<Book> orders) {
        CategoryList = new ArrayList<>();
        CategoryList.addAll(orders);
        notifyDataSetChanged();
    }


}
