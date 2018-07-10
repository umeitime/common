package com.umeitime.common.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.umeitime.common.R;
import com.umeitime.common.base.BaseActivity;
import com.umeitime.common.base.BaseEvent;
import com.umeitime.common.model.ModifyNameInfo;
import com.umeitime.common.tools.StringUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/10/13.
 */
public class ModifyNameActivity extends BaseActivity {
    private EditText etName;
    private int selectionStart;
    private int selectionEnd;
    private String content="";
    private ModifyNameInfo modifyNameInfo;

    public static void toModifyNameActivity(Context context, ModifyNameInfo modifyNameInfo) {
        Intent intent = new Intent(context, ModifyNameActivity.class);
        intent.putExtra("data", modifyNameInfo);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_modify_name;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            modifyNameInfo = (ModifyNameInfo) bundle.getSerializable("data");
        }
        etName = findViewById(R.id.etContent);
        initToolbar(modifyNameInfo.title);

        if(modifyNameInfo.maxLines>1){
            etName.setMinLines(modifyNameInfo.maxLines);
        }else{
            etName.setMaxLines(1);
        }
        if(modifyNameInfo.intputType>0) {
            etName.setInputType(modifyNameInfo.intputType);
        }
        if (StringUtils.isNotBlank(modifyNameInfo.name)){
            etName.setText(modifyNameInfo.name);
            etName.setSelection(modifyNameInfo.name.length());
        }else {
            etName.setHint("请填写"+modifyNameInfo.title);
        }
    }

    @Override
    protected void initEvent() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectionStart = etName.getSelectionStart();
                selectionEnd = etName.getSelectionEnd();
                content = s.toString().trim();
                etName.removeTextChangedListener(this);//防止内存溢出，必须写。
                if(modifyNameInfo!=null) {
                    while (s.toString().trim().replaceAll("[^\\x00-\\xff]", "**").length() > modifyNameInfo.length) {//循环到30个字节为止
                        s.delete(selectionStart - 1, selectionEnd);
                        selectionStart--;
                        selectionEnd--;
                    }
                    etName.setText(s);//重新设置字符串
                    etName.setSelection(selectionStart);//重新设置光标的位置
                    etName.addTextChangedListener(this);//配合removeTextChangedListener
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_submit){
            EventBus.getDefault().post(new BaseEvent.ModifyNameEvent(content));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
