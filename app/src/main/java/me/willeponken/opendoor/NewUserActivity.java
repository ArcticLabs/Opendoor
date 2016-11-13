package me.willeponken.opendoor;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
                String name        = name_input.getText().toString();
                String phone       = phone_input.getText().toString();
                String password    = password_input.getText().toString();

                String[] userArray = new String[] {name, phone, password};

                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                mainActivity.putExtra("newUserData", userArray);
                startActivity(mainActivity);

            }
        });

    }
}