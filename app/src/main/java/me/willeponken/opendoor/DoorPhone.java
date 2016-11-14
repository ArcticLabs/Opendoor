package me.willeponken.opendoor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

class DoorPhone {
    private static final String TAG = DoorPhone.class.getSimpleName();

    static void dial(final Context context, final String number) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Dialing phone number: " + number);

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Required for Android below v7
                callIntent.setData(Uri.parse("tel:" + number));

                try {
                    context.startActivity(callIntent);
                } catch (SecurityException e) {
                    Log.e(TAG, e.toString());
                }

                try {
                    Thread.sleep(15000);
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
        Log.d(TAG, "Ending current call");

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Reflect getITelephony() method from TelephonyManager
            Class<?> classTelephony = Class.forName(tm.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
            methodGetITelephony.setAccessible(true); // Ignore it's private

            // Reflect ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(tm);

            // Reflect endCall() method from ITelephony
            Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // End the phone call
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
