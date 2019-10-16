package com.huajie.readbook.adapter;

import android.content.Context;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.bean.MessageNoticeModel;


public class MessageNoticeAdapter extends ListBaseAdapter<MessageNoticeModel> {

    Context context;
    private TextView tv_time,tv_message;

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

        MessageNoticeModel model = mDataList.get(position);
        tv_time.setText(model.getCreateTime());
        tv_message.setText(model.getNotice());

    }

}
