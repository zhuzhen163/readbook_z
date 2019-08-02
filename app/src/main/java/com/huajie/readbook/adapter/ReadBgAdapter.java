package com.huajie.readbook.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.huajie.readbook.R;
import com.huajie.readbook.bean.ReadBgBean;

import java.util.List;

/**
 * Created by Liang_Lu on 2017/11/22.
 * 选择阅读背景
 */

public class ReadBgAdapter extends BaseQuickAdapter<ReadBgBean, BaseViewHolder> {

    List<ReadBgBean> data;
    public ReadBgAdapter(@Nullable List<ReadBgBean> data) {
        super(R.layout.item_read_bg, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReadBgBean item) {

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        ReadBgBean readBgBean = data.get(position);
        if (position == 0){
            holder.setBackgroundRes(R.id.read_bg_view,R.drawable.icon_yuan1);
        }else if (position == 1){
            holder.setBackgroundRes(R.id.read_bg_view,R.drawable.icon_yuan2);
        }else if (position == 2){
            holder.setBackgroundRes(R.id.read_bg_view,R.drawable.icon_yuan3);
        }else if (position == 3){
            holder.setBackgroundRes(R.id.read_bg_view,R.drawable.icon_yuan4);
        }else if (position == 4){
            holder.setBackgroundRes(R.id.read_bg_view,R.drawable.icon_yuan5);
        }else if (position == 5){
            holder.setBackgroundRes(R.id.read_bg_view,R.drawable.icon_yuan6);
        }
        holder.setVisible(R.id.read_bg_iv_checked, readBgBean.isSelect());
    }
}
