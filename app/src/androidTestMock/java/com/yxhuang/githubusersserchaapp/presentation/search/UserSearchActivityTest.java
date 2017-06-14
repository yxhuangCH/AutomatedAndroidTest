package com.yxhuang.githubusersserchaapp.presentation.search;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import com.yxhuang.githubusersserchaapp.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Author: yxhuang
 * Date: 2017/6/14
 * Email: yxhuang@gmail.com
 */
@RunWith(AndroidJUnit4.class)
public class UserSearchActivityTest {

    @Rule
    public ActivityTestRule<UserSearchActivity> mTestRule = new ActivityTestRule<>(UserSearchActivity.class);

    @Test
    public void searchActivity_onLaunch_HintTextDisplayed() {
        // Given activity automatically launched
        // When user doesn't interact with the view
        // then
        onView(withText("Start typing to search")).check(matches(isDisplayed()));
    }

    @Test
    public void searchText_ReturnsCorrectlyFromWebService_DisplaysResult() {
        // Given activity automatically launched

        // When
        // 1.点击搜索图标
        onView(allOf(withId(R.id.menu_search), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
        // 2. 输入 riggaroo ， 按回车键
        onView(withId(R.id.search_src_text)).perform(typeText("riggaroo"), pressKey(KeyEvent.KEYCODE_ENTER));

        // Then
        onView(withText("Start typing to search")).check(matches(not(isDisplayed())));
        onView(withText("riggaroo - Rebecca Franks")).check(matches(isDisplayed()));
        onView(withText("Android Dev")).check(matches(isDisplayed()));
        onView(withText("A unicorn")).check(matches(isDisplayed()));
        onView(withText("riggaroo2 - Rebecca's Alter Ego")).check(matches(isDisplayed()));
    }

//    @Test
//    public void searchText_ServiceCallFails_DisplayError() {
//        String errorMsg = "Server Error";
//        MockGithubUserRestServiceImpl.setDummySearchGithubCallResult(Observable.error(new Exception(errorMsg)));
//
//        onView(allOf(withId(R.id.menu_search), withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).perform(click());
//        onView(withId(R.id.search_src_text)).perform(typeText("riggaroo"), pressKey(KeyEvent.KEYCODE_ENTER));
//        onView(withText(errorMsg)).check(matches(isDisplayed()));
//
//    }

}