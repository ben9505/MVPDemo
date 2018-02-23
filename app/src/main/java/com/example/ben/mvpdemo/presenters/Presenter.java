package com.example.ben.mvpdemo.presenters;

import android.content.ContentResolver;
import android.text.TextUtils;

import com.example.ben.mvpdemo.models.User;
import com.example.ben.mvpdemo.views.ViewSaveData;

/**
 * Created by ben on 23/02/2018.
 */

public class Presenter {

    public static void saveDataToLocal(ContentResolver cr, String name, String email, ViewSaveData viewSaveData) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)) {
            User user = new User(name,email);
            AppContentUtils.USER.insertOrUpdateUser(cr,user);
            viewSaveData.onSuccessSaveData();
        } else {
            viewSaveData.onErrorSaveData();
        }

    }

}
