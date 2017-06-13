package com.yxhuang.githubusersserchaapp.presentation.search;

import com.yxhuang.githubusersserchaapp.data.UserRepository;
import com.yxhuang.githubusersserchaapp.data.remote.model.User;
import com.yxhuang.githubusersserchaapp.presentation.base.BasePresenter;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;

/**
 * Author: yxhuang
 * Date: 2017/6/12
 * Email: yxhuang@gmail.com
 */

public class UserSearchPresenter extends BasePresenter<UserSearchContract.View> implements UserSearchContract.Presenter {

    private Scheduler mMainScheduler, mIoScheduler;
    private UserRepository mUserRepository;

    public UserSearchPresenter(Scheduler mainScheduler, Scheduler ioScheduler, UserRepository userRepository) {
        mMainScheduler = mainScheduler;
        mIoScheduler = ioScheduler;
        mUserRepository = userRepository;
    }

    @Override
    public void search(String term) {
        checkViewAttached();

        getView().showLoading();
        addSubscription(mUserRepository.searchUsers(term)
        .subscribeOn(mIoScheduler)
        .observeOn(mMainScheduler)
        .subscribe(new Subscriber<List<User>>(){

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                getView().showError(e.getMessage());

            }

            @Override
            public void onNext(List<User> users) {
                getView().hideLoading();
                getView().showSearchResults(users);
            }
        }));
    }
}
