package com.huajie.readbook.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huajie.readbook.R;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class ReportGridViewAdapter extends BaseAdapter {

    private String [] list;
    private LayoutInflater layoutInflater;
    private Context context;
    private int selectorPosition;

    public ReportGridViewAdapter(Context context, String [] list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.report_gridview_item, null);
            holder = new ViewHolder();
            holder.tv_classify = convertView.findViewById(R.id.tv_classify);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_classify.setText(list[position]);

        if (selectorPosition == position){
            holder.tv_classify.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_classify.setBackground(context.getResources().getDrawable(R.drawable.background_corners_search_null));
        }else {
            holder.tv_classify.setTextColor(context.getResources().getColor(R.color.a2a9b2));
            holder.tv_classify.setBackground(context.getResources().getDrawable(R.drawable.background_corners_search));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_classify;
    }

    public void isCheck(int position){
        this.selectorPosition = position;
        notifyDataSetChanged();
    }

}
