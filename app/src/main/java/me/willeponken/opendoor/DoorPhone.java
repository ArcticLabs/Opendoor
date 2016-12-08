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
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

class DoorPhone {
    private static final String TAG = DoorPhone.class.getSimpleName();

    static void dial(final Context context, String number) {
        number = PhoneNumberUtils.normalizeNumber(number);

        Log.d(TAG, "Dialing phone number: " + number);

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Required for Android below v7
        callIntent.setData(Uri.parse("tel:" + number));

        try {
            context.startActivity(callIntent); // Call dial number
        } catch (SecurityException e) {
            Log.e(TAG, e.toString());
        }
    }
}
