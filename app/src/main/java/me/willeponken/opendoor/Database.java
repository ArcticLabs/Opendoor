package me.willeponken.opendoor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.ListIterator;

class Database {

    private static final String SETTINGS_FILE_KEY = "settings";

    private static final String SETTINGS_KEY_DIAL_NUMBER = "settings_dial_number";
    private static final String SETTINGS_DEFAULT_DIAL_NUMBER = "+46920420062"; //TODO(willeponken): Change this to something sane

    private static final String SETTINGS_KEY_USERS = "settings_users";
    private static final String SETTINGS_DEFAULT_USERS = "";


    private static SharedPreferences getSettings(Context context) {
        return context.getSharedPreferences(
                SETTINGS_FILE_KEY, Context.MODE_PRIVATE);
    }

    @Nullable
    static ArrayList<User> getUsers(Context context) {
        String encodedObj = getSettings(context)
                .getString(SETTINGS_KEY_USERS, SETTINGS_DEFAULT_USERS);

        if (encodedObj.equals(SETTINGS_DEFAULT_USERS)) return null;

        return new Gson().fromJson(encodedObj, new TypeToken<ArrayList<User>>(){}.getType());
    }

    @Nullable
    static User getUserFromNumber(Context context, String number) {
        ArrayList<User> users = getUsers(context);

        if (users != null) {
            for (User user : users) {
                if (user.number.equals(number)) {
                    return user;
                }
            }
        }

        return null;
    }

    static void addUser(Context context, User user) {
        ArrayList<User> users = getUsers(context);

        boolean found = false;
        if (users != null) {
            ListIterator<User> iterator;
            iterator = users.listIterator();
            User current;
            while (iterator.hasNext()) {
                current = iterator.next();
                if (current.number.equals(user.number)) {
                    iterator.set(user); // Overwrite duplicate user
                    found = true;
                }
            }
        } else {
            users = new ArrayList<User>(); // No users, add empty list
        }

        if (!found) {
            users.add(user);
        }

        SharedPreferences.Editor e = getSettings(context).edit();
        e.putString(SETTINGS_KEY_USERS, new Gson().toJson(users));
        e.apply();
    }

    static String getDialNumber(Context context) {
        return getSettings(context)
                .getString(SETTINGS_KEY_DIAL_NUMBER, SETTINGS_DEFAULT_DIAL_NUMBER);
    }

    static void setDialNumber(Context context, String number) {
        SharedPreferences.Editor e = getSettings(context).edit();
        e.putString(SETTINGS_KEY_DIAL_NUMBER, number);
        e.apply();
    }
}