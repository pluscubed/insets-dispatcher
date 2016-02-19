package com.pluscubed.insetsdispatcher.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import com.pluscubed.insetsdispatcher.InsetsDispatchReceiver;
import com.pluscubed.insetsdispatcher.InsetsDispatcherHelper;
import com.pluscubed.insetsdispatcher.InsetsDispatcherLayoutParams;
import com.pluscubed.insetsdispatcher.InsetsDispatcherLayoutParamsHelper;

public class InsetsDispatcherViewPager extends ViewPager implements InsetsDispatchReceiver {

    private InsetsDispatcherHelper mHelper;

    public InsetsDispatcherViewPager(Context context) {
        this(context, null);
    }

    public InsetsDispatcherViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

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
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return generateDefaultLayoutParams();
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public void dispatchFitSystemWindows(Rect insets) {
        mHelper.onFitSystemWindows(insets);
    }

    public class LayoutParams extends ViewPager.LayoutParams implements InsetsDispatcherLayoutParams {

        private InsetsDispatcherLayoutParamsHelper mHelper;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            mHelper = new InsetsDispatcherLayoutParamsHelper(c, attrs);
        }

        public LayoutParams() {
            super();
        }

        @Nullable
        @Override
        public InsetsDispatcherLayoutParamsHelper getHelper() {
            return mHelper;
        }
    }
}
