package com.huajie.readbook.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huajie.readbook.R;

/**
 * 加入书架dialog
 */
public class AddBookShelfDialog extends Dialog {
    private Button btn_cancel,btn_ok;
    private DoWhatCallBack callBack;

    public AddBookShelfDialog(Context context) {
        super(context, R.style.custom_dialog);
    }

    public interface  DoWhatCallBack{
        void addBookShelf();
        void exitReadActivity();
    }

    public void setDoWhatCallBack(DoWhatCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_book_shelf);
        initView();
    }

    public void initView() {
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        setCanceledOnTouchOutside(false);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.addBookShelf();
                dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callBack.exitReadActivity();
            }
        });
    }
}
