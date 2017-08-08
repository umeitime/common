package com.umeitime.common.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.umeitime.common.R;
import com.umeitime.common.tools.ToastUtil;

/**
 * Created by hujunwei on 16/8/16.
 */
public class LoadingDialog {
    TextView tvMsg;
    public Dialog loadingDialog;
    Context context;
    public LoadingDialog(Context context){
        this.context=context;
        createLoadingDialog(context);
    }
    public LoadingDialog(Context context, CharSequence msg){
        this.context=context;
        createLoadingDialog(context,msg);
    }
    public void createLoadingDialog(Context context, CharSequence msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        tvMsg = (TextView) v.findViewById(R.id.tv_msg);// 提示文字
        // 加载动画
        tvMsg.setText(msg);// 设置加载信息
        loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);
        loadingDialog.setCanceledOnTouchOutside(true);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
    }
    public void createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        tvMsg = (TextView) v.findViewById(R.id.tv_msg);// 提示文字
        // 加载动画
        loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
    }
    public void setMsg(String msg){
        tvMsg.setText(msg);
    }

    public void loadFinish(String msg){
        if(loadingDialog!=null){
            dismissDialog();
            ToastUtil.showToast(context,msg);
        }
    }

    public void showDialog(){
        if(loadingDialog!=null){
            loadingDialog.show();
        }
    }

    public void dismissDialog(){
        if(loadingDialog!=null&&loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }
}
