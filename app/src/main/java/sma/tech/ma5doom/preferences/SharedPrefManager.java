package sma.tech.ma5doom.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;

import sma.tech.ma5doom.networkController.model.login.User;


public class SharedPrefManager {
    final static String SHARED_PREF_NAME = "shared_pref";

    final static String USER_DATA = "any_advertisement_user_data";
    final static String REMIND_ME = "remind_me";



    final static String App_LANGUAGE = "app_language";

    Context mContext;


    public SharedPrefManager(Context mContext) {
        this.mContext = mContext;
    }



    public void setUserDate(User userModel) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_DATA, gson.toJson(userModel));
        editor.commit();
        editor.apply();
    }


    public void setRemindMe(int num) {
        Toast.makeText(mContext,"set"+num,Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(REMIND_ME, num);
        editor.commit();
        editor.apply();
    }


    public int getRemindMe() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREF_NAME,0);
        int num=sharedPreferences.getInt(REMIND_ME,5);
        Toast.makeText(mContext,"get"+num,Toast.LENGTH_SHORT).show();

        return num;
    }

    public User getUserDate() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(USER_DATA, null), User.class);
    }





    public String getAppLanguage() {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                SHARED_PREF_NAME, 0);
        String value = sharedPreferences.getString(App_LANGUAGE, "ar");
        return value;
    }

    public void setAppLanguage(String language) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME,
                0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(App_LANGUAGE, language);
        editor.commit();
    }


    public void Logout() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
