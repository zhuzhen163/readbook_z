package com.huajie.readbook.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.bean.ClassifyModel;
import com.huajie.readbook.bean.ClassifysModel;
import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class ClassifyGridViewAdapter extends BaseAdapter {

    private List<ClassifysModel> list;
    private LayoutInflater layoutInflater;
    private Context context;

    public ClassifyGridViewAdapter(Context context, List <ClassifysModel> list) {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.view_classify_gridview_item, null);
            holder = new ViewHolder();
            holder.tv_classify = convertView.findViewById(R.id.tv_classify);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ClassifysModel model = list.get(position);
        holder.tv_classify.setText(model.getName());
        if (model.isCheck()){
            holder.tv_classify.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_classify.setBackground(context.getResources().getDrawable(R.drawable.background_corners_search_null));
        }else {
            holder.tv_classify.setTextColor(context.getResources().getColor(R.color.text_33));
            holder.tv_classify.setBackground(context.getResources().getDrawable(R.drawable.background_corners_search_white));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_classify;
    }

}
