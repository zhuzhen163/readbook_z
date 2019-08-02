package com.huajie.readbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.activity.ReadHistoryActivity;
import com.huajie.readbook.bean.BookshelfBean;
import com.huajie.readbook.bean.ReadHistoryModel;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.db.entity.BookRecordBean;
import com.huajie.readbook.db.helper.BookRecordHelper;
import com.huajie.readbook.utils.SwitchActivityManager;

import static com.huajie.readbook.base.BaseContent.ImageUrl;


/**
 * 阅读历史
 * viewpager
 */

public class ReadHistoryAdapter extends ListBaseAdapter<BookBean> {

    private ImageView iv_bookImg;
    private TextView tv_bookName,tv_readPercent,tv_readTime,tv_openBook;
    private CheckBox checkbox_list;
    public boolean isDelete = false;
    //存储阅读记录类
    private BookRecordBean mBookRecord;
    private ReadHistoryActivity activity;

    public ReadHistoryAdapter(Context context, ReadHistoryActivity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_read_history;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        iv_bookImg = holder.getView(R.id.iv_bookImg);
        tv_bookName = holder.getView(R.id.tv_bookName);
        tv_readPercent = holder.getView(R.id.tv_readPercent);
        tv_readTime = holder.getView(R.id.tv_readTime);
        tv_openBook = holder.getView(R.id.tv_openBook);
        checkbox_list = holder.getView(R.id.checkbox_list);
        BookBean historyModel = mDataList.get(position);

        Glide.with(mContext).load(ImageUrl+historyModel.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg);
        tv_bookName.setText(historyModel.getName());
        //从数据库取阅读数据
        mBookRecord = BookRecordHelper.getsInstance().findBookRecordById(historyModel.getId());
        if (mBookRecord != null){
            String chapterPercent = mBookRecord.getChapterPercent();
            if (!chapterPercent.equals("0.00")){
                tv_readPercent.setText("已读"+chapterPercent+"%");
                tv_readPercent.setTextColor(Color.parseColor("#5297f7"));
            }else {
                tv_readPercent.setText("已读"+"0.01%");
                tv_readPercent.setTextColor(Color.parseColor("#5297f7"));
            }
        }else {
            tv_readPercent.setText("已读"+historyModel.getProgressbar()+"%");
        }
        tv_readTime.setText(historyModel.getUpdateTime());
        boolean delete = historyModel.isDelete();
        if (delete){
            checkbox_list.setChecked(true);
        }else {
            checkbox_list.setChecked(false);
        }

        if (isDelete){//长按删除
            checkbox_list.setVisibility(View.VISIBLE);
            tv_openBook.setVisibility(View.GONE);
        }else {
            checkbox_list.setVisibility(View.GONE);
            tv_openBook.setVisibility(View.VISIBLE);
        }

        checkbox_list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //判断是不是点击触发的，否则当我setChecked()时会触发此listener
                if(!buttonView.isPressed()) return;
                if (position < mDataList.size()){
                    BookBean historyModel = mDataList.get(position);
                    if (isChecked){
                        historyModel.setDelete(true);
                    }else {
                        historyModel.setDelete(false);
                    }
                }
                activity.setDeleteNum(deleteNum());
            }
        });

        tv_openBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookBean readHistory = mDataList.get(position);
                SwitchActivityManager.startReadActivity(mContext,readHistory.getCollBookBean(),readHistory.getIsJoin()==0?true:false);
            }
        });
    }

    //删除数量
    public int deleteNum(){
        int deleteNum = 0;
        for (int i = 0; i <mDataList.size() ; i++) {
            BookBean bookBean = mDataList.get(i);
            if (bookBean.isDelete()){
                deleteNum++;
            }
        }
        return deleteNum;
    }

    public void setDelete(boolean isDelete){
        this.isDelete = isDelete;
        notifyDataSetChanged();
    }
}
