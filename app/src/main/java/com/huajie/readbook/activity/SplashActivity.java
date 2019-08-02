package com.huajie.readbook.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.huajie.readbook.R;
import com.huajie.readbook.ZApplication;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.Constant;
import com.huajie.readbook.utils.SwitchActivityManager;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class SplashActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //app状态改为正常
                ZApplication.flag=0;
                if (!ConfigUtils.getChooseGender()){
                    SwitchActivityManager.startChooseGenderActivity(SplashActivity.this);
                }else {
                    SwitchActivityManager.startMainActivity(SplashActivity.this);
                }

                finish();
            }
        },1000);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
         }
         return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }
}
