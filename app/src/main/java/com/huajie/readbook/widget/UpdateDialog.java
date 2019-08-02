package com.huajie.readbook.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huajie.readbook.R;

/**
 * 升级
 */
public class UpdateDialog extends Dialog {
    private UpdateCallBack callBack;
    TextView tv_updateContent,tv_update;
    ImageView iv_cancel;

    public UpdateDialog(Context context) {
        super(context, R.style.custom_dialog);
    }

    public interface  UpdateCallBack{
        void update();
    }

    public void setUpdateCallBack(UpdateCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        initView();
    }

    public void initView() {
        setCancelable(false);
        tv_updateContent = findViewById(R.id.tv_updateContent);
        tv_update = findViewById(R.id.tv_update);
        iv_cancel = findViewById(R.id.iv_cancel);

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null){
                    dismiss();
                    callBack.update();
                }
            }
        });

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setContent(String content,int isforce){
        this.show();
        tv_updateContent.setText(content);
        if (0 == isforce){
            iv_cancel.setVisibility(View.GONE);
        }else {
            iv_cancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
