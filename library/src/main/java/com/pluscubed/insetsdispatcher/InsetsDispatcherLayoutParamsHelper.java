package com.pluscubed.insetsdispatcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class InsetsDispatcherLayoutParamsHelper {

    public boolean useLeftInset;
    public boolean useTopInset;
    public boolean useRightInset;
    public boolean useBottomInset;
    public boolean insetsUseMargin;

    public InsetsDispatcherLayoutParamsHelper(Context c, AttributeSet attrs) {
        if (c == null) {
            return;
        }

        TypedArray a = c.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.WindowInsetsLayout_Layout,
                0, 0);

        int insetsFlags = a.getInt(R.styleable.WindowInsetsLayout_Layout_layout_windowInsets, 0);
        useLeftInset = (insetsFlags & InsetsDispatcherHelper.FLAG_INSETS_LEFT) == InsetsDispatcherHelper.FLAG_INSETS_LEFT;
        useTopInset = (insetsFlags & InsetsDispatcherHelper.FLAG_INSETS_TOP) == InsetsDispatcherHelper.FLAG_INSETS_TOP;
        useRightInset = (insetsFlags & InsetsDispatcherHelper.FLAG_INSETS_RIGHT) == InsetsDispatcherHelper.FLAG_INSETS_RIGHT;
        useBottomInset = (insetsFlags & InsetsDispatcherHelper.FLAG_INSETS_BOTTOM) == InsetsDispatcherHelper.FLAG_INSETS_BOTTOM;
        insetsUseMargin = a.getBoolean(R.styleable.WindowInsetsLayout_Layout_layout_windowInsetsUseMargin, false);

        a.recycle();
    }
}
