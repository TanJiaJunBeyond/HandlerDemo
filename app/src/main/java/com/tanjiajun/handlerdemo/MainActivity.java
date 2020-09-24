package com.tanjiajun.handlerdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/**
 * Created by TanJiaJun on 2020/9/25.
 */
public class MainActivity extends AppCompatActivity {

    // 消息类别
    private static final int MESSAGE_CODE_MAIN = 0;

    // 继承Handler，并且重写它的handleMessage(Message msg)方法
    private static final class MainHandler extends Handler {

        // 声明Activity的弱引用对象
        private final WeakReference<Activity> activityRef;

        MainHandler(Activity activity) {
            // 创建Activity的弱引用对象
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            // 得到MainActivity对象
            MainActivity activity = (MainActivity) activityRef.get();
            if (msg.what == MESSAGE_CODE_MAIN) {
                // 如果消息类别是MESSAGE_CODE_MAIN的值，就执行以下逻辑
                // 得到消息内容
                String content = (String) msg.obj;
                // 设置TextView的文本为消息内容
                activity.tvContent.setText(content);
            }
        }

    }

    private TextView tvContent;
    private MainHandler mainHandler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvContent = findViewById(R.id.tv_content);
        // 创建MainHandler对象
        mainHandler = new MainHandler(this);
        // 创建Runnable对象
        runnable = new Runnable() {
            @Override
            public void run() {
                // 从消息池中取出消息
                Message message = mainHandler.obtainMessage();
                // 设置消息类别
                message.what = MESSAGE_CODE_MAIN;
                // 设置消息内容
                message.obj = "谭嘉俊";
                // 将该消息添加到消息队列的尾部
                mainHandler.sendMessage(message);
            }
        };
        // 创建线程，并且启动它
        new Thread(runnable).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 删除消息队列中Runnable的普通消息
        mainHandler.removeCallbacks(runnable);
    }

}