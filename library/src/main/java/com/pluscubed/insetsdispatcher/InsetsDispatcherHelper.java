package com.pluscubed.insetsdispatcher;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

public class InsetsDispatcherHelper {

    public static final int FLAG_INSETS_LEFT = 0x1;
    public static final int FLAG_INSETS_TOP = 0x2;
    public static final int FLAG_INSETS_RIGHT = 0x3;
    public static final int FLAG_INSETS_BOTTOM = 0x4;

    private boolean mUseLeftInset;
    private boolean mUseTopInset;
    private boolean mUseRightInset;
    private boolean mUseBottomInset;
    private boolean mInsetsUseMargin;

    private ViewGroup mView;

    private Rect mInsets;

    public InsetsDispatcherHelper(Context context, AttributeSet attrs, ViewGroup view) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.WindowInsetsLayout,
                0, 0);

        int insetsFlags = a.getInt(R.styleable.WindowInsetsLayout_windowInsets, 0);
        mUseLeftInset = (insetsFlags & FLAG_INSETS_LEFT) == FLAG_INSETS_LEFT;
        mUseTopInset = (insetsFlags & FLAG_INSETS_TOP) == FLAG_INSETS_TOP;
        mUseRightInset = (insetsFlags & FLAG_INSETS_RIGHT) == FLAG_INSETS_RIGHT;
        mUseBottomInset = (insetsFlags & FLAG_INSETS_BOTTOM) == FLAG_INSETS_BOTTOM;
        mInsetsUseMargin = a.getBoolean(R.styleable.WindowInsetsLayout_windowInsetsUseMargin, false);

        a.recycle();

        mView = view;
        mView.setFitsSystemWindows(true);
    }

    public void onAddView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            onApplyWindowInsets(null);
        } else {
            onFitSystemWindows(null);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    public WindowInsets onApplyWindowInsets(@Nullable WindowInsets insets) {
        if (insets != null) {
            mInsets = new Rect(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
        }

        for (int i = 0; i < mView.getChildCount(); i++) {
            final View child = mView.getChildAt(i);

            if (!(child instanceof InsetsDispatchReceiver)) {
                InsetsDispatcherLayoutParamsHelper helper = ((InsetsDispatcherLayoutParams) child.getLayoutParams()).getHelper();
                if (helper != null) {
                    applyInsets(mInsets, child, helper.useLeftInset, helper.useTopInset, helper.useRightInset, helper.useBottomInset, helper.insetsUseMargin);
                } else {
                    applyInsets(mInsets, child, false, false, false, false, false);
                }
            }
        }

        applyInsets(mInsets, mView, mUseLeftInset, mUseTopInset, mUseRightInset, mUseBottomInset, mInsetsUseMargin);

        ViewCompat.postInvalidateOnAnimation(mView);

        return insets;
    }

    public void onFitSystemWindows(@Nullable Rect insets) {
        if (mInsets != null) {
            mInsets = insets;
        }

        for (int i = 0; i < mView.getChildCount(); i++) {
            final View child = mView.getChildAt(i);

            if (child instanceof InsetsDispatchReceiver) {
                ((InsetsDispatchReceiver) child).dispatchFitSystemWindows(insets);
            } else {
                InsetsDispatcherLayoutParamsHelper helper = ((InsetsDispatcherLayoutParams) child.getLayoutParams()).getHelper();
                if (helper != null) {
                    applyInsets(mInsets, child, helper.useLeftInset, helper.useTopInset, helper.useRightInset, helper.useBottomInset, helper.insetsUseMargin);
                } else {
                    applyInsets(mInsets, child, false, false, false, false, false);
                }
            }
        }

        applyInsets(mInsets, mView, mUseLeftInset, mUseTopInset, mUseRightInset, mUseBottomInset, mInsetsUseMargin);

        ViewCompat.postInvalidateOnAnimation(mView);
    }

    private void applyInsets(@Nullable Rect insets, @NonNull final View view, final boolean useLeft, final boolean useTop, final boolean useRight, final boolean useBottom, final boolean useMargin) {
        if (insets == null) {
            insets = new Rect();
        }

        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginLp = null;

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            marginLp = (ViewGroup.MarginLayoutParams) lp;
        } else if (useMargin) {
            return;
        }

        int left = useMargin ? marginLp.leftMargin : view.getPaddingLeft();
        int top = useMargin ? marginLp.topMargin : view.getPaddingTop();
        int right = useMargin ? marginLp.rightMargin : view.getPaddingRight();
        int bottom = useMargin ? marginLp.bottomMargin : view.getPaddingBottom();

        InsetInfo insetInfo = null;
        if (view.getTag() != null && view.getTag() instanceof InsetInfo) {
            insetInfo = (InsetInfo) view.getTag();
        }

        if (useLeft) {
            if (insetInfo != null) {
                // remove the previous margin/padding first
                left -= useMargin ? insetInfo.marginInsets.left : insetInfo.paddingInsets.left;
            }
            left += insets.left;
        }

        if (useTop) {
            if (insetInfo != null) {
                // remove the previous margin/padding first
                top -= useMargin ? insetInfo.marginInsets.top : insetInfo.paddingInsets.top;
            }
            top += insets.top;
        }

        if (useRight) {
            if (insetInfo != null) {
                // remove the previous margin/padding first
                right -= useMargin ? insetInfo.marginInsets.right : insetInfo.paddingInsets.right;
            }
            right += insets.right;
        }

        if (useBottom) {
            if (insetInfo != null) {
                // remove the previous margin/padding first
                bottom -= useMargin ? insetInfo.marginInsets.bottom : insetInfo.paddingInsets.bottom;
            }
            bottom += insets.bottom;
        }

        if (useMargin) {
            marginLp.leftMargin = left;
            marginLp.topMargin = top;
            marginLp.rightMargin = right;
            marginLp.bottomMargin = bottom;
            view.setLayoutParams(lp);
        } else {
            view.setPadding(left, top, right, bottom);
        }

        if (insetInfo == null) {
            insetInfo = useMargin ? new InsetInfo(new Rect(), insets) : new InsetInfo(insets, new Rect());
        } else {
            if (useMargin) {
                insetInfo.marginInsets = insets;
            } else {
                insetInfo.paddingInsets = insets;
            }
        }
        view.setTag(insetInfo);
    }

    private static class InsetInfo {
        @NonNull
        public Rect paddingInsets;
        @NonNull
        public Rect marginInsets;

        public InsetInfo(@NonNull Rect paddingInsets, @NonNull Rect marginInsets) {
            this.paddingInsets = paddingInsets;
            this.marginInsets = marginInsets;
        }
    }
}
