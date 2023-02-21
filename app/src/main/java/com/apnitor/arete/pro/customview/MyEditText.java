package com.apnitor.arete.pro.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.apnitor.arete.pro.R;

import androidx.appcompat.widget.AppCompatEditText;

public class MyEditText extends AppCompatEditText {
    public MyEditText(Context context) {
        super(context);
        init(null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyTextView);
            String fontName = a.getString(R.styleable.MyTextView_fontName);

            try {
                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                    setTypeface(myTypeface);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            a.recycle();
        }
    }
}
