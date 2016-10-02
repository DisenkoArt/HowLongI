package com.disenkoart.howlongi.customView;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.disenkoart.howlongi.FontManager;
import com.disenkoart.howlongi.R;

/**
 * Created by Артём on 01.10.2016.
 */
public class EmptyView extends LinearLayout {
    private TextView mEmptyTextView;

    public EmptyView(Context context) {
        super(context);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /* -- START PUBLIC METHODS -- */

    public void setText(String text){
        mEmptyTextView.setText(text);
    }

    /* -- END PUBLIC METHODS -- */

    /* -- START PRIVATE METHODS -- */

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.empty_view_layout, this);
        mEmptyTextView = (TextView) findViewById(R.id.empty_view_text_view);
        mEmptyTextView.setTypeface(FontManager.UNI_SANS);
    }

    /* -- END PRIVATE METHODS -- */

}
