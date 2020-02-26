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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tariqaliiman.tariqaliiman.Constants;
import com.tariqaliiman.tariqaliiman.Models.Sora;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.activities.QuranPageReadActivity;
import com.tariqaliiman.tariqaliiman.Utilities.AppConstants;
import com.tariqaliiman.tariqaliiman.Utilities.Settingsss;

import java.util.List;
import java.util.Locale;

/**
 * Adapter class for part show list view
 */
public class PartShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Sora> items ;
    private Typeface customFont;
int x=0;
    private  boolean isTabletDevice;
    /**
     * Constructor for part show adapter
     *
     * @param context Application context
     * @param items   list of sora
     */
    public PartShowAdapter(Context context, List<Sora> items) {
        this.context = context;
        this.items = items ;
        customFont = Typeface.createFromAsset(context.getAssets(), "fonts/elmasri.ttf");
        isTabletDevice = isTablet(context);
    }

    @Override
    public int getItemViewType(int position) {
        Sora sora = items.get(position);
        return sora.ayahCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType != -1){

            // sora name.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_surah , parent , false);
            return new ViewHolder(view);
        }else{
            //header mame
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sura_separator , parent , false);
            return new ViewHolderSplitter(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        final Sora sora = items.get(position);

        if(getItemViewType(position) != -1){
            String currentLanguage = Resources.getSystem().getConfiguration().locale.getLanguage();
            Log.d(Constants.log, "current language = " + currentLanguage);
            String soraNameText = currentLanguage.equals("ar") ? sora.name : sora.name_english.replace("$$$", "'");
            ((ViewHolder) holder).number.setText(Settingsss.ChangeNumbers(context , sora.getSoraTag()));
            ((ViewHolder) holder).soraName.setText(Settingsss.ChangeNumbers(context, context.getResources().getString(R.string.sora) + " " + soraNameText));
            ((ViewHolder) holder).ayaCount.setText(Settingsss.ChangeNumbers(context, (sora.places == 1 ? context.getResources().getString(R.string.type1) : context.getResources().getString(R.string.type2)) + " - " + (sora.startPageNumber == 1 ? 7 : sora.ayahCount) + " " + context.getResources().getString(R.string.ayat)));
            ((ViewHolder) holder).pageNumbers.setText(Settingsss.ChangeNumbers(context, sora.startPageNumber + ""));
            if(isTabletDevice){
                ((ViewHolder) holder).soraName.setTextSize(30);
                ((ViewHolder) holder).ayaCount.setTextSize(25);
                ((ViewHolder) holder).pageNumbers.setTextSize(25);

            }
        }else{

            ((ViewHolderSplitter) holder).jozaNumber.setText(Settingsss.ChangeNumbers(context, context.getString(R.string.juza) + " " + sora.jozaNumber));
            ((ViewHolderSplitter) holder).pageNumber.setText(Settingsss.ChangeNumbers(context , sora.startPageNumber+""));
            if(isTabletDevice){
                ((ViewHolderSplitter) holder).jozaNumber.setTextSize(30);
                ((ViewHolderSplitter) holder).pageNumber.setTextSize(20);
            }
        }

        //open quran pages
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 if(sora.ayahCount != -1){

                }else{

                }

                Intent QuranPage = new Intent(context, QuranPageReadActivity.class);
                QuranPage.putExtra(AppConstants.General.PAGE_NUMBER, (604 - sora.startPageNumber));
                context.startActivity(QuranPage);
            }
        });

    }

    @Override
    public int getItemCount() {

    return items.size();

    }

    /**
     * Adapter view holder for Sura
     */
    private class ViewHolder extends RecyclerView.ViewHolder {

         TextView soraName, ayaCount, pageNumbers, number;
         RelativeLayout eelativeLayout;
         ViewHolder(View layout) {
            super(layout);

            eelativeLayout=(RelativeLayout)layout.findViewById(R.id.eelativeLayout);
            soraName = (TextView) layout.findViewById(R.id.textView5);
            ayaCount = (TextView) layout.findViewById(R.id.textView6);
            pageNumbers = (TextView) layout.findViewById(R.id.textView7);
            number = (TextView) layout.findViewById(R.id.textView4);
            soraName.setTypeface(customFont);
            ayaCount.setTypeface(customFont);
            pageNumbers.setTypeface(customFont);

        }
    }


    /**
     * Adapter view holder for part
     */
    private class ViewHolderSplitter extends RecyclerView.ViewHolder {
        public TextView jozaNumber , pageNumber;

         ViewHolderSplitter(View layout) {
            super(layout);

            jozaNumber = (TextView) layout.findViewById(R.id.textView8);
            pageNumber = (TextView) layout.findViewById(R.id.tv_page_number);
            jozaNumber.setTypeface(customFont);
            pageNumber.setTypeface(customFont);
        }
    }
     private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
