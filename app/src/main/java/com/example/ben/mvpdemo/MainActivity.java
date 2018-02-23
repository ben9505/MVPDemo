package com.example.ben.mvpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.example.ben.mvpdemo.adapter.UserAdapter;
import com.example.ben.mvpdemo.presenters.AppContentUtils;
import com.example.ben.mvpdemo.models.User;
import com.example.ben.mvpdemo.presenters.Presenter;
import com.example.ben.mvpdemo.views.ViewSaveData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getName();

    @BindView(R.id.edName) EditText edName;
    @BindView(R.id.edEmail) EditText edEmail;
    @BindView(R.id.rcView) RecyclerView rcView;

    ArrayList<User> users;
    UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListUsers();
    }

    private void initListUsers() {
        users = AppContentUtils.USER.getAllUsers(getContentResolver());
        rcView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userAdapter = new UserAdapter(this,users);
        rcView.setAdapter(userAdapter);
    }

    @OnClick(R.id.btnSave)
    public void onClick(View v) {
        Presenter.saveDataToLocal(getContentResolver(), edName.getText().toString().trim(), edEmail.getText().toString().trim(), new ViewSaveData() {
            @Override
            public void onSuccessSaveData() {
                super.onSuccessSaveData();
                users = AppContentUtils.USER.getAllUsers(getContentResolver());
                userAdapter.notify(users);
            }
        });
    }

}
