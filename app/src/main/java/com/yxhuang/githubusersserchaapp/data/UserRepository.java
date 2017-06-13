package com.yxhuang.githubusersserchaapp.data;

import com.yxhuang.githubusersserchaapp.data.remote.model.User;

import java.util.List;

import rx.Observable;

/**
 * Author: yxhuang
 * Date: 2017/6/12
 * Email: yxhuang@gmail.com
 */

public interface UserRepository {

    Observable<List<User>> searchUsers(String searchTerm);

}
