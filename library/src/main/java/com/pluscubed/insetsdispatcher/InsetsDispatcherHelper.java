package com.pluscubed.insetsdispatcher;

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

import com.pluscubed.windowinsetsdispatcher.R;

public class InsetsDispatcherHelper {

    public static final int FLAG_INSETS_TOP = 0x1;
    public static final int FLAG_INSETS_BOTTOM = 0x2;
    private static final String TAG_INSETS_APPLIED = "insets_applied";
    private boolean mInsetsTop;
    private boolean mInsetsBottom;
    private boolean mInsetsUseMargin;

    private ViewGroup mView;

    private Rect mInsets;

    public InsetsDispatcherHelper(Context context, AttributeSet attrs, ViewGroup view) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.WindowInsetsLayout,
                0, 0);

        int insetsFlags = a.getInt(R.styleable.WindowInsetsLayout_windowInsets, 0);
        mInsetsTop = (insetsFlags & FLAG_INSETS_TOP) == FLAG_INSETS_TOP;
        mInsetsBottom = (insetsFlags & FLAG_INSETS_BOTTOM) == FLAG_INSETS_BOTTOM;
        mInsetsUseMargin = a.getBoolean(R.styleable.WindowInsetsLayout_windowInsetsUseMargin, false);

        a.recycle();

        mView = view;
    }

    public void onAddView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            onApplyWindowInsets(null);
        } else {
            onFitSystemWindows(null);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {

        if (insets == null) {
            return null;
        } else {
            mInsets = new Rect(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
        }


        for (int i = 0; i < mView.getChildCount(); i++) {
            final View child = mView.getChildAt(i);

            if (!(child instanceof InsetsDispatcherViewGroup)) {
                InsetsDispatcherLayoutParamsHelper helper = ((InsetsDispatcherLayoutParams) child.getLayoutParams()).getHelper();
                applyInsets(mInsets, child, helper.insetsTop, helper.insetsBottom, helper.insetsUseMargin);
            }
        }

        applyInsets(mInsets, mView, mInsetsTop, mInsetsBottom, mInsetsUseMargin);

        ViewCompat.postInvalidateOnAnimation(mView);

        return insets;
    }

    public void onFitSystemWindows(Rect insets) {
        mInsets = insets;

        if (mInsets == null) {
            return;
        }

        for (int i = 0; i < mView.getChildCount(); i++) {
            final View child = mView.getChildAt(i);

            if (child instanceof InsetsDispatcherViewGroup) {
                ((InsetsDispatcherViewGroup) child).dispatchFitSystemWindows(insets);
            } else {
                InsetsDispatcherLayoutParamsHelper helper = ((InsetsDispatcherLayoutParams) child.getLayoutParams()).getHelper();
                applyInsets(mInsets, child, helper.insetsTop, helper.insetsBottom, helper.insetsUseMargin);
            }
        }

        applyInsets(mInsets, mView, mInsetsTop, mInsetsBottom, mInsetsUseMargin);

        ViewCompat.postInvalidateOnAnimation(mView);
    }

    private void applyInsets(Rect insets, View view, boolean insetsTop, boolean insetsBottom, boolean useMargin) {
        if (view.getTag() != null && view.getTag().equals(TAG_INSETS_APPLIED)) {
            return;
        }

        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLp = null;

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            marginLp = (ViewGroup.MarginLayoutParams) lp;
        } else if (useMargin) {
            return;
        }

        int paddingTop = useMargin ? marginLp.topMargin : view.getPaddingTop();
        int paddingBottom = useMargin ? marginLp.bottomMargin : view.getPaddingBottom();

        if (insetsTop) {
            paddingTop += insets.top;
        }

        if (insetsBottom) {
            paddingBottom += insets.bottom;
        }

        if (useMargin) {
            marginLp.topMargin = paddingTop;
            marginLp.bottomMargin = paddingBottom;
            view.setLayoutParams(lp);
        } else {
            view.setPadding(view.getPaddingLeft(), paddingTop, view.getPaddingRight(), paddingBottom);
        }

        view.setTag(TAG_INSETS_APPLIED);
    }
}
