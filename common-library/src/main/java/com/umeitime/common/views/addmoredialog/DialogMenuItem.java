package com.umeitime.common.views.addmoredialog;

/**
 * Author：June on 2017/06/08.
 */

public class DialogMenuItem {
    private int icon;
    private String text;

    public DialogMenuItem() {}

    public DialogMenuItem(String text) {
        this.text = text;
    }

    public DialogMenuItem(int iconId, String text) {
        this.icon = iconId;
        this.text = text;
    }

    public int getIcon() {
        return icon;

}
    public void setIcon(int iconId) {
        this.icon = iconId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
