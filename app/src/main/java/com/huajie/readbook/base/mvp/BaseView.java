package com.huajie.readbook.base.mvp;

/**
 *描述：基本回调 可自定义添加所需回调
 *作者：Created by zhuzhen
 */

public interface BaseView {
    /**
     * 显示dialog
     */
    void showLoading();
    /**
     * 隐藏 dialog
     */

    void hideLoading();
    /**
     * 显示错误信息
     *
     * @param msg
     */
    void showError(String msg);
    /**
     * 错误码
     */
    void onErrorCode(BaseModel model);

    /**
     * 网络是否可用
     * @param connect
     */
    void netWorkConnect(boolean connect);
}
