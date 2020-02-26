package com.tariqaliiman.tariqaliiman.adapters;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Models.Bookmark;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.activities.QuranPageReadActivity;
import com.tariqaliiman.tariqaliiman.Utilities.AppConstants;
import com.tariqaliiman.tariqaliiman.Utilities.Settingsss;

import java.util.List;
import java.util.Locale;

/**
 * Adapter class for the bookmarks list view
 */
public class BookmarksShowAdapter extends ArrayAdapter<Bookmark> implements View.OnClickListener {

    private Context context;
    private View rootview;
    Typeface type ;
    LayoutInflater inflate;
    /**
     * Constructor fo bookmark show
     *
     * @param context Application context
     * @param bookmarks   List of bookmarks
     */
    public BookmarksShowAdapter(Context context, List<Bookmark> bookmarks) {
        super(context, R.layout.row_bookmark, bookmarks);
        this.context = context;
        type = Typeface.createFromAsset(context.getAssets(), "fonts/elmasri.ttf");
         inflate = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Function to get the view of every row in the list view
     *
     * @param position    Row position number
     * @param convertView view
     * @param parent      Parent view
     * @return return your custom row view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bookmark bookmark = getItem(position);
        ViewHolder viewHolder;
        ViewHolderSeparator viewHolderSeparator;

        //switch between two views bookmark and separators
        if (bookmark.bookmarkID != -1) {
            // sora item

            convertView = inflate.inflate(R.layout.row_bookmark, parent, false);
            viewHolder = new ViewHolder(convertView);
            viewHolder.soraLayout.setTag(R.id.item_holder_position, position);
            viewHolder.soraLayout.setOnClickListener(this);
            String currentLanguage = Resources.getSystem().getConfiguration().locale.getLanguage();
            Log.d(Constants.log, "current language = " + currentLanguage);
            viewHolder.suraName.setText(currentLanguage.equals("ar") ?
                    context.getResources().getString(R.string.sora) + " " + bookmark.pageInfo.soraName :
                    context.getResources().getString(R.string.sora) + " "
                            + bookmark.pageInfo.soraNameEnglish.replace("$$$", "'")
            );
            viewHolder.pageNumber.setText(Settingsss.ChangeNumbers(context, bookmark.page + ""));
            viewHolder.bookmarkInfo.setText(Settingsss.ChangeNumbers(context, context.getResources().getString(R.string.page) + " " +
                    bookmark.page + ", " +
                    context.getResources().getString(R.string.juza) + " " +
                    bookmark.pageInfo.jozaNumber));


            if(isTablet(context)){
                viewHolder.suraName.setTextSize(31);
                viewHolder.bookmarkInfo.setTextSize(25);
                viewHolder.pageNumber.setTextSize(25);
            }


        } else {
            // header item.

            convertView =  inflate.inflate(R.layout.bookmark_splite_row_item, parent, false);
            convertView.setClickable(false);
            viewHolderSeparator = new ViewHolderSeparator(convertView);
            //data and time where i put bookmark title
            viewHolderSeparator.bookmarkType.setText(bookmark.dateAndTime);
            if(isTablet(context)){
                viewHolderSeparator.bookmarkType.setTextSize(30);
            }
        }

        return convertView;
    }

    @Override
    public void onClick(View view) {
        int itemPosition = (int) view.getTag(R.id.item_holder_position);
        Bookmark bookmark = getItem(itemPosition);
        Intent QuranPage = new Intent(getContext(), QuranPageReadActivity.class);
        QuranPage.putExtra(AppConstants.General.PAGE_NUMBER, (604 - bookmark.page));
        QuranPage.putExtra(AppConstants.General.BOOK_MARK, bookmark.bookmarkID);
        context.startActivity(QuranPage);
    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    /**
     * View holder for bookmark adapter
     */
    private class ViewHolder {
         TextView suraName, bookmarkInfo, pageNumber;
         RelativeLayout soraLayout;
         ViewHolder(View layout) {

            soraLayout = (RelativeLayout) layout.findViewById(R.id.rl_sora_item);
            suraName = (TextView) layout.findViewById(R.id.textView5);
            bookmarkInfo = (TextView) layout.findViewById(R.id.textView6);
            pageNumber = (TextView) layout.findViewById(R.id.textView7);
            suraName.setTypeface(type);
            bookmarkInfo.setTypeface(type);
            pageNumber.setTypeface(type);
        }
    }

    /**
     * View holder class for separator
     */
    private class ViewHolderSeparator {
        public TextView bookmarkType;

        public ViewHolderSeparator(View view) {

            bookmarkType = (TextView) view.findViewById(R.id.tv_separator);
            bookmarkType.setTypeface(type);
        }
    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
