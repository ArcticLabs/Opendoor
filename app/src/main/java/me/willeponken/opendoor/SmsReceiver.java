package me.willeponken.opendoor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                String messageNumber = smsMessage.getOriginatingAddress();

                Log.d(TAG, "SMS received from: " + messageNumber + ", with body: " + messageBody);

                Database.User user = Database.GetUserFromNumber(context, messageNumber);
                if (user != null && user.password.equals(messageBody)) {
                    DoorPhone.dial(context, Database.GetDialNumber(context));
                }
            }
        }
    }
}