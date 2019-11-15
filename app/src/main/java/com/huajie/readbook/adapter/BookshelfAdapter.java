package com.huajie.readbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.StaticLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.huajie.readbook.R;
import com.huajie.readbook.bean.BookshelfBean;
import com.huajie.readbook.db.entity.BookRecordBean;
import com.huajie.readbook.db.helper.BookRecordHelper;
import com.huajie.readbook.fragment.BookshelfFragment;
import com.huajie.readbook.utils.RelativeDateFormat;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *描述：书架适配器
 *作者：Created by zhuzhen
 */
public class BookshelfAdapter extends ListBaseAdapter<BookshelfBean> {

    BookshelfFragment activity;
    private RelativeLayout rl_item_book,rl_add_book,rl_item_book_grid,rl_ad;
    private ImageView iv_add_grid,iv_bookImg,iv_bookImg_grid,iv_bookImg_ad;
    private TextView tv_bookName,tv_bookName_grid,tv_time_list,tv_percent,tv_percent_list,tv_progress_list,tv_progress_grid,tv_bookName_ad,tv_message_ad;
    private CheckBox checkbox_list,checkbox_grid;
    LinearLayout card_view;
    public boolean isDelete = false;
    private List<TTFeedAd> mData;

    //存储阅读记录类
    private BookRecordBean mBookRecord;

    private OnRemoveListener onremoveListener;
    private OnClickItemListener onClickItemListener;
    private AddBookListener addBookListener;
    public interface OnRemoveListener{
        void  onDelete(int i);
    }

    public interface OnClickItemListener{
        void onItem(int position);
    }

    public interface AddBookListener{
        void addBook();
    }

    public void setAddBookListener(AddBookListener addBookListener) {
        this.addBookListener = addBookListener;
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public void setOnRemoveListener(OnRemoveListener onremoveListener) {
        this.onremoveListener = onremoveListener;
    }

    public BookshelfAdapter(Context context , BookshelfFragment activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    public int getLayoutId() {
        if (activity.layout == 0){
            return R.layout.bookshelf_list;
        }else {
            return R.layout.bookshelf_gride;
        }
    }

    public void setAd(List<TTFeedAd> mData){
        this.mData = mData;
    }
    TTFeedAd ttFeedAd = null;

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {


        BookshelfBean item = null;
        if (activity.layout == 0){//列表模式
            rl_item_book = holder.getView(R.id.rl_item_book);
            rl_add_book = holder.getView(R.id.rl_add_book);
            tv_bookName = holder.getView(R.id.tv_bookName);
            tv_time_list = holder.getView(R.id.tv_time_list);
            checkbox_list = holder.getView(R.id.checkbox_list);
            iv_bookImg = holder.getView(R.id.iv_bookImg);
            tv_progress_list = holder.getView(R.id.tv_progress_list);
            tv_percent_list = holder.getView(R.id.tv_percent_list);
            rl_ad = holder.getView(R.id.rl_ad);
            iv_bookImg_ad = holder.getView(R.id.iv_bookImg_ad);
            tv_bookName_ad = holder.getView(R.id.tv_bookName_ad);
            tv_message_ad = holder.getView(R.id.tv_message_ad);
            card_view = holder.getView(R.id.card_view);

            //展示正常数据
            if (position < mDataList.size()) {
                try {
                    if (mData != null){
                        if (position == 0||position == 2){
                            rl_ad.setVisibility(View.VISIBLE);
                            if (position == 0){
                                ttFeedAd = mData.get(0);
                            }else {
                                if (mData.size()>1){
                                    ttFeedAd = mData.get(1);
                                }
                            }
                            if (ttFeedAd != null){
                                if (ttFeedAd.getImageList() != null && !ttFeedAd.getImageList().isEmpty()) {
                                    TTImage image = ttFeedAd.getImageList().get(0);
                                    if (image != null && image.isValid()) {
                                        Glide.with(activity).load(image.getImageUrl()).into(iv_bookImg_ad);
                                    }
                                    tv_bookName_ad.setText(ttFeedAd.getTitle());
                                    tv_message_ad.setText(ttFeedAd.getDescription());
                                }
                            }
                        }else {
                            rl_ad.setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                item = mDataList.get(position);
                tv_bookName.setText(item.getName());
                if (item.isImportLocal()){
                    Glide.with(activity).load(R.drawable.icon_local).into(iv_bookImg);
                }else {
                    if (StringUtils.isNotBlank(item.getLogo())){
                        Glide.with(activity).load(item.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg);
                    }else {
                        Glide.with(activity).load(R.drawable.icon_pic_def).into(iv_bookImg);
                    }
                }
                boolean delete = item.isDelete();
                if (delete){
                    checkbox_list.setChecked(true);
                }else {
                    checkbox_list.setChecked(false);
                }
                tv_progress_list.setVisibility(View.GONE);
                if (1 == item.getProgress()){
                    boolean time = timeCompare(item.getLastRead(), item.getUpdateTime());
                    if (time){
                        tv_progress_list.setVisibility(View.VISIBLE);
                        tv_progress_list.setText("更新");
                    }else {
                        tv_progress_list.setVisibility(View.GONE);
                    }

                    String updateTime = item.getUpdateTime();
                    tv_time_list.setText(RelativeDateFormat.format(updateTime));
                }else {
                    tv_time_list.setText("已完结");
                }

                rl_item_book.setVisibility(View.VISIBLE);
                rl_add_book.setVisibility(View.GONE);

                tv_percent_list.setText("未读过");
                tv_percent_list.setTextColor(Color.parseColor("#a2a9b2"));
                //从数据库取阅读数据
                mBookRecord = BookRecordHelper.getsInstance().findBookRecordById(item.getBookId());
                if (mBookRecord != null){
                    String chapterPercent = mBookRecord.getChapterPercent();
                    if (!chapterPercent.equals("0.00")){
                        if (chapterPercent.equals("100.00")){
                            tv_percent_list.setText("100%");
                        }else {
                            tv_percent_list.setText(chapterPercent+"%");
                        }
                        tv_percent_list.setTextColor(Color.parseColor("#5297f7"));
                        if (1 == item.getProgress()){
                            boolean time = timeCompare(item.getLastRead(), item.getUpdateTime());
                            if (time){
                                tv_progress_list.setVisibility(View.VISIBLE);
                                tv_progress_list.setText("更新");
                            }else {
                                tv_progress_list.setVisibility(View.GONE);
                            }
                            String updateTime = item.getUpdateTime();
                            tv_time_list.setText(RelativeDateFormat.format(updateTime));
                        }else {
                            tv_time_list.setText("已完结");
                        }
                    }else {
                        tv_percent_list.setText("0.01%");
                        tv_percent_list.setTextColor(Color.parseColor("#5297f7"));
                    }
                }

            }else {//展示添加数据
                rl_ad.setVisibility(View.GONE);
                rl_item_book.setVisibility(View.GONE);
                if (isDelete){//如果删除添加数据需要隐藏
                    rl_add_book.setVisibility(View.INVISIBLE);
                }else {
                    rl_add_book.setVisibility(View.VISIBLE);
                }
            }

            if (isDelete){//长按删除
                checkbox_list.setVisibility(View.VISIBLE);
                tv_time_list.setVisibility(View.GONE);
            }else {
                checkbox_list.setVisibility(View.GONE);
                tv_time_list.setVisibility(View.VISIBLE);
            }

            rl_add_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addBookListener != null){
                        addBookListener.addBook();
                    }
                }
            });

            rl_item_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickItemListener != null && !isDelete){
                        onClickItemListener.onItem(position);
                    }
                }
            });

            try {
                if (position == 0 ||position == 2 && ttFeedAd != null){
                    ttFeedAd.registerViewForInteraction((ViewGroup) card_view, holder.getView(R.id.rl_ad), new TTNativeAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, TTNativeAd ad) {
                            if (ad != null) {
//                            ToastUtil.showToast( "广告" + ad.getTitle() + "被点击");
                            }
                        }

                        @Override
                        public void onAdCreativeClick(View view, TTNativeAd ad) {
                            if (ad != null) {
//                                ToastUtil.showToast("广告" + ad.getTitle() + "被创意按钮被点击");
                            }
                        }

                        @Override
                        public void onAdShow(TTNativeAd ad) {
                            if (ad != null) {
//                                ToastUtil.showToast("广告" + ad.getTitle() + "展示");
                            }
                        }

                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            checkbox_list.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //判断是不是点击触发的，否则当我setChecked()时会触发此listener
                    if(!buttonView.isPressed()) return;
                    if (position < mDataList.size()){
                        final BookshelfBean bookshelfBean = mDataList.get(position);
                        if (isChecked){
                            bookshelfBean.setDelete(true);
                        }else {
                            bookshelfBean.setDelete(false);
                        }
                    }
                    activity.setDeleteNum(deleteNum());
                }
            });

            rl_item_book.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onremoveListener!=null){
                        onremoveListener.onDelete(position);
                        BookshelfBean bookshelfBean = mDataList.get(position);
                        bookshelfBean.setDelete(true);
                        checkbox_list.setChecked(true);
                        activity.setDeleteNum(1);
                    }
                    return true;
                }
            });

        }else if (activity.layout == 1){//封面模式
            rl_item_book_grid = holder.getView(R.id.rl_item_book_grid);
            iv_add_grid = holder.getView(R.id.iv_add_grid);
            checkbox_grid = holder.getView(R.id.checkbox_grid);
            tv_bookName_grid = holder.getView(R.id.tv_bookName_grid);
            iv_bookImg_grid = holder.getView(R.id.iv_bookImg_grid);
            tv_percent = holder.getView(R.id.tv_percent);
            tv_progress_grid = holder.getView(R.id.tv_progress_grid);
            if (position < mDataList.size()) {//展示正常数据
                item = mDataList.get(position);
                tv_bookName_grid.setText(item.getName());
                if (item.isImportLocal()){
                    Glide.with(activity).load(R.drawable.icon_local).into(iv_bookImg_grid);
                }else {
                    if (StringUtils.isNotBlank(item.getLogo())){
                        Glide.with(activity).load(item.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg_grid);
                    }else {
                        Glide.with(activity).load(R.drawable.icon_pic_def).into(iv_bookImg_grid);
                    }
                }
                boolean delete = item.isDelete();
                if (delete){
                    checkbox_grid.setChecked(true);
                }else {
                    checkbox_grid.setChecked(false);
                }
                tv_progress_grid.setVisibility(View.GONE);
                if (1 == item.getProgress()){
                    boolean time = timeCompare(item.getLastRead(), item.getUpdateTime());
                    if (time){
                        tv_progress_grid.setVisibility(View.VISIBLE);
                        tv_progress_grid.setText("更新");
                    }else {
                        tv_progress_grid.setVisibility(View.GONE);
                    }
                }
                rl_item_book_grid.setVisibility(View.VISIBLE);
                iv_add_grid.setVisibility(View.GONE);

                tv_percent.setText("未读过");
                tv_percent.setTextColor(Color.parseColor("#a2a9b2"));
                //从数据库取阅读数据
                mBookRecord = BookRecordHelper.getsInstance().findBookRecordById(item.getBookId());
                if (mBookRecord != null){
                    String chapterPercent = mBookRecord.getChapterPercent();
                    if (!chapterPercent.equals("0.00")){
                        if (chapterPercent.equals("100.00")){
                            tv_percent.setText("已读100%");
                        }else {
                            tv_percent.setText("已读"+chapterPercent+"%");
                        }
                        tv_percent.setTextColor(Color.parseColor("#5297f7"));
                        if (1 == item.getProgress()){
                            boolean time = timeCompare(item.getLastRead(), item.getUpdateTime());
                            if (time){
                                tv_progress_grid.setVisibility(View.VISIBLE);
                                tv_progress_grid.setText("更新");
                            }else {
                                tv_progress_grid.setVisibility(View.GONE);
                            }
                        }
                    }else {
                        tv_percent.setText("已读"+"0.01%");
                        tv_percent.setTextColor(Color.parseColor("#5297f7"));
                    }
                }

            } else {//展示添加数据
                rl_item_book_grid.setVisibility(View.GONE);
                if (isDelete){//如果删除添加数据需要隐藏
                    iv_add_grid.setVisibility(View.INVISIBLE);
                }else {
                    iv_add_grid.setVisibility(View.VISIBLE);
                }
            }

            if (isDelete){//长按删除
                checkbox_grid.setVisibility(View.VISIBLE);
            }else {
                checkbox_grid.setVisibility(View.GONE);
            }

            checkbox_grid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //判断是不是点击触发的，否则当我setChecked()时会触发此listener
                    if(!buttonView.isPressed()) return;
                    if (position < mDataList.size()){
                        final BookshelfBean bookshelfBean = mDataList.get(position);
                        if (isChecked){
                            bookshelfBean.setDelete(true);
                        }else {
                            bookshelfBean.setDelete(false);
                        }
                    }
                    activity.setDeleteNum(deleteNum());
                }
            });

            iv_add_grid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addBookListener != null){
                        addBookListener.addBook();
                    }
                }
            });
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onremoveListener!=null){
                    onremoveListener.onDelete(position);
                    BookshelfBean bookshelfBean = mDataList.get(position);
                    bookshelfBean.setDelete(true);
                    checkbox_grid.setChecked(true);
                    activity.setDeleteNum(1);
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItemListener != null && !isDelete){
                    onClickItemListener.onItem(position);
                }
            }
        });

    }

    //删除数量
    public int deleteNum(){
        int deleteNum = 0;
        for (int i = 0; i <mDataList.size() ; i++) {
            BookshelfBean bookshelfBean = mDataList.get(i);
            if (bookshelfBean.isDelete()){
                deleteNum++;
            }
        }
        return deleteNum;
    }

    public void setDelete(boolean isDelete){
        this.isDelete = isDelete;
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() == 0){
            return mDataList.size();
        }else {
            return mDataList.size()+1;
        }
    }

    public boolean timeCompare(String lastTime,String updateTime) {
        try {
            if (StringUtils.isNotBlank(lastTime) && StringUtils.isNotBlank(updateTime)){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date sd1 = df.parse(lastTime);
                Date sd2 = df.parse(updateTime);
                return sd1.before(sd2);
            }else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
