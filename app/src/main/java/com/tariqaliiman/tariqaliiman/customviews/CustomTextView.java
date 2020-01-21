package com.tariqaliiman.tariqaliiman.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;


/**
 * Created by baher on 01/03/2017.
 */

public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /*public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }*/

    public CustomTextView(Context context) {
        super(context);

        init(context);
    }

    public void init(Context context) {
        Typeface tf;
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/elmasri.ttf");
        setTypeface(tf ,1);

    }




}
