package com.tariqaliiman.tariqaliiman.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

/**
 * Created by 12 on 01/03/2017.
 */

public class CustomButton extends AppCompatButton {
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomButton(Context context) {
        super(context);
        init(context);
    }
    public void init(Context context) {
        Typeface tf;
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/elmasri.ttf");
        setTypeface(tf ,1);

    }




}
