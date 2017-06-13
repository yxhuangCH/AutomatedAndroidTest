package com.yxhuang.githubusersserchaapp.presentation.search;

import com.yxhuang.githubusersserchaapp.data.UserRepository;
import com.yxhuang.githubusersserchaapp.data.remote.model.User;
import com.yxhuang.githubusersserchaapp.data.remote.model.UsersList;
import com.yxhuang.githubusersserchaapp.presentation.base.BasePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Author: yxhuang
 * Date: 2017/6/13
 * Email: yxhuang@gmail.com
 */
public class UserSearchPresenterTest {
    private static final String USER_LOGIN_RIGGAROO = "riggaroo";
    private static final String USER_LOGIN_2_REBECCA = "rebecca";

    @Mock
    UserRepository userRepository;

    @Mock
    UserSearchContract.View view;

    UserSearchPresenter userSearchPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userSearchPresenter = new UserSearchPresenter(Schedulers.immediate(), Schedulers.immediate(), userRepository);
        userSearchPresenter.attachView(view);
    }

    // 1. 测试正常情况下的执行效果
    @Test
    public void search_ValidSearchTerm_ReturnsResults() {
        // Given
        UsersList usersList = getDummyUserList();
        when(userRepository.searchUsers(anyString())).thenReturn(Observable.just(usersList.getItems()));

        // When
        userSearchPresenter.search("riggaroo");

        // then
//        verify(view).showLoading();
//        verify(view).hideLoading();
//        verify(view).showSearchResults(usersList.getItems());
//        verify(view, never()).showError(anyString());

        // InOrder 验证按顺序执行
        InOrder inOrder = inOrder(view);
        inOrder.verify(view).showLoading();
        inOrder.verify(view).hideLoading();
        inOrder.verify(view).showSearchResults(usersList.getItems());
        inOrder.verify(view, never()).showError(anyString());
    }

    // 2. 测试返回异常的情况
    @Test
    public void search_UserRepositoryErrorMsg() {
        String errorMsg = "No internet";
        // Given
        when(userRepository.searchUsers(anyString())).thenReturn(Observable.error(new IOException(errorMsg)));

        // When
        userSearchPresenter.search("bookdash");

        // then
        verify(view).showLoading();
        verify(view).hideLoading();
        verify(view, never()).showSearchResults(anyList());
        verify(view).showError(errorMsg);
    }

    // 3. 测试 view 没有依附的情况
    @Test(expected = BasePresenter.MvpViewNotAttachedException.class) // 去除 expected 内容验证
    public void search_NotAttached_ThrowsMvpException() {
        // a.首先移除 view
        userSearchPresenter.detachView();

        // b.调用 search 方法
        userSearchPresenter.search("test");

        // c. 验证
        verify(view).showLoading();
        verify(view, never()).showSearchResults(anyList());
    }

    UsersList getDummyUserList() {
        List<User> githubUsers = new ArrayList<>();
        githubUsers.add(user1FullDetails());
        githubUsers.add(user2FullDetails());
        return new UsersList(githubUsers);
    }

    User user1FullDetails() {
        return new User(USER_LOGIN_RIGGAROO, "Rigs Franks", "avatar_url", "Bio1");
    }

    User user2FullDetails() {
        return new User(USER_LOGIN_2_REBECCA, "Rebecca Franks", "avatar_url2", "Bio2");
    }

}