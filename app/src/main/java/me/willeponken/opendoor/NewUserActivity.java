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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends AppCompatActivity {

    EditText name_input;
    EditText phone_input;
    EditText password_input;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        name_input        = (EditText)findViewById(R.id.editText);
        phone_input       = (EditText)findViewById(R.id.editText2);
        password_input    = (EditText)findViewById(R.id.editText3);

        fab = (FloatingActionButton) findViewById(R.id.fabSave);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name_input.getText().length() == 0 || phone_input.getText().length() == 0 || password_input.getText().length() == 0){

                    // Build toast
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;
                    CharSequence errorMsg = "Incorrect input. Please enter all fields.";

                    // Show toast
                    Toast toast = Toast.makeText(context, errorMsg, duration);
                    toast.show();
                } else {

                    String name        = name_input.getText().toString();
                    String phone       = phone_input.getText().toString();
                    String password    = password_input.getText().toString();

                    Database.addUser(getApplicationContext(), new User(name, phone, password, true, true));

                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mainActivity);

                }

            }
        });

    }
}