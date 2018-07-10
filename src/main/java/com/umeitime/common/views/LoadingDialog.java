package com.umeitime.common.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.umeitime.common.R;
import com.umeitime.common.tools.StringUtils;
/**
 * Created by hujunwei on 16/8/16.
 */
public class LoadingDialog {
    public TextView tvMsg;
    public SmileView smileView;
    public Dialog loadingDialog;

    public LoadingDialog(Context context, CharSequence msg){
        createLoadingDialog(context, msg);
    }

    public void createLoadingDialog(Context context, CharSequence msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout =  v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        tvMsg = v.findViewById(R.id.tv_msg);// 提示文字
        smileView = v.findViewById(R.id.smileView);// 提示文字
        if(StringUtils.isNotBlank(msg.toString()))
            tvMsg.setText(msg);// 设置加载信息
        loadingDialog = new Dialog(context, R.style.DialogLoading);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(true);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
    }

    public void setMsg(String msg){
        tvMsg.setText(msg);
    }


    public void showDialog(){
        if(loadingDialog!=null){
            loadingDialog.show();
        }
        if(smileView!=null)smileView.startAnimation();
    }

    public void dismissDialog(){
        if(loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
        if(smileView!=null)smileView.stopAnimation();
    }
}
