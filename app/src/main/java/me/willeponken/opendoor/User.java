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

import android.telephony.PhoneNumberUtils;

class User {
    String name;
    String number;
    String password;
    boolean active = true;
    boolean caseSensitive = false;

    User(String name, String number, String password, boolean active, boolean caseSensitive) {
        this.name = name;
        this.number = PhoneNumberUtils.normalizeNumber(number);
        this.password = password;
        this.active = active;
        this.caseSensitive = caseSensitive;
    }

    boolean toggleActive() {
        this.active = !this.active;
        return this.active;
    }

    boolean toggleCaseSensitive() {
        this.caseSensitive = !this.caseSensitive;
        return this.caseSensitive;
    }

    public String toString() {
        return String.format("%s: %s", name, password); //NON-NLS
    }
}