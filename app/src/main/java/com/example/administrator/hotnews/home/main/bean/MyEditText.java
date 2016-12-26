package com.example.administrator.hotnews.home.main.bean;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.example.administrator.hotnews.R;

/**
 * Created by Administrator on 2016/11/20.
 */

public class MyEditText extends EditText {
    private Context mcontext;
    private Drawable imgInable;
    private Drawable imgAble;
    private final static String TAG = "MyEditText";


    public MyEditText(Context context) {
        super(context);
        this.mcontext=context;
        init();

    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mcontext=context;
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mcontext=context;
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mcontext=context;
        init();
    }
    private void init() {
        imgInable = mcontext.getResources().getDrawable(R.drawable.delete_gray);
        imgAble = mcontext.getResources().getDrawable(R.drawable.delete1);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }
    private void setDrawable() {
        if(length() < 1)
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Log.e(TAG, "eventX = " + eventX + "; eventY = " + eventY);
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if(rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
