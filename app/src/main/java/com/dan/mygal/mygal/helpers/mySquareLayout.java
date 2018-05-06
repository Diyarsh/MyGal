package com.dan.mygal.mygal.helpers;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

// Класс для отображения картинок в пропорциональном масшиабе (вписанных в квдратную область)

class mySquareLayout extends RelativeLayout {

    // Везде вызываем родительские методы
    public mySquareLayout(Context context) {
        super(context);
    }

    public mySquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mySquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public mySquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Устанавливаем квадратную область
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}