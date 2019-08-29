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
import com.huajie.readbook.utils.StringUtils;

import java.util.List;

import static com.huajie.readbook.base.BaseContent.ImageUrl;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class ThreeHeadAdapter extends BaseAdapter {

    private List<BookBean> list;
    private LayoutInflater layoutInflater;
    private Context context;

    public ThreeHeadAdapter(Context context,List <BookBean> list) {
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
            convertView = layoutInflater.inflate(R.layout.view_head_adapter_3, null);
            holder = new ViewHolder();
            holder.iv_bookImg = convertView.findViewById(R.id.iv_bookImg);
            holder.tv_bookContent = convertView.findViewById(R.id.tv_bookContent);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BookBean bookBean = list.get(position);
        if (StringUtils.isNotBlank(bookBean.getLogo())){
            Glide.with(context).load(ImageUrl+bookBean.getLogo()).placeholder(R.drawable.icon_pic_def).into(holder.iv_bookImg);
        }else {
            Glide.with(context).load(R.drawable.icon_pic_def).into(holder.iv_bookImg);
        }
        holder.tv_bookContent.setText(bookBean.getName());
        return convertView;
    }

    class ViewHolder {
        ImageView iv_bookImg;
        TextView tv_bookContent;
    }

    public void setList(List<BookBean> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
