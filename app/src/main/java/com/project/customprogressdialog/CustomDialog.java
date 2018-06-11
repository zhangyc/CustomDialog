package com.project.customprogressdialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by zhangyc on 2018/6/7.
 */

public class CustomDialog extends DialogFragment {
    private ProgressBar mProgress;
    private TextView mProgressNumber,mProgressPercent,mProgressMessage,mPercentTv;
    private RoundCornerImageView mProgressIv;
    private ImageView mBotIv;
    public static final String F_TAG = "f_tag_ChangeUserProgressDialog";
    private String msg;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mProgressNumber = (TextView) view.findViewById(R.id.progress_number);
        mProgressPercent = (TextView) view.findViewById(R.id.progress_percent);
        mProgressMessage = (TextView) view.findViewById(R.id.progress_message);
        //新增进度条
        mProgressIv = (RoundCornerImageView)view.findViewById(R.id.p_cover_iv);
        mBotIv = (ImageView)view.findViewById(R.id.p_bot_iv);
        mPercentTv = (TextView)view.findViewById(R.id.percent_tv);
        mProgressMessage.setText(msg);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.common_progress_dialog,container,false);
    }
    public void updatePercent(int percent) {
//        mPercent = percent;
        Log.i("mPercent","mPercent="+percent);
        mPercentTv.setText(String.format(Locale.CHINA, "%2d%%", percent));//
        float percentFloat = percent / 100.0f;//除以100，得到百分比
        final int ivWidth = mBotIv.getWidth();//获取总长度
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mProgressIv.getLayoutParams();
        int marginEnd = (int) ((1 - percentFloat) * ivWidth); //获取剩下的长度
        lp.width = ivWidth - marginEnd;
        mProgressIv.setLayoutParams(lp);
        mProgressIv.postInvalidate();
    }
    public void setMessage(String message){
        this.msg=message;
    }
    @Override
    public void onStart() {
        super.onStart();
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.CENTER;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }
}
