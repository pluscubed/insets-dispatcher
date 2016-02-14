package com.pluscubed.insetsdispatcher;

import android.graphics.Rect;

public interface InsetsDispatchReceiver {
    void dispatchFitSystemWindows(Rect insets);
}
