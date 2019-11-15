package com.huajie.readbook.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.bean.MessageNoticeModel;
import com.huajie.readbook.utils.SwitchActivityManager;

import java.util.Map;


public class MessageNoticeAdapter extends ListBaseAdapter<MessageNoticeModel.MessageNotice> {

    Context context;
    private TextView tv_time,tv_message;

    private Map<String, String> data;

    public MessageNoticeAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_message_notice;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        tv_time = holder.getView(R.id.tv_time);
        tv_message = holder.getView(R.id.tv_message);

        MessageNoticeModel.MessageNotice model = mDataList.get(position);
        tv_time.setText(model.getCreateTime());

        String type = data.get(model.getMsgType());
        String message = "<font color=\"#323232\">【"+type+"】</font>"+model.getNotice();

        tv_message.setText(Html.fromHtml(message));

    }

    public void setType(Map<String, String> data){
        this.data = data;
    }

}
