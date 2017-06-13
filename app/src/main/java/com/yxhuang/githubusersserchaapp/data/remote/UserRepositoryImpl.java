package com.yxhuang.githubusersserchaapp.data.remote;

import com.yxhuang.githubusersserchaapp.data.UserRepository;
import com.yxhuang.githubusersserchaapp.data.remote.model.User;

import java.io.IOException;
import java.util.List;

import rx.Observable;

/**
 * Author: yxhuang
 * Date: 2017/6/12
 * Email: yxhuang@gmail.com
 */

public class UserRepositoryImpl implements UserRepository {

    private GithubUserRestService mGithubUserRestService;

    public UserRepositoryImpl(GithubUserRestService githubUserRestService) {
        mGithubUserRestService = githubUserRestService;
    }

    @Override
    public Observable<List<User>> searchUsers(String searchTerm) {
        return Observable.defer(() -> mGithubUserRestService.searchGithubUsers(searchTerm).concatMap(
                usersList -> Observable.from(usersList.getItems())
                .concatMap(user -> mGithubUserRestService.getUser(user.getLogin())).toList()))
                .retryWhen(observable -> observable.flatMap(o -> {
                    if (o instanceof IOException){
                        return Observable.just(null);
                    }
                    return Observable.error(o);
                }));
    }
}
