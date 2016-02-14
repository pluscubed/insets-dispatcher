package com.pluscubed.insetsdispatcher.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import com.pluscubed.insetsdispatcher.InsetsDispatcherViewGroup;

/**
 * This view sets it's width/height automatically to the left, top, right or bottom inset. Can be used to draw i.E. a color under the status/navigation bar on KitKat.
 * Doesn't need to be placed inside an InsetsDispatcherLayout.
 */
public class InsetLayout extends FrameLayout implements InsetsDispatcherViewGroup {
    private final int windowInset;

    public InsetLayout(Context context) {
        this(context, null, 0);
    }

    public InsetLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsetLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                com.pluscubed.windowinsetsdispatcher.R.styleable.InsetLayout,
                0, 0);

        windowInset = a.getInt(com.pluscubed.windowinsetsdispatcher.R.styleable.InsetLayout_windowInset, -1);

        a.recycle();
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
        if (insets == null) {
            return null;
        } else {
            setInsets(new Rect(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom()));
        }
        return insets;
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        onFitSystemWindows(insets);
        return false;
    }

    @Override
    public void dispatchFitSystemWindows(Rect insets) {
        onFitSystemWindows(insets);
    }

    public void onFitSystemWindows(Rect insets) {
        setInsets(insets);
    }

    private void setInsets(Rect insets) {
        if (insets == null || windowInset == -1) return;
        final ViewGroup.LayoutParams lp = getLayoutParams();
        switch (windowInset) {
            case 0:
                lp.width = insets.left;
            case 1:
                lp.height = insets.top;
            case 2:
                lp.width = insets.right;
            case 3:
                lp.height = insets.bottom;
        }
        setLayoutParams(lp);
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
