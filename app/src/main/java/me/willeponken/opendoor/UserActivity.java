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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    static public final int CONTACT = 0;

    static final String EDIT_USER_NAME_KEY = "edit_user_name"; //NON-NLS
    static final String EDIT_USER_PHONE_KEY = "edit_user_phone"; //NON-NLS
    static final String EDIT_USER_PASSWORD_KEY = "edit_user_password"; //NON-NLS
    static final String EDIT_USER_KEY = "edit_user"; //NON-NLS

    EditText nameInput;
    EditText phoneInput;
    EditText passwordInput;

    FloatingActionButton fab;
    Button contactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        nameInput = (EditText)findViewById(R.id.editText);
        phoneInput = (EditText)findViewById(R.id.editText2);
        passwordInput = (EditText)findViewById(R.id.editText3);

        // Retrieve data from parent activity if edit mode
        Bundle stateExtras = getIntent().getExtras();
        if (stateExtras != null) {
            if (stateExtras.getBoolean(EDIT_USER_KEY, false)) {
                setTitle(getString(R.string.user_activity_title_edit_user));
                nameInput.setText(stateExtras.getString(EDIT_USER_NAME_KEY, ""));
                phoneInput.setText(stateExtras.getString(EDIT_USER_PHONE_KEY, ""));
                passwordInput.setText(stateExtras.getString(EDIT_USER_PASSWORD_KEY, ""));
            }
        }

        fab = (FloatingActionButton) findViewById(R.id.fabSave);
        contactBtn = (Button)findViewById(R.id.contactButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nameInput.getText().length() == 0 || phoneInput.getText().length() == 0 || passwordInput.getText().length() == 0){

                    // Build toast
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence errorMsg = getString(R.string.user_activity_error_incorrect_input);

                    // Show toast
                    Toast toast = Toast.makeText(context, errorMsg, duration);
                    toast.show();

                } else {

                    String name        = nameInput.getText().toString();
                    String phone       = phoneInput.getText().toString();
                    String password    = passwordInput.getText().toString();

                    Database.addUser(getApplicationContext(), new User(name, phone, password, true, true));

                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);

                }

            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, CONTACT);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    String contactName = "";
                    String phoneNumber = "";
                    String contactId = null;

                    Cursor contactCursor = getContentResolver().query(data.getData(), null,null,null,null);
                    if(contactCursor != null && contactCursor.moveToFirst()) {
                        contactName = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        contactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
                    }
                    contactCursor.close();

                    if (contactId != null) {
                        Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{ contactId }, null);
                        if (phoneCursor != null && phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                        }
                        phoneCursor.close();
                    }
                    nameInput.setText(contactName);
                    phoneInput.setText(phoneNumber);
                }
        }
    }
}