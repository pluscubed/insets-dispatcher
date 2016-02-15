package com.pluscubed.windowinsetsdispatchersample;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pluscubed.insetsdispatchersample.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setDrawUnderStatusbar(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setStatusbarColor(withAlpha(Color.RED, 0.5f));
        setNavbarColor(withAlpha(Color.GREEN, 0.5f));
    }

    public void setDrawUnderStatusbar(boolean drawUnderStatusbar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setAllowDrawUnderStatusBar(getWindow());
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            setStatusBarTranslucent(getWindow());
    }

    @TargetApi(19)
    public static void setStatusBarTranslucent(@NonNull Window window) {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public static void setAllowDrawUnderStatusBar(@NonNull Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @ColorInt
    public static int shiftColor(@ColorInt int color, @FloatRange(from = 0.0D, to = 2.0D) float by) {
        if (by == 1.0F) {
            return color;
        } else {
            int alpha = Color.alpha(color);
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[2] *= by;
            return (alpha << 24) + (16777215 & Color.HSVToColor(hsv));
        }
    }

    @ColorInt
    public static int darkenColor(@ColorInt int color) {
        return shiftColor(color, 0.9F);
    }

    @ColorInt
    public static int withAlpha(@ColorInt int baseColor, @FloatRange(from = 0.0D, to = 1.0D) float alpha) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255.0F))) << 24;
        int rgb = 16777215 & baseColor;
        return a + rgb;
    }

    public void setStatusbarColor(int color) {
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setStatusBarColor(darkenColor(color));
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        final View statusBar = getWindow().getDecorView().getRootView().findViewById(R.id.status_bar);
        if (statusBar != null) {
            statusBar.setBackgroundColor(color);
        }
//        }
    }

    public void setNavbarColor(int color) {
        final View navBar = getWindow().getDecorView().getRootView().findViewById(R.id.nav_bar);
        if (navBar != null) {
            navBar.setBackgroundColor(color);
        }
    }

}
