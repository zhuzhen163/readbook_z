package com.huajie.readbook.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.utils.GlideRectRound;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.tendcloud.tenddata.TCAgent;

import static com.huajie.readbook.base.BaseContent.ImageUrl;


public class FindFragmentAdapter extends ListBaseAdapter<BookBean> {

    Context context;
    private ImageView civ_pic;
    private TextView tv_title,tv_des,tv_more;
    private LinearLayout ll_readHistory;
    private boolean isMore = false;
    private boolean isCollected = false; //是否加入书架

    public FindFragmentAdapter(Context context) {
        super(context);
        this.context = context;
    }
    private AddBookListener addBookListener;

    public interface AddBookListener{
        void addBook();
    }

    public void setAddBookListener(AddBookListener addBookListener) {
        this.addBookListener = addBookListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_find_fragment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        BookBean bookBean = mDataList.get(position);
        civ_pic = holder.getView(R.id.civ_pic);
        tv_title = holder.getView(R.id.tv_title);
        tv_des = holder.getView(R.id.tv_des);
        tv_more = holder.getView(R.id.tv_more);
        ll_readHistory = holder.getView(R.id.ll_readHistory);


        tv_title.setText(bookBean.getAlias());
        tv_des.setText(bookBean.getDetails());
        if (StringUtils.isNotBlank(bookBean.getImage())){
            Glide.with(mContext)
                    .load(ImageUrl+bookBean.getImage())
//                    .transform(new GlideRectRound(mContext,10))
                    .placeholder(R.drawable.icon_find_def)
                    .into(civ_pic);
        }else {
            Glide.with(mContext)
                    .load(R.drawable.icon_find_def)
//                    .transform(new GlideRectRound(mContext,10))
                    .into(civ_pic);
        }

        if (position == mDataList.size()-1 && isMore){
            tv_more.setVisibility(View.VISIBLE);
        }else {
            tv_more.setVisibility(View.GONE);
        }
        ll_readHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCAgent.onEvent(mContext, "发现_阅读全文_<"+bookBean.getName()+">:"+bookBean.getAuthorName());
                CollBookBean collBookBean = bookBean.getCollBookBean();
                CollBookBean bookById = CollBookHelper.getsInstance().findBookById(bookBean.getId());
                if (bookById != null){
                    isCollected = true;
                }else {
                    isCollected = false;
                }
                SwitchActivityManager.startReadActivity(mContext,collBookBean,isCollected);
            }
        });
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addBookListener != null){
                    addBookListener.addBook();
                }
            }
        });
    }

    public void setMore(boolean isMore){
        this.isMore = isMore;
    }

}
