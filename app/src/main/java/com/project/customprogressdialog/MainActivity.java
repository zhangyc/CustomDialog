package com.project.customprogressdialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CommonProgressDialog.OnOkAndCancelListener {

    private CustomDialog mDialog;
    private int dataCount = 0;// 记录累加的值
    private boolean isCancel; // 用来判断是否点击了取消
    private ProgressThread mProgressThread;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tvShow = (TextView) findViewById(R.id.tv_show);
        tvShow.setOnClickListener(this);
    }
    public void showDialog() {

        mDialog = new CustomDialog();
        mDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTranslucentOrigin);
        mDialog.show(getSupportFragmentManager(), CustomDialog.F_TAG);
        mDialog.setMessage("加载中，请稍后");
        mProgressThread = new ProgressThread();
        mProgressThread.start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_show:
                showDialog();
                break;
            default:
                break;
        }

    }

    @Override
    public void onCancel(View v) {
        mDialog.dismiss();
        isCancel = true;
    }
    private class ProgressThread extends Thread {

        private int progress = 0;

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                progress++;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                if (progress > 100) {
                    progress = 0;
                }
                final int p = progress;
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.updatePercent(p);
                    }
                });
            }
        }
    }

}