package com.yxhuang.githubusersserchaapp.data.remote;

import com.yxhuang.githubusersserchaapp.data.UserRepository;
import com.yxhuang.githubusersserchaapp.data.remote.model.User;
import com.yxhuang.githubusersserchaapp.data.remote.model.UsersList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Author: yxhuang
 * Date: 2017/6/13
 * Email: yxhuang@gmail.com
 */
public class UserRepositoryImplTest {

    private static final String USER_LOGIN_RIGGAROO = "riggaroo";
    private static final String USER_LOGIN_2_REBECCA = "rebecca";

    @Mock
    GithubUserRestService githubUserRestService;

    private UserRepository userRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userRepository = new UserRepositoryImpl(githubUserRestService);

    }

    @Test
    public void searchUsers_200OKResponse_InvokesCorrectApiCalls(){
        // 命名建议 [Name of method under test]_[Conditions of test case]_[Expected Result]
        // Given
        when(githubUserRestService.searchGithubUsers(anyString())).thenReturn(Observable.just(githubUserList()));  // 当方法调用是，返回指定值
        when(githubUserRestService.getUser(anyString())).thenReturn(Observable.just(user1FullDetails()), Observable.just(user2FullDetails()));

        // When
        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        userRepository.searchUsers(USER_LOGIN_RIGGAROO).subscribe(subscriber);

        // Then
        subscriber.awaitTerminalEvent();  // 当 Subscriber 完成
        subscriber.assertNoErrors();

        List<List<User>> onNextEvents = subscriber.getOnNextEvents();
        List<User> users = onNextEvents.get(0);
        Assert.assertEquals(USER_LOGIN_RIGGAROO, users.get(0).getLogin());  // 断言相等
        Assert.assertEquals(USER_LOGIN_2_REBECCA, users.get(1).getLogin());

        // verify 监控方法调用
        // 验证 githubUserRestService 调用方法 searchGithubUsers(...) 时，参数是 USER_LOGIN_RIGGAROO
        verify(githubUserRestService).searchGithubUsers(USER_LOGIN_RIGGAROO);
        verify(githubUserRestService).getUser(USER_LOGIN_RIGGAROO);
        verify(githubUserRestService).getUser(USER_LOGIN_2_REBECCA);
    }

    @Test
    public void searchUsers_IOExceptionThenSuccess_SearchUserRetried() {
        // Given
        when(githubUserRestService.searchGithubUsers(anyString()))
                .thenReturn(getIOExceptionError(), Observable.just(githubUserList()));  // 当方法调用是，返回指定值   // 这里变化 ！！！！
        when(githubUserRestService.getUser(anyString())).thenReturn(Observable.just(user1FullDetails()), Observable.just(user2FullDetails()));

        // When
        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        userRepository.searchUsers(USER_LOGIN_RIGGAROO).subscribe(subscriber);

        // Then
        subscriber.awaitTerminalEvent();  // 当 Subscriber 完成
        subscriber.assertNoErrors();

        verify(githubUserRestService, times(2)).searchGithubUsers(USER_LOGIN_RIGGAROO);  // 这里变化 ！！！！
        verify(githubUserRestService).getUser(USER_LOGIN_RIGGAROO);
        verify(githubUserRestService).getUser(USER_LOGIN_2_REBECCA);

    }

    @Test
    public void searchUsers_GetUserIOExceptionThenSuccess_SearchUsersRetried() {
        // Given
        when(githubUserRestService.searchGithubUsers(anyString())).thenReturn(Observable.just(githubUserList()));  // 当方法调用是，返回指定值
        when(githubUserRestService.getUser(anyString()))
                .thenReturn(getIOExceptionError(), Observable.just(user1FullDetails()), Observable.just(user2FullDetails()));   // 这里变化 ！！！！

        // When
        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        userRepository.searchUsers(USER_LOGIN_RIGGAROO).subscribe(subscriber);

        // Then
        subscriber.awaitTerminalEvent();  // 当 Subscriber 完成
        subscriber.assertNoErrors();

        // verify 监控方法调用
        // 验证 githubUserRestService 调用方法 searchGithubUsers(...) 时，参数是 USER_LOGIN_RIGGAROO
        verify(githubUserRestService, times(2)).searchGithubUsers(USER_LOGIN_RIGGAROO);   // 这里变化 ！！！！
        verify(githubUserRestService, times(2)).getUser(USER_LOGIN_RIGGAROO);           // 这里变化 ！！！！
        verify(githubUserRestService).getUser(USER_LOGIN_2_REBECCA);
    }

    @Test
    public void searchUsers_OtherHttpError_SearchTerminatedWithError() {
        // Given
        when(githubUserRestService.searchGithubUsers(anyString())).thenReturn(get403ForbiddenError());

        // When
        TestSubscriber<List<User>> subscriber = new TestSubscriber<>();
        userRepository.searchUsers(USER_LOGIN_RIGGAROO).subscribe(subscriber);

        // Then
        subscriber.awaitTerminalEvent();  // 当 Subscriber 完成
        subscriber.assertError(HttpException.class);   // 这里变化 ！！！！

        // verify 监控方法调用
        // 验证 githubUserRestService 调用方法 searchGithubUsers(...) 时，参数是 USER_LOGIN_RIGGAROO
        verify(githubUserRestService).searchGithubUsers(USER_LOGIN_RIGGAROO);

        verify(githubUserRestService, never()).getUser(USER_LOGIN_RIGGAROO);   // 出现异常之后，调用不到后面了
        verify(githubUserRestService, never()).getUser(USER_LOGIN_2_REBECCA);


    }


    private UsersList githubUserList(){
        User user = new User();
        user.setLogin(USER_LOGIN_RIGGAROO);

        User user2 = new User();
        user2.setLogin(USER_LOGIN_2_REBECCA);

        List<User> githubUserList = new ArrayList<>();
        githubUserList.add(user);
        githubUserList.add(user2);

        return new  UsersList(githubUserList);
    }

    private User user1FullDetails(){
        User user = new User();
        user.setLogin(USER_LOGIN_RIGGAROO);
        user.setName("Rigs Franks");
        user.setAvatarUrl("avatar_url");
        user.setBio("Bio1");
        return user;
    }

    private User user2FullDetails(){
        User user = new User();
        user.setLogin(USER_LOGIN_2_REBECCA);
        user.setName("Rebecca Franks");
        user.setAvatarUrl("avatar_url2");
        user.setBio("Bio2");
        return user;
    }

    private Observable getIOExceptionError() {
        return Observable.error(new IOException());
    }


    private Observable<UsersList> get403ForbiddenError() {
        return Observable.error(new HttpException(
                Response.error(403, ResponseBody.create(MediaType.parse("application/json"), "Forbidden"))));
    }



}