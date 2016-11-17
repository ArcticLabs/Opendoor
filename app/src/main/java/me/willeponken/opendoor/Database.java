/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package me.willeponken.opendoor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.ListIterator;

class Database {

    private static final String TAG = Database.class.getSimpleName();

    private static final String SETTINGS_FILE_KEY = "settings";

    private static final String SETTINGS_KEY_DIAL_NUMBER = "settings_dial_number";
    private static final String SETTINGS_DEFAULT_DIAL_NUMBER = "";

    private static final String SETTINGS_KEY_USERS = "settings_users";
    private static final String SETTINGS_DEFAULT_USERS = "";

    private static final String SETTINGS_KEY_FIRST_TIME_RUNNING = "settings_first_time_running";
    private static final boolean SETTINGS_DEFAULT_FIRST_TIME_RUNNING = true;

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

    @Nullable
    private static ListIterator<User> getUserPosition(Context context, User user) {
        ArrayList<User> users = getUsers(context);

        if (users != null) {
            ListIterator<User> iterator;
            iterator = users.listIterator();
            while (iterator.hasNext()) {
                if (iterator.next().number.equals(user.number)) {
                    return iterator;
                }
            }
        }

        return null;
    }

    static void addUser(Context context, User user) {
        ArrayList<User> users = getUsers(context);

        if (users != null) {
            ListIterator<User> iterator = getUserPosition(context, user);
            if (iterator != null) {
                users.set(iterator.nextIndex()-1, user);
            } else {
                users.add(user); // No such user, add new
            }
        } else {
            users = new ArrayList<>(); // No users, add empty list
            users.add(user);
        }

        getSettings(context).edit()
                .putString(SETTINGS_KEY_USERS, new Gson().toJson(users))
                .apply();
    }

    static void replaceUser(Context context, User oldUser, User newUser) {
        ArrayList<User> users = getUsers(context);

        if (users != null) {
            ListIterator<User> iterator = getUserPosition(context, oldUser);
            if (iterator != null) {
                users.set(iterator.nextIndex()-1, newUser);

                getSettings(context).edit()
                        .putString(SETTINGS_KEY_USERS, new Gson().toJson(users))
                        .apply();
            }
        }
    }

    static String getDialNumber(Context context) {
        return getSettings(context)
                .getString(SETTINGS_KEY_DIAL_NUMBER, SETTINGS_DEFAULT_DIAL_NUMBER);
    }

    static void setDialNumber(Context context, String number) {
        getSettings(context).edit()
                .putString(SETTINGS_KEY_DIAL_NUMBER, number)
                .apply();
    }

    static boolean firstTimeRunning(Context context) {
        return getSettings(context)
                .getBoolean(SETTINGS_KEY_FIRST_TIME_RUNNING, SETTINGS_DEFAULT_FIRST_TIME_RUNNING);
    }

    static void hasRunFirstTime(Context context) {
        getSettings(context).edit()
                .putBoolean(SETTINGS_KEY_FIRST_TIME_RUNNING, false)
                .apply();
    }
}