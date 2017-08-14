package com.umeitime.common.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by hujunwei on 17/6/10.
 */

public class LimitRowEditText extends EditText {

    public LimitRowEditText(Context context) {
        super(context);
    }
    public LimitRowEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LimitRowEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        addTextChangedListener(new TextWatcher() {
            String text = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int lines = getLineCount();
                // 限制入行数
                if (lines > 5) {
                    String str = s.toString();
                    int cursorStart = getSelectionStart();
                    int cursorEnd = getSelectionEnd();
                    if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) {
                        str = str.substring(0, cursorStart - 1) + str.substring(cursorStart);
                    } else {
                        str = str.substring(0, s.length() - 1);
                    }
                    // setText会触发afterTextChanged的递归
                    setText(str);
                    // setSelection用的索引不能使用str.length()否则会越界
                    setSelection(getText().length());
                } else if (!text.equals(s.toString())) {
                    int end = getSelectionEnd();
                    setText(s);
                    setSelection(end);
                }
            }
        });
    }
}
