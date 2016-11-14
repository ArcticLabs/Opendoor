package me.willeponken.opendoor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.ListIterator;

public class Database {

    private static final String SETTINGS_FILE_KEY = "settings";

    private static final String SETTINGS_KEY_DIAL_NUMBER = "settings_dial_number";
    private static final String SETTINGS_DEFAULT_DIAL_NUMBER = "+46920420062"; //TODO(willeponken): Change this to something sane

    private static final String SETTINGS_KEY_USERS = "settings_users";
    private static final String SETTINGS_DEFAULT_USERS = "";


    private static SharedPreferences getSettings(Context context) {
        return context.getSharedPreferences(
                SETTINGS_FILE_KEY, Context.MODE_PRIVATE);
    }

    class User {
        String name;
        String number;
        String password;

        public User(String name, String number, String password) {
            this.name = name;
            this.number = number;
            this.password = password;
        }
    }

    class Users {
        private ArrayList<User> userList;
        private ArrayList<User> getUserList() {
            return userList;
        }
    }

    @Nullable
    static ArrayList<User> GetUsers(Context context) {
        ArrayList<User> users;

        String encodedObj = getSettings(context)
                .getString(SETTINGS_KEY_USERS, SETTINGS_DEFAULT_USERS);

        if (encodedObj.equals(SETTINGS_DEFAULT_USERS)) return null;

        return new Gson().fromJson(encodedObj, Users.class).getUserList();
    }

    @Nullable
    static User GetUserFromNumber(Context context, String number) {
        ArrayList<User> users = GetUsers(context);

        for (User user: users) {
            if (user.number.equals(number)) {
                return user;
            }
        }

        return null;
    }

    static void AddUser(Context context, User user) {
        ArrayList<User> users = GetUsers(context);

        ListIterator<User> iterator = users.listIterator();
        User current;
        boolean found = false;
        while(iterator.hasNext()) {
            current = iterator.next();
            if (current.number.equals(user.number)) {
                iterator.set(user); // Overwrite duplicate user
                found = true;
            }
        }

        if (!found) {
            users.add(user);
        }

        SharedPreferences.Editor e = getSettings(context).edit();
        e.putString(SETTINGS_KEY_USERS, new Gson().toJson(users));
        e.apply();
    }

    static String GetDialNumber(Context context) {
        return getSettings(context)
                .getString(SETTINGS_KEY_DIAL_NUMBER, SETTINGS_DEFAULT_DIAL_NUMBER);
    }

    static void SetDialNumber(Context context, String number) {
        SharedPreferences.Editor e = getSettings(context).edit();
        e.putString(SETTINGS_KEY_DIAL_NUMBER, number);
        e.apply();
    }
}