package com.eunwon.duri;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SpManager {
    private static SpManager instance = new SpManager();

    private SpManager() {

    }

    public static SpManager getInstance() {
        return instance;
    }

    public Boolean checkIsLogIn(Context context) {
        return context.getSharedPreferences("UserInfo", MODE_PRIVATE).getBoolean("isLogIn", false);
    }

    public void logIn(Context context, String userID, String nickname) {
        SharedPreferences pref = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean("isLogIn", true);
        editor.putString("userID", userID);
        editor.putString("nickname", nickname);
        editor.commit();
    }

    public String getNickname(Context context) {
        SharedPreferences pref = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
        return pref.getString("nickname", "");
    }

    public void saveNickname(Context context, String nickname) {
        SharedPreferences pref = context.getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("nickname", nickname);
        editor.commit();
    }
}
