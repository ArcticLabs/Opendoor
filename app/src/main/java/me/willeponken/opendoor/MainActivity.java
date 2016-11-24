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

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = DoorPhone.class.getSimpleName();

    private static final int PERMISSION_ALL = 0;
    private static final int PERMISSION_RECEIVE_SMS = 1;
    private static final int PERMISSION_READ_PHONE_STATE = 2;
    private static final int PERMISSION_CALL_PHONE = 3;

    ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent firstTime = new Intent(getApplicationContext(), IntroActivity.class);
        MainActivity.this.startActivity(firstTime);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**if (Database.firstTimeRunning(getApplicationContext())) {
            Database.hasRunFirstTime(getApplicationContext());
            Intent firstTime = new Intent(getApplicationContext(), InitialActivity.class);
            MainActivity.this.startActivity(firstTime);
        }**/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addUserActivity = new Intent(MainActivity.this, NewUserActivity.class);
                MainActivity.this.startActivity(addUserActivity);
            }
        });

        /**requestPermission(new String[]{
                // Check for RECEIVE_SMS permission. This permission is needed for retrieving the latest
                // SMS and reading it checking for a passphrase to call the open telephone number.
                Manifest.permission.RECEIVE_SMS,
                // Check for READ_PHONE_STATE permission. This permission is needed to end the phone call
                // to the door open phone number.
                Manifest.permission.READ_PHONE_STATE,
                // Check for CALL_PHONE. This permission is needed to dial the door open phone number.
                Manifest.permission.CALL_PHONE,
                // Check for READ_CONTACTS. This permission is required for adding a contact using contact view.
                Manifest.permission.READ_CONTACTS
        }, PERMISSION_ALL); **/

        createUserListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void createUserListView() {
        userListView = (ListView)findViewById(R.id.list);
        ArrayList<User> users = Database.getUsers(getApplicationContext());

        if (users != null) {
            ArrayAdapter<User> usersAdapter = new ArrayAdapter<User>(
                    this,
                    android.R.layout.simple_list_item_1,
                    users
            );

            userListView.setAdapter(usersAdapter);
        }
    }

    private void requestPermission(String[] permissions, int code) {
        boolean request = false;

        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "Permission: " + permission + " denied");

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    // TODO(edvinnn): Show permission rationale.
                }

                request = true;
            }
        }

        if (request) {
            ActivityCompat.requestPermissions(this, permissions, code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_RECEIVE_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // TODO(edvinnn) Show dialog that requires the user to call requestPermissions() again.
                }
            }

            case PERMISSION_READ_PHONE_STATE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // TODO(edvinnn) Show dialog that requires the user to call requestPermissions() again.
                }
            }

            case PERMISSION_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // TODO(edvinnn) Show dialog that requires the user to call requestPermissions() again.
                }
            }
        }
    }
}