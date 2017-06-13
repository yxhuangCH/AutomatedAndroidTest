package com.yxhuang.githubusersserchaapp.injection;


import com.yxhuang.githubusersserchaapp.data.UserRepository;
import com.yxhuang.githubusersserchaapp.data.remote.GithubUserRestService;
import com.yxhuang.githubusersserchaapp.data.remote.UserRepositoryImpl;
import com.yxhuang.githubusersserchaapp.injection.data.remote.MockGithubUserRestServiceImpl;

public class Injection {

    private static GithubUserRestService userRestService;


    public static UserRepository provideUserRepo() {
        return new UserRepositoryImpl(provideGithubUserRestService());
    }

    static GithubUserRestService provideGithubUserRestService() {
        if (userRestService == null) {
            userRestService = new MockGithubUserRestServiceImpl();
        }
        return userRestService;
    }

}