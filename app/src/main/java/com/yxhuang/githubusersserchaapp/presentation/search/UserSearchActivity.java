package com.yxhuang.githubusersserchaapp.presentation.search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yxhuang.githubusersserchaapp.R;
import com.yxhuang.githubusersserchaapp.data.remote.model.User;
import com.yxhuang.githubusersserchaapp.injection.Injection;
import com.yxhuang.githubusersserchaapp.presentation.base.BaseActivity;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserSearchActivity extends BaseActivity implements UserSearchContract.View {

    private UserSearchContract.Presenter userSearchPresenter;
    private UsersAdapter usersAdapter;
    private SearchView searchView;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewUsers;
    private TextView textViewErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);

        userSearchPresenter = new UserSearchPresenter(AndroidSchedulers.mainThread(), Schedulers.io(), Injection.provideUserRepo());

        userSearchPresenter.attachView(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        textViewErrorMessage = (TextView) findViewById(R.id.text_view_error_msg);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recycler_view_users);
        usersAdapter = new UsersAdapter(null, this);
        recyclerViewUsers.setAdapter(usersAdapter);


    }

    @Override
    public void showSearchResults(List<User> githubUserList) {
        recyclerViewUsers.setVisibility(View.VISIBLE);
        textViewErrorMessage.setVisibility(View.GONE);
        usersAdapter.setItems(githubUserList);
    }

    @Override
    public void showError(String message) {
        textViewErrorMessage.setVisibility(View.VISIBLE);
        recyclerViewUsers.setVisibility(View.GONE);
        textViewErrorMessage.setText(message);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewUsers.setVisibility(View.GONE);
        textViewErrorMessage.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        recyclerViewUsers.setVisibility(View.VISIBLE);
        textViewErrorMessage.setVisibility(View.GONE);
    }
}
