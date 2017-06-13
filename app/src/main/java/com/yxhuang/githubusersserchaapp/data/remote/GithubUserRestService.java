package com.yxhuang.githubusersserchaapp.data.remote;

import com.yxhuang.githubusersserchaapp.data.remote.model.User;
import com.yxhuang.githubusersserchaapp.data.remote.model.UsersList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Author: yxhuang
 * Date: 2017/6/12
 * Email: yxhuang@gmail.com
 */

public interface GithubUserRestService {

    @GET("/search/users?per_page=2")
    Observable<UsersList> searchGithubUsers(@Query("q") String searchTerm);

    @GET("/users/{username}")
    Observable<User> getUser(@Path("username") String username);
}
