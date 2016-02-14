package com.pluscubed.insetsdispatcher.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import com.pluscubed.insetsdispatcher.InsetsDispatchReceiver;
import com.pluscubed.insetsdispatcher.R;

/**
 * This view sets its width/height automatically to the left, top, right or bottom inset. Can be used to draw e.g. a color under the status/navigation bar on KitKat.
 */
public class InsetView extends View implements InsetsDispatchReceiver {

    private final int mWindowInset;

    public InsetView(Context context) {
        this(context, null, 0);
    }

    public InsetView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.InsetView,
                0, 0);

        mWindowInset = a.getInt(R.styleable.InsetView_windowInset, -1);

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
        if (insets == null || mWindowInset == -1) return;
        final ViewGroup.LayoutParams lp = getLayoutParams();
        switch (mWindowInset) {
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
