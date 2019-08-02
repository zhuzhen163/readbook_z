package com.huajie.readbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

import static com.huajie.readbook.base.BaseContent.ImageUrl;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class TwoHeadAdapter extends BaseAdapter {

    private List<BookBean> list;
    private LayoutInflater layoutInflater;
    private Context context;


    public TwoHeadAdapter(Context context, List <BookBean> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_head_adapter_2, null);
            holder = new ViewHolder();
            holder.iv_ph_bookImg1  = convertView.findViewById(R.id.iv_ph_bookImg1);
            holder.tv_no_1 = convertView.findViewById(R.id.tv_no_1);
            holder.tv_ph_bookName = convertView.findViewById(R.id.tv_ph_bookName);
            holder.tv_redu = convertView.findViewById(R.id.tv_redu);
            holder.tv_rd = convertView.findViewById(R.id.tv_rd);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BookBean bean = list.get(position);
        Glide.with(context).load(ImageUrl+bean.getLogo()).placeholder(R.drawable.icon_pic_def).into(holder.iv_ph_bookImg1);
        holder.tv_ph_bookName.setText(bean.getName());

        int heat = bean.getHeat();
        if (heat != 0){
            holder.tv_redu.setVisibility(View.VISIBLE);
            holder.tv_rd.setVisibility(View.VISIBLE);
            holder.tv_redu.setText(heat+"");
        }else {
            holder.tv_redu.setVisibility(View.GONE);
            holder.tv_rd.setVisibility(View.GONE);
        }
        if (position == 0){
            holder.tv_no_1.setText("NO.1");
            holder.tv_no_1.setBackgroundResource(R.drawable.icon_no_1);
        }else if (position ==1){
            holder.tv_no_1.setText("NO.2");
            holder.tv_no_1.setBackgroundResource(R.drawable.icon_no_2);
        }else if (position == 2){
            holder.tv_no_1.setText("NO.3");
            holder.tv_no_1.setBackgroundResource(R.drawable.icon_no_2);
        }else if (position == 3){
            holder.tv_no_1.setText("NO.4");
            holder.tv_no_1.setBackgroundResource(R.drawable.icon_no_4);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_ph_bookImg1;
        TextView tv_no_1;
        TextView tv_ph_bookName;
        TextView tv_redu;
        TextView tv_rd;
    }

    public void setList(List<BookBean> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
