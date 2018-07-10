package com.umeitime.common.views;

import android.graphics.Color;

public class QQTipItem {
    private String title;

    private int textColor = Color.WHITE;

    public QQTipItem(String title) {
        this.title = title;
    }

    public QQTipItem(String title, int textColor) {
        this.title = title;

        this.textColor = textColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}