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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String body = smsMessage.getMessageBody();
                String number = smsMessage.getOriginatingAddress();

                Log.d(TAG, "SMS received from: " + number + ", with body: " + body); //NON-NLS NON-NLS

                if (!Database.isGlobalBlock(context) && validUserCredentials(context, number, body)) {
                    DoorPhone.dial(context, Database.getDialNumber(context));
                }
            }
        }
    }

    private boolean validUserCredentials(Context context, String number, String body) {
        User user = Database.getUserFromNumber(context, PhoneNumberUtils.normalizeNumber(number));

        if (user != null && user.active) {
            String providedPassword = body.trim();
            String userPassword = user.password.trim();

            if ((user.caseSensitive && userPassword.equalsIgnoreCase(providedPassword))
                    || userPassword.equals(providedPassword)) {
                return true;
            }
        }

        return false;
    }
}