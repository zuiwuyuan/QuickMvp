package com.njfu.yxcmc.util;


import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.StringRes;


public enum ToastUtil {
    INSTANCE;
    private int radiusInPx = 0;
    private int toastViewWidthInPx = 0;
    private ShapeDrawable toastBg = null;

    /**
     * Toast对象
     */
    private Toast toast = null;

    /**
     * 显示Toast
     *
     * @param context  context
     * @param message  message
     * @param duration duration
     */
    public void showToast(Context context, String message, @ToastDuration int duration) {
        if (context == null) return;
        if (toastBg == null) {
            toastViewWidthInPx = (int) (UiFixTools.getDisplayWidth(context) * 0.7f);
            radiusInPx = UiFixTools.dpToPx(context, 12);
            float[] outerRadii = {radiusInPx, radiusInPx, radiusInPx, radiusInPx, radiusInPx, radiusInPx, radiusInPx, radiusInPx};
            RoundRectShape shape = new RoundRectShape(outerRadii, null, null);
            toastBg = new ShapeDrawable(shape);
            toastBg.getPaint().setColor(0xB0000000);
        }
        if (toast != null) {
            toast.cancel();
            toast = null;
        }

        TextView tv = new TextView(context);
        tv.setBackground(toastBg);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(radiusInPx, 2 * radiusInPx, radiusInPx, 2 * radiusInPx);
        tv.setWidth(toastViewWidthInPx);
        tv.setText(message);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f);
        tv.setTextColor(0xFFFFFFFF);
        tv.setShadowLayer(2.75f, 0, 0, 0xBB000000);

        toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(tv);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void showToast(Context context, @StringRes int id, @ToastDuration int duration) {
        showToast(context, context.getString(id), duration);
    }

    public void showToast(Context context, @StringRes int id) {
        showToast(context, id, Toast.LENGTH_SHORT);
    }

    public void showToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ToastDuration {
    }
}
