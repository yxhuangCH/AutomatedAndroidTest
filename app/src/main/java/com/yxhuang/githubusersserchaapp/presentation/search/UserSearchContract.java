package com.yxhuang.githubusersserchaapp.presentation.search;

import com.yxhuang.githubusersserchaapp.data.remote.model.User;
import com.yxhuang.githubusersserchaapp.presentation.base.MvpPresenter;
import com.yxhuang.githubusersserchaapp.presentation.base.MvpView;

import java.util.List;

/**
 * Author: yxhuang
 * Date: 2017/6/12
 * Email: yxhuang@gmail.com
 */

public interface UserSearchContract {

    interface View extends MvpView {
        void showSearchResults(List<User> githubUserList);

        void showError(String message);

        void showLoading();

        void hideLoading();
    }


    interface Presenter extends MvpPresenter<View> {
        void search(String term);
    }
}
