package com.huajie.readbook.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.huajie.readbook.R;

/**
 * 删除书架
 */
public class DeleteBookShelfDialog extends Dialog {
    private Button btn_cancel,btn_ok;
    private DoWhatCallBack callBack;

    public DeleteBookShelfDialog(Context context) {
        super(context, R.style.custom_dialog);
    }

    public interface  DoWhatCallBack{
        void deleteSubmit();
    }

    public void setDoWhatCallBack(DoWhatCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_bookshelf);
        initView();
    }

    public void initView() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        setCanceledOnTouchOutside(false);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.deleteSubmit();
                dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
