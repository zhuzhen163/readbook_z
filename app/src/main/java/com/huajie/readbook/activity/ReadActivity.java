package com.huajie.readbook.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.huajie.readbook.R;
import com.huajie.readbook.ZApplication;
import com.huajie.readbook.adapter.BookMarkAdapter;
import com.huajie.readbook.adapter.CategoryAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.db.entity.BookChapterBean;
import com.huajie.readbook.db.entity.BookChaptersBean;
import com.huajie.readbook.db.entity.BookMarkBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.BookChapterHelper;
import com.huajie.readbook.db.helper.BookMarkHelpter;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.presenter.ReadActivityPresenter;
import com.huajie.readbook.utils.BrightnessUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.Constant;
import com.huajie.readbook.utils.ImageveiwUitls;
import com.huajie.readbook.utils.RxUtils;
import com.huajie.readbook.utils.ScreenUtils;
import com.huajie.readbook.utils.ShareUtils;
import com.huajie.readbook.utils.StatusBarUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.ReadActivityView;
import com.huajie.readbook.widget.AddBookShelfDialog;
import com.huajie.readbook.widget.CircleProgressBar;
import com.huajie.readbook.widget.ReadLightDialog;
import com.huajie.readbook.widget.ReadPopWindow;
import com.huajie.readbook.widget.ReadSettingDialog;
import com.huajie.readbook.widget.ReadSettingManager;
import com.huajie.readbook.widget.ShareBookDialog;
import com.huajie.readbook.widget.page.NetPageLoader;
import com.huajie.readbook.widget.page.PageLoader;
import com.huajie.readbook.widget.page.PageView;
import com.huajie.readbook.widget.page.TxtChapter;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static android.support.v4.view.ViewCompat.LAYER_TYPE_SOFTWARE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.huajie.readbook.base.BaseContent.ImageUrl;

@SuppressLint("WrongConstant")
public class ReadActivity extends BaseActivity<ReadActivityPresenter> implements ReadActivityView , AddBookShelfDialog.DoWhatCallBack,ReadPopWindow.PopWindowInterface,ShareBookDialog.ShareBookInterface {
    @BindView(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @BindView(R.id.read_abl_top_menu)
    AppBarLayout mReadAblTopMenu;
    @BindView(R.id.pv_read_page)
    PageView mPvReadPage;
//    @BindView(R.id.read_tv_page_tip)
//    TextView mReadTvPageTip;
    @BindView(R.id.read_tv_pre_chapter)
    TextView mReadTvPreChapter;
    @BindView(R.id.read_sb_chapter_progress)
    SeekBar mReadSbChapterProgress;
    @BindView(R.id.read_tv_next_chapter)
    TextView mReadTvNextChapter;
    @BindView(R.id.read_tv_category)
    TextView mReadTvCategory;
    @BindView(R.id.read_tv_night_mode)
    TextView mReadTvNightMode;
    @BindView(R.id.read_tv_setting)
    TextView mReadTvSetting;
    @BindView(R.id.read_ll_bottom_menu)
    LinearLayout mReadLlBottomMenu;
    @BindView(R.id.read_iv_category)
    ListView mLvCategory;
    @BindView(R.id.read_dl_slide)
    DrawerLayout mReadDlSlide;
    @BindView(R.id.tv_menu)
    ImageView tv_menu;
    @BindView(R.id.tv_catalog)
    TextView tv_catalog;
    @BindView(R.id.view_catalog)
    View view_catalog;
    @BindView(R.id.ll_catalog)
    LinearLayout ll_catalog;
    @BindView(R.id.ll_bookMark)
    LinearLayout ll_bookMark;
    @BindView(R.id.tv_bookMark)
    TextView tv_bookMark;
    @BindView(R.id.view_bookMark)
    View view_bookMark;
    @BindView(R.id.rl_catalog)
    RelativeLayout rl_catalog;
    @BindView(R.id.rl_bookMark)
    RelativeLayout rl_bookMark;
    @BindView(R.id.lv_bookMark)
    ListView lv_bookMark;
    @BindView(R.id.tv_addMark)
    TextView tv_addMark;
    @BindView(R.id.iv_night_mode)
    ImageView iv_night_mode;
    @BindView(R.id.tv_catalogNum)
    TextView tv_catalogNum;
    @BindView(R.id.rl_first)
    RelativeLayout rl_first;
    @BindView(R.id.tv_first)
    TextView tv_first;
    @BindView(R.id.progressBar)
    CircleProgressBar progressBar;
    @BindView(R.id.tv_addGold)
    TextView tv_addGold;

    ImageveiwUitls iv_cover_img;
    TextView tv_cover_name;
    TextView tv_cover_auth;

    private CategoryAdapter mCategoryAdapter;//目录
    private BookMarkAdapter bookMarkAdapter;//书签

    private static final String TAG = "ReadActivity";
    //注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI =
            Settings.System.getUriFor("screen_auto_brightness_adj");

    public static final String EXTRA_COLL_BOOK = "extra_coll_book";
    public static final String EXTRA_IS_COLLECTED = "extra_is_collected";

    private boolean isRegistered = false;

    /*****************view******************/
    private AddBookShelfDialog addBookShelfDialog;//添加书架
    private ReadSettingDialog mSettingDialog;//设置
    private ReadLightDialog readLightDialog;//亮度
    private ShareBookDialog shareDialog;//分享
    private ReadPopWindow readPopWindow;//右上角
    private PageLoader mPageLoader;
    private Animation mTopInAnim;
    private Animation mTopOutAnim;
    private Animation mBottomInAnim;
    private Animation mBottomOutAnim;
    private Animation mRightInAnim;
    private Animation mRightOutAnim;
    //    private CategoryAdapter mCategoryAdapter;
    private CollBookBean mCollBook;
    //控制屏幕常亮
    private PowerManager.WakeLock mWakeLock;

    /***************params*****************/
    private boolean isCollected = false; //是否加入书架
    private boolean isNightMode = false;
    private boolean firstRead = false;//第一次阅读书架书籍
    private boolean mReceiverTag = false;//解决销毁广播报错
    private boolean importLocal = false; //是否本地导入
    private String mBookId;
    private String shareUrl;
    private String pagePosContent;//当前页第一行文字
    List<BookChapterBean> bookChapterList = new ArrayList<>();

    private static final int WHAT_CATEGORY = 1;
    private static final int WHAT_CHAPTER = 2;
    private static final int WHAT_CLOSE_CHAPTER = 3;
    private static final int WHAT_OPEN_BOOKMARK = 4;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case WHAT_CATEGORY:
                    mLvCategory.setSelection(mPageLoader.getChapterPos());
                    break;
                case WHAT_CHAPTER:
                    if (mPageLoader == null) return;
                    mPageLoader.openChapter();
                    break;
                case WHAT_CLOSE_CHAPTER:
                    mReadDlSlide.closeDrawer(Gravity.START);
                    break;
                case WHAT_OPEN_BOOKMARK:
                    mPageLoader.openBookMarkPosition();
                    break;
            }
        }
    };

    // 接收电池信息和时间更新的广播
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int level = intent.getIntExtra("level", 0);
                mPageLoader.updateBattery(level);
            }
            //监听分钟的变化
            else if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                mPageLoader.updateTime();
            }
        }
    };


    //亮度调节监听
    //由于亮度调节没有 Broadcast 而是直接修改 ContentProvider 的。所以需要创建一个 Observer 来监听 ContentProvider 的变化情况。
    private ContentObserver mBrightObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);

            //判断当前是否跟随屏幕亮度，如果不是则返回
            if (readLightDialog == null) return;
            if (selfChange || !readLightDialog.isBrightFollowSystem()) return;

            //如果系统亮度改变，则修改当前 Activity 亮度
                if (BRIGHTNESS_MODE_URI.equals(uri)) {
                    Log.d(TAG, "亮度模式改变");
                } else if (BRIGHTNESS_URI.equals(uri) && !BrightnessUtils.isAutoBrightness(ReadActivity.this)) {
                Log.d(TAG, "亮度模式为手动模式 值改变");
                BrightnessUtils.setBrightness(ReadActivity.this, BrightnessUtils.getScreenBrightness(ReadActivity.this));
            } else if (BRIGHTNESS_ADJ_URI.equals(uri) && BrightnessUtils.isAutoBrightness(ReadActivity.this)) {
                Log.d(TAG, "亮度模式为自动模式 值改变");
                BrightnessUtils.setBrightness(ReadActivity.this, BrightnessUtils.getScreenBrightness(ReadActivity.this));
            } else {
                Log.d(TAG, "亮度调整 其他");
            }
        }
    };

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void initView() {
        // 如果 API < 18 取消硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPvReadPage.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mCollBook = (CollBookBean) getIntent().getSerializableExtra(EXTRA_COLL_BOOK);
        isCollected = getIntent().getBooleanExtra(EXTRA_IS_COLLECTED, false);
        isNightMode = ReadSettingManager.getInstance().isNightMode();
        mBookId = mCollBook.getBookId();
        importLocal = mCollBook.getImportLocal();
        if (mBookId == null){
            SwitchActivityManager.exitActivity(ReadActivity.this);
        }

        first();

        mTvToolbarTitle.setText(mCollBook.getName());
        StatusBarUtils.transparencyBar(this);
//        StatusBarUtil.setColor(this,Color.BLACK);
//        notchPhone();
        //获取页面加载器

        mPageLoader = mPvReadPage.getPageLoader(importLocal);
        mReadDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //更多设置dialog
        mSettingDialog = new ReadSettingDialog(this, mPageLoader);

        //亮度
        readLightDialog = new ReadLightDialog(this);

        //加入书架dialog
        addBookShelfDialog = new AddBookShelfDialog(this);
        addBookShelfDialog.setDoWhatCallBack(this);

        readPopWindow = new ReadPopWindow(mContext);
        readPopWindow.setPopWindowInterface(this);

        //分享
        shareDialog = new ShareBookDialog(mContext);
        shareDialog.setShareBookInterface(this);

        setCategory();

        toggleNightMode();

        //注册广播
        if (!mReceiverTag){
            mReceiverTag = true;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            registerReceiver(mReceiver, intentFilter);
        }

        //设置当前Activity的Brightness
        if (ReadSettingManager.getInstance().isBrightnessAuto()) {
            BrightnessUtils.setBrightness(this, BrightnessUtils.getScreenBrightness(this));
        } else {
            BrightnessUtils.setBrightness(this, ReadSettingManager.getInstance().getBrightness());
        }

        //初始化屏幕常亮类
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "keep bright");
        //隐藏StatusBar
        mPvReadPage.post(
                () -> hideSystemBar()
        );

        //初始化TopMenu
        initTopMenu();

        //初始化BottomMenu
        initBottomMenu();

        TCAgent.onPageStart(mContext, "阅读器");
        TCAgent.onEvent(mContext,"阅读器");

        MobclickAgent.onEvent(mContext, "reader_vc", "阅读器");

        MobclickAgent.onPageStart("阅读器");

//        View coverPageView = LayoutInflater.from(this).inflate(R.layout.layout_cover_view, null, false);
//        tv_cover_name = coverPageView.findViewById(R.id.tv_cover_name);
//        tv_cover_auth = coverPageView.findViewById(R.id.tv_cover_auth);
//        iv_cover_img = coverPageView.findViewById(R.id.iv_cover_img);
//        tv_cover_name.setText(mCollBook.getName());
//        tv_cover_auth.setText(mCollBook.getAuthor());
//        iv_cover_img.setImageURL(ImageUrl+mCollBook.getLogo());//解决用glid第一次加载不出来
////        Glide.with(mContext).load(ImageUrl+mCollBook.getLogo()).into(iv_cover_img);
//
//        mPvReadPage.setReaderAdListener(new PageView.ReaderAdListener() {
//
//            @Override
//            public View getCoverPageView() {
//                return coverPageView;
//            }
//        });


        mPageLoader.setOnPageChangeListener(new PageLoader.OnPageChangeListener() {
            @Override
            public void onChapterChange(int pos) {
                mCategoryAdapter.setChapter(pos);
            }

            @Override
            public void onLoadChapter(List<TxtChapter> chapters, int pos) {
                mPresenter.loadContent(mBookId, chapters);
                mHandler.sendEmptyMessage(WHAT_CATEGORY);
                if (mPageLoader.getPageStatus() == NetPageLoader.STATUS_LOADING
                        || mPageLoader.getPageStatus() == NetPageLoader.STATUS_ERROR) {
                    //冻结使用
                    mReadSbChapterProgress.setEnabled(false);
                }

                //隐藏提示
//                mReadTvPageTip.setVisibility(GONE);
                mReadSbChapterProgress.setProgress(0);

            }

            @Override
            public void onCategoryFinish(List<TxtChapter> chapters) {
                mCategoryAdapter.refreshItems(chapters);
            }

            @Override
            public void onPageCountChange(int count) {
                try {
                    if (mReadSbChapterProgress != null){
                        mReadSbChapterProgress.setEnabled(true);
                        mReadSbChapterProgress.setMax(count - 1);
                        mReadSbChapterProgress.setProgress(0);
                        mReadSbChapterProgress.setMax(Math.max(0, count - 1));
                        mReadSbChapterProgress.setProgress(0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageChange(int pos) {
                try {
                    if (num>=45){
                        num = 0;
                        mHandler.postDelayed(progressChangeTask,1000);
                    }
                    mReadSbChapterProgress.post(() -> {
                        mReadSbChapterProgress.setProgress(pos);
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        mReadSbChapterProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mReadLlBottomMenu.getVisibility() == VISIBLE) {
                    //显示标题
//                    mReadTvPageTip.setText((progress + 1) + "/" + (mReadSbChapterProgress.getMax() + 1));
//                    mReadTvPageTip.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //进行切换
                try {
                    int pagePos = mReadSbChapterProgress.getProgress();
                    if (pagePos != mPageLoader.getPagePos()) {
                        mPageLoader.skipToPage(pagePos);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                //隐藏提示
//                mReadTvPageTip.setVisibility(GONE);
            }
        });

        mPvReadPage.setTouchListener(new PageView.TouchListener() {
            @Override
            public void center() {
                toggleMenu(true);
            }

            @Override
            public boolean onTouch() {
                return !hideReadMenu();
            }

            @Override
            public boolean prePage() {
                return true;
            }

            @Override
            public boolean nextPage() {
                return true;
            }

            @Override
            public void cancel() {
            }
        });

        add_gold = AnimatorInflater.loadAnimator(mContext, R.animator.add_gold);

    }

    private Animator add_gold;
    private int num = 0;
    private String oldPosContent,newPosContent;
    private Runnable progressChangeTask = new Runnable() {
        @Override
        public void run() {
            try {
                num++;
                if (num == 1){
                    progressBar.start();
                    oldPosContent = mPageLoader.getPagePosContent();
                }
                if (num == 45){
                    newPosContent = mPageLoader.getPagePosContent();
                    if (oldPosContent.equals(newPosContent)){
                        progressBar.stop();
                        mHandler.removeCallbacks(progressChangeTask);
                        return;
                    }
                }
            }catch (Exception e){
                num = 0;
            }
            mHandler.postDelayed(progressChangeTask,1000);
        }
    };


    /**
     * 新手引导
     */
    private void first(){
        if (!ConfigUtils.getFirstRead()){
            rl_first.setVisibility(VISIBLE);
            if ("0".equals(ConfigUtils.getGender())){
                tv_first.setText("朕知道了");
            }else {
                tv_first.setText("本宫知道了");
            }
        }else {
            rl_first.setVisibility(GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read;
    }

    @Override
    protected ReadActivityPresenter createPresenter() {
        return new ReadActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()) {
            case R.id.read_tv_pre_chapter:
                mPageLoader.skipPreChapter();
                mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
                break;
            case R.id.read_tv_next_chapter:
                mPageLoader.skipNextChapter();
                mCategoryAdapter.setChapter(mPageLoader.getChapterPos());
                break;
            case R.id.read_tv_category:
                findBookMark();
                //移动到指定位置
                if (mCategoryAdapter.getCount() > 0) {
                    mLvCategory.setSelection(mPageLoader.getChapterPos());
                }
                //切换菜单
                toggleMenu(true);
                //打开侧滑动栏
                mReadDlSlide.openDrawer(Gravity.START);
                break;
            case R.id.read_tv_night_mode:
                hideSystemBar();
                toggleMenu(false);
                readLightDialog.show();
                break;
            case R.id.read_tv_setting:
                hideSystemBar();
                toggleMenu(false);
                mSettingDialog.show();
                break;
            case R.id.tv_toolbar_title:
                if (isCollected){
                    SwitchActivityManager.exitActivity(ReadActivity.this);
                }else {
                    if (!addBookShelfDialog.isShowing()){
                        addBookShelfDialog.show();
                    }
                }
                break;
            case R.id.tv_menu:
                readPopWindow.bookMarkOrDelete(1);
                Disposable disposable1 = BookMarkHelpter.getsInstance().findBookMark(mBookId)
                        .compose(RxUtils::toSimpleSingle)
                        .subscribe(beans -> {
                            if (beans.size()>0){
                                for (int i = 0; i < beans.size(); i++) {
                                    BookMarkBean bookMarkBean = beans.get(i);
                                    pagePosContent = bookMarkBean.getContent();
                                    if (pagePosContent.contains(mPageLoader.getPagePosContent())){
                                        readPopWindow.bookMarkOrDelete(2);
                                    }
                                }
                            }
                        });
                mPresenter.addDisposadle(disposable1);

                readPopWindow.showAtBottom(tv_menu,importLocal);
                break;
            case R.id.ll_bookMark:
                catalogOrBookMark(1);
//                findBookMark();
                break;
            case R.id.ll_catalog:
                catalogOrBookMark(0);
                break;
            case R.id.tv_addMark:
                bookRackAdd();
                break;
            case R.id.iv_night_mode:
                if (isNightMode) {
                    isNightMode = false;
                } else {
                    isNightMode = true;
                }
                mPageLoader.setNightMode(isNightMode);
                toggleNightMode();
                break;
            case R.id.tv_first:
                rl_first.setVisibility(GONE);
                ConfigUtils.saveFirstRead(true);
                break;
        }
    }

    /**
     * 加书架
     */
    private void bookRackAdd() {
        tv_addMark.setVisibility(GONE);
        //设置为已收藏
        isCollected = true;
        //设置阅读时间
        mCollBook.setLastRead(StringUtils.
                dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
        mCollBook.setIsLocal(true);
        CollBookHelper.getsInstance().saveBookWithAsync(mCollBook);
        TCAgent.onEvent(mContext, "加书架");
        MobclickAgent.onEvent(mContext, "add_bookshelf", "加书架");
        List<BookChapterBean> bookChapters = mCollBook.getBookChapters();
        if (bookChapters!= null && bookChapters.size()>0){
            double chaptersCount = mCollBook.getBookChapters().size();
            double chapterPos = mPageLoader.getChapterPos();
            double percent = (chapterPos /chaptersCount)*100;
            DecimalFormat df = new DecimalFormat("#0.00");
            ToastUtil.showToast("加入书架成功");
            mPresenter.bookRackAdd(mCollBook.getBookId(),df.format(percent));
        }
    }

    @Override
    protected void initData() {
        if (mCollBook.getImportLocal()){
            mPageLoader.openBook(mCollBook);
        }else {
            //如果是网络文件
            //如果是已经收藏的，那么就从数据库中获取目录
            if (isCollected) {
                Disposable disposable = BookChapterHelper.getsInstance().findBookChaptersInRx(mBookId)
                        .compose(RxUtils::toSimpleSingle)
                        .subscribe(beans -> {
                            if (beans != null && beans.size()>0){
                                firstRead = false;
                                mCollBook.setBookChapters(beans);
                                mCollBook.setChaptersCount(beans.size());
                                mPageLoader.openBook(mCollBook);
                                //如果是被标记更新的,重新从网络中获取目录
                                if (mCollBook.isUpdate()) {
                                    mPresenter.loadChapters(mBookId);
                                }
                            }else {
                                firstRead = true;
                                mPresenter.loadChapters(mBookId);
                            }
                        });
                mPresenter.addDisposadle(disposable);
            } else {
                //加载书籍目录
                mPresenter.loadChapters(mBookId);
            }
        }

        progressBar.setCountdownProgressListener(new CircleProgressBar.OnCircleProgressListener() {
            @Override
            public void onProgress(int progress) {
                if (progress == 30){
                    add_gold.setTarget(tv_addGold);
                    add_gold.start();
                    ToastUtil.showToast("请求接口");
                }
            }

            @Override
            public void onStop(int stop) {
                progressBar.stop();
            }
        });

        mHandler.postDelayed(progressChangeTask,1000);
    }

    /**
     * 查找书签
     */
    private void findBookMark(){
        Disposable disposable1 = BookMarkHelpter.getsInstance().findBookMark(mBookId)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(beans -> {
                    bookMarkAdapter = new BookMarkAdapter();
                    lv_bookMark.setAdapter(bookMarkAdapter);
                    bookMarkAdapter.refreshItems(beans);
                });
        mPresenter.addDisposadle(disposable1);
    }

    @Override
    protected void initListener() {
        tv_first.setOnClickListener(this);
        mReadTvPreChapter.setOnClickListener(this);
        mReadTvNextChapter.setOnClickListener(this);
        mReadTvCategory.setOnClickListener(this);
        mReadTvNightMode.setOnClickListener(this);
        mReadTvSetting.setOnClickListener(this);
        mTvToolbarTitle.setOnClickListener(this);
        tv_menu.setOnClickListener(this);
        ll_catalog.setOnClickListener(this);
        ll_bookMark.setOnClickListener(this);
        tv_addMark.setOnClickListener(this);
        iv_night_mode.setOnClickListener(this);
        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    private void setCategory() {
        mCategoryAdapter = new CategoryAdapter();
        mLvCategory.setAdapter(mCategoryAdapter);
        mLvCategory.setFastScrollEnabled(true);

        mLvCategory.setOnItemClickListener((parent, view, position, id) -> {
            //延时关闭抽屉，不然会有卡顿
            mHandler.sendEmptyMessageDelayed(WHAT_CLOSE_CHAPTER,300);
            mPageLoader.skipToChapter(position);
        });

        lv_bookMark.setOnItemClickListener((parent, view, position, id) -> {
            mPageLoader.openBookMark(bookMarkAdapter.getItem(position));
            bookMarkAdapter.setChapter(position);
            lv_bookMark.setSelection(position);
            mHandler.sendEmptyMessage(WHAT_OPEN_BOOKMARK);
            mHandler.sendEmptyMessageDelayed(WHAT_CLOSE_CHAPTER,300);
        });

        lv_bookMark.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                BookMarkBean item = bookMarkAdapter.getItem(position);
                String content = item.getContent();
                BookMarkHelpter.getsInstance().removeBookMark(content);
                findBookMark();
                ToastUtil.showToast("删除书签成功");
                return true;
            }
        });
    }

    private void toggleNightMode() {
        if (isNightMode) {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.read_menu_morning);
            iv_night_mode.setImageDrawable(drawable);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                getWindow().setNavigationBarColor(ContextCompat.getColor(ZApplication.getAppContext(), R.color.color_0a0907));
            }

        } else {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.read_menu_night);
            iv_night_mode.setImageDrawable(drawable);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                getWindow().setNavigationBarColor(ContextCompat.getColor(ZApplication.getAppContext(), R.color.white));
            }
        }
    }


    @Override
    public void setStatusBar() {
//        super.setStatusBar();
    }

    private void showSystemBar() {
        //显示
        StatusBarUtils.showUnStableStatusBar(this);
    }

    private void hideSystemBar() {
        //隐藏
        StatusBarUtils.hideStableStatusBar(this);
    }


    private void initTopMenu() {
        if (Build.VERSION.SDK_INT >= 19) {
            mReadAblTopMenu.setPadding(0, ScreenUtils.getStatusBarHeight(), 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                getWindow().setStatusBarColor(ContextCompat.getColor(ZApplication.getAppContext(), R.color.color_19));
            }
        }
    }

    private void initBottomMenu() {
        //判断是否全屏
        if (ReadSettingManager.getInstance().isFullScreen()) {
            //还需要设置mBottomMenu的底部高度
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mReadLlBottomMenu.getLayoutParams();
            params.bottomMargin = ScreenUtils.getNavigationBarHeight();
            mReadLlBottomMenu.setLayoutParams(params);
        } else {
            //设置mBottomMenu的底部距离
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mReadLlBottomMenu.getLayoutParams();
            params.bottomMargin = 0;
            mReadLlBottomMenu.setLayoutParams(params);
        }
    }


    /**
     * 隐藏阅读界面的菜单显示
     *
     * @return 是否隐藏成功
     */
    private boolean hideReadMenu() {
        hideSystemBar();
        if (mReadAblTopMenu.getVisibility() == VISIBLE) {
            toggleMenu(true);
            return true;
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return true;
        }
        return false;
    }

    /**
     * 切换菜单栏的可视状态
     * 默认是隐藏的
     */
    private void toggleMenu(boolean hideStatusBar) {
        initMenuAnim();

        if (mReadAblTopMenu.getVisibility() == View.VISIBLE) {
            //关闭
            mReadAblTopMenu.startAnimation(mTopOutAnim);
            mReadLlBottomMenu.startAnimation(mBottomOutAnim);
            mReadAblTopMenu.setVisibility(GONE);
            mReadLlBottomMenu.setVisibility(GONE);
//            mReadTvPageTip.setVisibility(GONE);
            if (!isCollected){
                tv_addMark.startAnimation(mRightOutAnim);
                tv_addMark.setVisibility(GONE);
            }

            if (hideStatusBar) {
                hideSystemBar();
            }
        } else {
            mReadAblTopMenu.setVisibility(View.VISIBLE);
            mReadLlBottomMenu.setVisibility(View.VISIBLE);
            mReadAblTopMenu.startAnimation(mTopInAnim);
            mReadLlBottomMenu.startAnimation(mBottomInAnim);
            if (!isCollected){//已加入书签不弹出
                tv_addMark.setVisibility(VISIBLE);
                tv_addMark.startAnimation(mRightInAnim);
            }

            showSystemBar();
        }
    }


    //初始化菜单动画
    private void initMenuAnim() {
        if (mTopInAnim != null) return;

        mTopInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_in);
        mTopOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_top_out);
        mBottomInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_in);
        mBottomOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_bottom_out);
        mRightInAnim = AnimationUtils.loadAnimation(this, R.anim.slide_right_mark_in);
        mRightOutAnim = AnimationUtils.loadAnimation(this, R.anim.slide_right_mark_out);
        //退出的速度要快
        mTopOutAnim.setDuration(200);
        mBottomOutAnim.setDuration(200);
        mRightOutAnim.setDuration(200);
    }

    private void catalogOrBookMark(int state){
        if (0 == state){//目录
            tv_catalog.setTextColor(getResources().getColor(R.color.colorTheme));
            view_catalog.setBackgroundColor(getResources().getColor(R.color.colorTheme));
            tv_bookMark.setTextColor(getResources().getColor(R.color.a2a9b2));
            view_bookMark.setBackgroundColor(getResources().getColor(R.color.transparency));
            rl_catalog.setVisibility(VISIBLE);
            rl_bookMark.setVisibility(GONE);
            if (bookChapterList.size()>0){
                tv_catalogNum.setVisibility(VISIBLE);
            }
        }else if (1== state){//书签
            tv_catalog.setTextColor(getResources().getColor(R.color.a2a9b2));
            view_catalog.setBackgroundColor(getResources().getColor(R.color.transparency));
            tv_bookMark.setTextColor(getResources().getColor(R.color.colorTheme));
            view_bookMark.setBackgroundColor(getResources().getColor(R.color.colorTheme));
            rl_catalog.setVisibility(GONE);
            rl_bookMark.setVisibility(VISIBLE);
            tv_catalogNum.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void bookChapters(BaseModel<BookChaptersBean> bookChapter) {
        BookChaptersBean bookChaptersBean = bookChapter.getData();

        bookChapterList.clear();
        for (BookChapterBean bean : bookChaptersBean.getMenu()) {
            BookChapterBean chapterBean = new BookChapterBean();
            chapterBean.setBookId(mBookId);
            chapterBean.setContent(bean.getContent());
            chapterBean.setTitle(bean.getName());
            chapterBean.setId(bean.getId());
            chapterBean.setUnreadble(false);
            bookChapterList.add(chapterBean);
        }

        mCollBook.setBookChapters(bookChapterList);
        mCollBook.setChaptersCount(bookChapterList.size());
        if (bookChapterList.size()>0){
            tv_catalogNum.setVisibility(VISIBLE);
            tv_catalogNum.setText("共"+bookChapterList.size()+"章");
        }

        //如果是更新加载，那么重置PageLoader的Chapter
        if (mCollBook.isUpdate() && isCollected) {
            mPageLoader.setChapterList(bookChapterList);
            //异步下载更新的内容存到数据库
            BookChapterHelper.getsInstance().saveBookChaptersWithAsync(bookChapterList);
            if (firstRead){
                mPageLoader.openBook(mCollBook);
            }
        } else {
            mPageLoader.openBook(mCollBook);
        }
    }

    @Override
    public void finishChapters() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mHandler.sendEmptyMessage(WHAT_CHAPTER);
        }
        //当完成章节的时候，刷新列表
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void errorChapters() {
        if (mPageLoader.getPageStatus() == PageLoader.STATUS_LOADING) {
            mPageLoader.chapterError();
        }
    }

    @Override
    public void shareUrl(BaseModel<PublicBean> url) {
        PublicBean data = url.getData();
        if (StringUtils.isNotBlank(data.getUrl())){
            shareUrl = data.getUrl();
            toggleMenu(false);
            shareDialog.show();
        }

    }

    @Override
    public void refresh(BaseModel<String> num) {
        tv_addGold.setText(num.getData());
        add_gold.setTarget(tv_addGold);
        add_gold.start();
    }


    //注册亮度观察者
    private void registerBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (!isRegistered) {
                    final ContentResolver cr = getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    isRegistered = true;
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "[ouyangyj] register mBrightObserver error! " + throwable);
        }
    }

    //解注册
    private void unregisterBrightObserver() {
        try {
            if (mBrightObserver != null) {
                if (isRegistered) {
                    getContentResolver().unregisterContentObserver(mBrightObserver);
                    isRegistered = false;
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "unregister BrightnessObserver error! " + throwable);
        }
    }


    public void onBackPressed() {
        if (mReadAblTopMenu.getVisibility() == View.VISIBLE) {
            //非全屏下才收缩，全屏下直接退出
            if (!ReadSettingManager.getInstance().isFullScreen()) {
                toggleMenu(true);
                return;
            }
        } else if (mSettingDialog.isShowing()) {
            mSettingDialog.dismiss();
            return;
        } else if (mReadDlSlide.isDrawerOpen(Gravity.START)) {
            mReadDlSlide.closeDrawer(Gravity.START);
            return;
        }else if (addBookShelfDialog.isShowing()){
            addBookShelfDialog.dismiss();
        }else if (!addBookShelfDialog.isShowing() && !isCollected){
            addBookShelfDialog.show();
        }else {
            SwitchActivityManager.exitActivity(ReadActivity.this);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean isVolumeTurnPage = ReadSettingManager
                .getInstance().isVolumeTurnPage();
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                onBackPressed();
                break;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (isVolumeTurnPage) {
//                    return mPageLoader.autoPrevPage();
                    simulateClick(mPvReadPage,10,700);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (isVolumeTurnPage) {
//                    return mPageLoader.autoNextPage();
                    simulateClick(mPvReadPage,1080,700);
                    return true;
                }
                break;
        }
        return true;
    }

    /**
     * 音量键翻页，模拟点击屏幕，不然没有动画效果，先实现了效果后期优化
     * @param view
     * @param x
     * @param y
     */
    private void simulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,MotionEvent.ACTION_DOWN, x, y, 0);
        downTime += 1000;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,MotionEvent.ACTION_UP, x, y, 0);
        view.onTouchEvent(downEvent);
        view.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBrightObserver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWakeLock.release();
        MobclickAgent.onPause(this);
        mPageLoader.saveRecord(isCollected);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterBrightObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mReceiverTag && mReceiver != null){
                unregisterReceiver(mReceiver);
            }
            if (addBookShelfDialog != null){
                addBookShelfDialog.dismiss();
            }
            if (mSettingDialog!=null){
                mSettingDialog.dismiss();
            }
            if (readLightDialog!= null){
                readLightDialog.dismiss();
            }
            if (shareDialog!= null){
                shareDialog.dismiss();
            }
            if (readPopWindow!= null){
                readPopWindow.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            if (mPageLoader != null){
                mPageLoader.closeBook();
                mPageLoader = null;
            }
            TCAgent.onPageEnd(mContext, "阅读器");
            MobclickAgent.onPageEnd("阅读器");
        }catch (Exception e){
            e.printStackTrace();
        }

        addBookShelfDialog = null;
        mSettingDialog = null;
        readLightDialog = null;
        shareDialog = null;
        readPopWindow = null;

        mHandler.removeMessages(WHAT_CATEGORY);
        mHandler.removeMessages(WHAT_CHAPTER);
        mHandler.removeMessages(WHAT_CLOSE_CHAPTER);
        mHandler.removeMessages(WHAT_OPEN_BOOKMARK);
        UMShareAPI.get(this).release();

    }

    //加入书架
    @Override
    public void addBookShelf() {
        bookRackAdd();
        if (addBookShelfDialog != null){
            addBookShelfDialog.dismiss();
        }
        SwitchActivityManager.exitActivity(ReadActivity.this);
    }

    @Override
    public void exitReadActivity() {
        if (mCollBook != null && mPageLoader != null && mCollBook.getBookChapters()!= null){
            double chaptersCount = mCollBook.getBookChapters().size();
            double chapterPos = mPageLoader.getChapterPos();
            double percent = (chapterPos /chaptersCount)*100;
            DecimalFormat df = new DecimalFormat("#0.00");
            mPresenter.updateRackAndHistory(mBookId,df.format(percent));
        }

        SwitchActivityManager.exitActivity(ReadActivity.this);
    }

    @Override
    public void bookRackAdd(BaseModel<PublicBean> publicBean) {
//        ToastUtil.showToast("加入书架成功");
//        SwitchActivityManager.exitActivity(ReadActivity.this);
    }

    @Override
    public void addBookMark() {
        mPageLoader.saveBookMark();
        ToastUtil.showToast("添加书签成功");
    }

    @Override
    public void deleteBookMark() {
        BookMarkHelpter.getsInstance().removeBookMark(pagePosContent);
        ToastUtil.showToast("删除书签成功");
    }

    @Override
    public void bookDetail() {
        SwitchActivityManager.startBookDetailActivity(mContext,mBookId);
    }

    @Override
    public void shareBook() {
        mPresenter.shareUrl();
    }

    @Override
    public void report() {
        if (StringUtils.isNotBlank(ConfigUtils.getToken())){
            SwitchActivityManager.startReportActivity(mContext);
        }else {
            SwitchActivityManager.startLoginActivity(mContext);
        }

    }

    @Override
    public void shareWeiXin() {
        ShareUtils.shareWeb(this, shareUrl+mBookId,mCollBook.getName()
                , mCollBook.getNotes(),ImageUrl+mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.WEIXIN
        );
    }

    @Override
    public void shareWeiXinCircle() {
        ShareUtils.shareWeb(this,shareUrl+mBookId,mCollBook.getName()
                , mCollBook.getNotes(),ImageUrl+mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.WEIXIN_CIRCLE
        );
    }

    @Override
    public void shareQQ() {
        ShareUtils.shareWeb(this, shareUrl+mBookId,mCollBook.getName()
                , mCollBook.getNotes(),ImageUrl+mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.QQ
        );
    }

    @Override
    public void shareQzone() {
        ShareUtils.shareWeb(this, shareUrl+mBookId,mCollBook.getName()
                , mCollBook.getNotes(),ImageUrl+mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.QZONE
        );
    }

    @Override
    public void shareSina() {
        ShareUtils.shareWeb(this, shareUrl+mBookId,mCollBook.getName()
                , mCollBook.getNotes(),ImageUrl+mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.SINA
        );
    }

    @Override
    public void shareCopyUrl() {
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText(null,shareUrl+mBookId);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
        ToastUtil.showToast("复制链接成功");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                mPageLoader.updateLayout();
            break;
        }
    }

    @Override
    public void netWorkConnect(boolean connect) {
        super.netWorkConnect(connect);
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

}
