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

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class AboutActivity extends AppCompatActivity {

    private int onClickCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView aboutTitle = (TextView)findViewById(R.id.textViewAboutTitle);
        aboutTitle.setText(getString(R.string.general_app_name) + " " + BuildConfig.VERSION_NAME);
        aboutTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCount++;
                if (onClickCount == 5){
                    onClickCount = 0;
                    playEpicSaxGuy();
                }
            }
        });

        TextView licenseText = (TextView)findViewById(R.id.textViewAboutLicense);
        try {
            Resources resources = this.getResources();
            InputStream reader = resources.openRawResource(R.raw.license);

            byte[] bytes = new byte[reader.available()];
            reader.read(bytes);
            licenseText.setText(new String(bytes));
        } catch (IOException e) {
            licenseText.setText(getString(R.string.about_activity_error_no_license));
        }
    }

    private void playEpicSaxGuy(){
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.shortsaxguy);
        mediaPlayer.start();
    }
}