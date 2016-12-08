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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public class PhoneReceiver extends BroadcastReceiver {
    private static final String TAG = PhoneReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        if (tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK &&
                PhoneNumberUtils.compare(Database.getDialNumber(context), phoneNumber)) {
            Log.d(TAG, "Door dial number found: " + phoneNumber + ", sleeping..."); //NON-NLS NON-NLS
            endCallSleeperThread(context);
        }
    }

    // endCallSleeperThread wait for defined number of seconds before ending the call.
    // BUG(willeponken):
    // This can be potentially dangerous if the user hangs up and tries to call another
    // number, in this case the other phone call will be hanged up.
    private static void endCallSleeperThread(final Context context) {
        final int sleepMilliseconds = Database.getSleepTime(context) * 1000;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(sleepMilliseconds);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                endCall(context);
            }
        }).start();
    }

    // endCall ends the current phone call. It does this by reflecting the methods in TelephonyManager
    // to extract the endCall() method in the ITelephony interface.
    // Required permissions: CALL_PHONE, READ_PHONE_STATE
    private static void endCall(Context context) {
        Log.d(TAG, "Ending current call"); //NON-NLS

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Reflect getITelephony() method from TelephonyManager
            Class<?> classTelephony = Class.forName(tm.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony"); //NON-NLS
            methodGetITelephony.setAccessible(true); // Ignore it's private

            // Reflect ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(tm);

            // Reflect endCall() method from ITelephony
            Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall"); //NON-NLS

            // End the phone call
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}

