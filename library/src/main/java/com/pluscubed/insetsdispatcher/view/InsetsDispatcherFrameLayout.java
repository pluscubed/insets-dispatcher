package com.pluscubed.insetsdispatcher.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import com.pluscubed.insetsdispatcher.InsetsDispatchReceiver;
import com.pluscubed.insetsdispatcher.InsetsDispatcherHelper;
import com.pluscubed.insetsdispatcher.InsetsDispatcherLayoutParams;
import com.pluscubed.insetsdispatcher.InsetsDispatcherLayoutParamsHelper;

public class InsetsDispatcherFrameLayout extends FrameLayout implements InsetsDispatchReceiver {

    private InsetsDispatcherHelper mHelper;

    public InsetsDispatcherFrameLayout(Context context) {
        this(context, null, 0);
    }

    public InsetsDispatcherFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsetsDispatcherFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mHelper = new InsetsDispatcherHelper(context, attrs, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            requestApplyInsets();
        } else {
            requestFitSystemWindows();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return mHelper.onApplyWindowInsets(insets);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        mHelper.onFitSystemWindows(insets);

        return false;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        mHelper.onAddView();
    }


    @Override
    protected FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public void dispatchFitSystemWindows(Rect insets) {
        mHelper.onFitSystemWindows(insets);
    }

    public class LayoutParams extends FrameLayout.LayoutParams implements InsetsDispatcherLayoutParams {

        private InsetsDispatcherLayoutParamsHelper mHelper;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            mHelper = new InsetsDispatcherLayoutParamsHelper(c, attrs);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @Override
        public InsetsDispatcherLayoutParamsHelper getHelper() {
            return mHelper;
        }
    }
}