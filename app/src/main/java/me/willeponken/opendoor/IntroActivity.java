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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;

public class IntroActivity extends AppIntro2 {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Intro slides
        addSlide(FirstRunSlide.newInstance(R.layout.fragment_initial0));
        addSlide(FirstRunSlide.newInstance(R.layout.fragment_initial1));

        // Hide Skip/Done button
        showSkipButton(false);
        setProgressButtonEnabled(true);

        // Turn off vibration
        setVibrate(false);

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        EditText editText_default = (EditText)findViewById(R.id.defaultNumber);
        if (setNumber(editText_default)){
            Database.setDialNumber(getApplicationContext(), editText_default.getText().toString());
            finish();
            //Intent continueToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
            //IntroActivity.this.startActivity(continueToMainActivity);
        }
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    private boolean setNumber(EditText editText){
        if (editText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else return true;
    }
}