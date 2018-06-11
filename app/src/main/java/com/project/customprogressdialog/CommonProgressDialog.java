package com.project.customprogressdialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/*
 * @创建者     wjh
 * @创建时间   2016/8/5 21:22
 * @描述        ${自定义下载的进度提示框}
 */
public class CommonProgressDialog extends AlertDialog {


    private ProgressBar mProgress;
    private TextView mProgressNumber;
    private TextView mProgressPercent;
    private TextView mProgressMessage;

    private Handler mViewUpdateHandler;
    private int mMax;
    private CharSequence mMessage;
    private boolean mHasStarted;
    private int mProgressVal;

    private String TAG = "CommonProgressDialog";
    private String mProgressNumberFormat;
    private NumberFormat mProgressPercentFormat;
    private RoundCornerImageView mProgressIv;
    private ImageView mBotIv;
    private TextView mPercentTv;

    public CommonProgressDialog(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initFormats();
    }


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_progress_dialog);

        mProgress = (ProgressBar) findViewById(R.id.progress);
        mProgressNumber = (TextView) findViewById(R.id.progress_number);
        mProgressPercent = (TextView) findViewById(R.id.progress_percent);
        mProgressMessage = (TextView) findViewById(R.id.progress_message);
        //新增进度条
        mProgressIv = (RoundCornerImageView)findViewById(R.id.p_cover_iv);
        mBotIv = (ImageView)findViewById(R.id.p_bot_iv);
        mPercentTv = (TextView)findViewById(R.id.percent_tv);

        mViewUpdateHandler = new Handler() {


            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                int progress = mProgress.getProgress();
                int max = mProgress.getMax();
                double dProgress = (double) progress / (double) (1024 * 1024);
                double dMax = (double) max / (double) (1024 * 1024);
                if (mProgressNumberFormat != null) {
                    String format = mProgressNumberFormat;
                    mProgressNumber.setText(String.format(format, dProgress, dMax));
                } else {
                    mProgressNumber.setText("");
                }
                if (mProgressPercentFormat != null) {
                    double percent = (double) progress / (double) max;
                    String string=new DecimalFormat("0").format(percent*100);
                    Log.i("mPercent2",">>>mPercent="+string);

                    updatePercent(Integer.valueOf(string));
                    SpannableString tmp = new SpannableString(mProgressPercentFormat.format(percent));
                    tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                            0, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mProgressPercent.setText(tmp);
                } else {
                    mProgressPercent.setText("");
                }
            }

        };
        onProgressChanged();
        if (mMessage != null) {
            setMessage(mMessage);
        }
        if (mMax > 0) {
            setMax(mMax);
        }
        if (mProgressVal > 0) {
            setProgress(mProgressVal);
        }

        //设置监听
//    tvCancel.setOnClickListener(this);

    }

    private void initFormats() {
        mProgressNumberFormat = "%1.2fM/%2.2fM";
        mProgressPercentFormat = NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    private void onProgressChanged() {
        mViewUpdateHandler.sendEmptyMessage(0);


    }

    public void setProgressStyle(int style) {
        //mProgressStyle = style;
    }

    public int getMax() {
        if (mProgress != null) {
            return mProgress.getMax();
        }
        return mMax;
    }

    public void setMax(int max) {
        if (mProgress != null) {
            mProgress.setMax(max);
            onProgressChanged();
        } else {
            mMax = max;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        if (mProgress != null) {
            mProgress.setIndeterminate(indeterminate);
        }
        //      else {
        //            mIndeterminate = indeterminate;
        //        }
    }

    public void setProgress(int value) {
        if (mHasStarted) {
            mProgress.setProgress(value);
            onProgressChanged();
        } else {
            mProgressVal = value;
        }
    }


    @Override
    public void setMessage(CharSequence message) {
        // TODO Auto-generated method stub
        //super.setMessage(message);
        if (mProgressMessage != null) {
            mProgressMessage.setText(message);
        } else {
            mMessage = message;
        }
    }


    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mHasStarted = true;
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mHasStarted = false;
    }

    /**
     * 声明成员变量
     */
    public OnOkAndCancelListener mOnOkAndCancelListener;

    /**
     * 暴露接口取消方法,如果需要确定,另行添加
     */
    public interface OnOkAndCancelListener {
        void onCancel(View v);
    }

    /**
     * 暴露方法,设置监听
     */
    public void setOnOkAndCancelListener(OnOkAndCancelListener mOnOkAndCancelListener) {
        this.mOnOkAndCancelListener = mOnOkAndCancelListener;
    }
    int mPercent = 0; // 百分比

    public void updatePercent(int percent) {
        mPercent = percent;
        Log.i("mPercent",">>>mPercent="+mPercent);
        mPercentTv.setText(String.format(Locale.CHINA, "%2d%%", mPercent));
        float percentFloat = mPercent / 100.0f;
        final int ivWidth = mBotIv.getWidth();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mProgressIv.getLayoutParams();
        int marginEnd = (int) ((1 - percentFloat) * ivWidth);
        lp.width = ivWidth - marginEnd;
        mProgressIv.setLayoutParams(lp);
        mProgressIv.postInvalidate();
    }

}