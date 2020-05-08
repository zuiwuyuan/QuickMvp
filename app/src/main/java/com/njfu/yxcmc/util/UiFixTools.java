package com.njfu.yxcmc.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.njfu.yxcmc.common.CmssSharedPreferences;

import androidx.annotation.NonNull;


/**
 * UI 适配工具类
 * Created by chice on 2017/9/30.
 */

public final class UiFixTools {

    private static int keyBoardHeight = 0;

    private UiFixTools() {
    }

    /**
     * 单位换算——px 转 dp
     *
     * @param context   上下文
     * @param valueInPx 以px为单位的长度数值
     * @return 以dp为单位的长度数值
     */
    public static int pxToDp(@NonNull Context context, int valueInPx) {
        return (int) (valueInPx / context.getResources().getDisplayMetrics().density);
    }

    /**
     * 单位换算——dp 转 px
     *
     * @param context   上下文
     * @param valueInDp 以dp为单位的长度数值
     * @return 以px为单位的长度数值
     */
    public static int dpToPx(Context context, int valueInDp) {
        return (int) (valueInDp * context.getResources().getDisplayMetrics().density);
    }

    /**
     * 单位换算——sp 转 px
     *
     * @param context   上下文
     * @param valueInSp 以sp为单位的字体大小数值
     * @return 以px为单位的字体大小数值
     */
    public static int spToPx(Context context, int valueInSp) {
        return (int) (valueInSp * context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 单位换算——px 转 sp
     *
     * @param context   上下文
     * @param valueInPx 以px为单位的字体大小数值
     * @return 以sp为单位的字体大小数值
     */
    public static int pxToSp(Context context, int valueInPx) {
        return (int) (valueInPx / context.getResources().getDisplayMetrics().scaledDensity);
    }


    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {

        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 设置view的paddingTop属性，以适应状态栏高度
     *
     * @param view view
     */
    public static void setViewTopPaddingFitSystemWindow(View view) {
        if (view == null) return;
        view.setPadding(view.getPaddingLeft(), getStatusBarHeight(view.getContext()), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static int getDisplayHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getDisplayWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static void setMarqueeProperty(TextView textView) {
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setSelected(true);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
    }

    /**
     * 设置TextView自动缩放(在setText之后调用)
     *
     * @param textView  textView
     * @param widthInPx widthInPx
     */
    public static void setTextViewAutoZoomWithMaxWidth(final TextView textView, int widthInPx) {
        setTextViewAutoZoom(textView, false, widthInPx);
    }

    /**
     * 设置TextView自动缩放(在setText之后调用)
     *
     * @param textView textView
     */
    public static void setTextViewAutoZoom(final TextView textView) {
        setTextViewAutoZoom(textView, true, 0);
    }

    private static void setTextViewAutoZoom(final TextView textView, final boolean isUseTextSelfWidth, final int maxWidth) {
        textView.post(new Runnable() {
            @Override
            public void run() {
                int tvWidth = isUseTextSelfWidth ? textView.getWidth() : maxWidth;

                int textAreaWidth = tvWidth - textView.getPaddingLeft() - textView.getPaddingRight() - textView.getCompoundPaddingLeft() - textView.getCompoundPaddingRight();

                String textStr = textView.getText().toString();
                if (TextUtils.isEmpty(textStr) || textStr.length() < 2) {
                    return;
                }
                TextPaint tvPaint = textView.getPaint();

                float textMeasureWidth = tvPaint.measureText(textStr);

                if (textMeasureWidth <= textAreaWidth)
                    return;
                if (textMeasureWidth < textAreaWidth * 3) {
                    float textPxSize = textView.getTextSize();
                    for (float i = textPxSize; i > 8; --i) {
                        tvPaint.setTextSize(i);
                        if (tvPaint.measureText(textStr) < textAreaWidth) {
                            break;
                        }
                    }
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setText(textStr);
                } else {
                    int subStringPosition = 0;
                    for (int i = 1; i < textStr.length(); i++) {
                        if (tvPaint.measureText(textStr.substring(0, i)) >= textMeasureWidth / 2) {
                            subStringPosition = i;
                            break;
                        }
                    }
                    String textInFirstLine = textStr.substring(0, subStringPosition);
                    String textInSecondLine = textStr.substring(subStringPosition, textStr.length());
                    String newTextString = textInFirstLine + "\n" + textInSecondLine;
                    if (textView.getMaxLines() < 2) {
                        textView.setMaxLines(1);
                    }
                    textView.setText(newTextString);
                    if (tvPaint.measureText(textInFirstLine) <= textAreaWidth
                            && tvPaint.measureText(textInSecondLine) <= textAreaWidth) {
                        return;
                    } else {
                        float textPxSize = textView.getTextSize();
                        for (float i = textPxSize; i > 8; --i) {
                            tvPaint.setTextSize(i);
                            if (tvPaint.measureText(textInFirstLine) < textAreaWidth
                                    && tvPaint.measureText(textInSecondLine) < textAreaWidth) {
                                break;
                            }
                        }
                        textView.setText(newTextString);
                    }
                }
            }
        });

    }

    public static int getTextLineHeight(TextPaint textPaint) {
        return textPaint.getFontMetricsInt().bottom - textPaint.getFontMetricsInt().top;
    }

    public static void tryToMeasureKeyboardHeight(@NonNull final Activity activity, @NonNull final View pageRootView) {
        if (keyBoardHeight > 0) return;
        keyBoardHeight = CmssSharedPreferences.getInstance(activity).getInt("KeyboardHeight", 0);
        if (keyBoardHeight > 0) return;
        final int[] bottomNavigationBarHeight = new int[1];
        final float density = activity.getResources().getDisplayMetrics().density;
        final View windowDecor = activity.getWindow().getDecorView();
        pageRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();                // 使用最外层布局填充，进行测算计算
                pageRootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = windowDecor.getRootView().getHeight();
                int bottomDistance = screenHeight - r.bottom;
                if (bottomDistance < 96 * density) {
                    bottomNavigationBarHeight[0] = bottomDistance;
                } else {
                    keyBoardHeight = bottomDistance - bottomNavigationBarHeight[0];
                    CmssSharedPreferences.getInstance(activity).saveInt("KeyboardHeight", keyBoardHeight);
                    pageRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    /**
     * 获取键盘高度
     * 需要先调用 {@link UiFixTools#tryToMeasureKeyboardHeight(Activity ac, View pageRootView)}
     *
     * @return keyBoardHeight
     */
    public static int getKeyBoardHeight() {
        return keyBoardHeight;
    }

    public static void showSoftInputWindow(@NonNull Context context) {
        //显示软键盘
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftInputWindow(@NonNull Context context, IBinder token) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(token, 0);
    }


}
