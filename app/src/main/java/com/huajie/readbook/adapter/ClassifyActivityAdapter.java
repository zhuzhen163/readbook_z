package com.huajie.readbook.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.bean.ClassifyModel;
import com.huajie.readbook.bean.ClassifysModel;
import com.huajie.readbook.utils.StringUtils;




public class ClassifyActivityAdapter extends ListBaseAdapter<ClassifysModel> {

    private ImageView iv_Img;
    private TextView tv_name,tv_count;

    public ClassifyActivityAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_classify_adapter;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ClassifysModel classifysModel = mDataList.get(position);
        iv_Img = holder.getView(R.id.iv_Img);
        tv_name = holder.getView(R.id.tv_name);
        tv_count = holder.getView(R.id.tv_count);
        if (StringUtils.isNotBlank(classifysModel.getLogo())){
            Glide.with(mContext).load(classifysModel.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_Img);
        }else {
            Glide.with(mContext).load(R.drawable.icon_pic_def).into(iv_Img);
        }
        tv_name.setText(classifysModel.getName());
        tv_count.setText(classifysModel.getBookCount()+"册");
    }
}
