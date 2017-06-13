package com.yxhuang.githubusersserchaapp.presentation.base;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Author: yxhuang
 * Date: 2017/6/12
 * Email: yxhuang@gmail.com
 */

public class BasePresenter<T extends MvpView> implements MvpPresenter<T>{

    private T view;

    private CompositeSubscription mSubscription = new CompositeSubscription();

    @Override
    public void attachView(T mvpView) {
        view = mvpView;
    }

    @Override
    public void detachView() {
        mSubscription.clear();
        view = null;
    }

    public T getView(){
        return view;
    }

    public void checkViewAttached(){
        if (!isViewAttached()){
            throw new MvpViewNotAttachedException();
        }
    }

    protected void addSubscription(Subscription subscription){
        this.mSubscription.add(subscription);
    }

    private boolean isViewAttached(){
        return view != null;
    }

    public static final class MvpViewNotAttachedException extends RuntimeException{
        public MvpViewNotAttachedException(){
            super("Please call Presenter.attachView(MvpView) before" + " requesting data to the Presenter");
        }
    }
}
