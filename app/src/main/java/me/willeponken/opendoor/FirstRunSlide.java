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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class FirstRunSlide extends Fragment implements AdapterView.OnItemSelectedListener{

    private static final String ARG_LAYOUT_RES_ID = "layoutResId"; //NON-NLS
    private int layoutResId;
    EditText editText;

    public static FirstRunSlide newInstance(int layoutResId) {
        FirstRunSlide firstRunSlide = new FirstRunSlide();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        firstRunSlide.setArguments(args);

        return firstRunSlide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layoutResId, container, false);
        if (R.layout.fragment_initial1 == layoutResId){
            editText = (EditText) v.findViewById(R.id.defaultNumber);
            Spinner spinner = (Spinner) v.findViewById(R.id.lab_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.lab_spinner_titles, R.layout.custom_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }
        return  v;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String[] spinnerTitles = getResources().getStringArray(R.array.lab_spinner_titles);
        String[] spinnerValues = getResources().getStringArray(R.array.lab_spinner_values);

        if (position < spinnerTitles.length-1 && position >= 0) {
            editText.setVisibility(View.INVISIBLE);
            editText.setText(spinnerValues[position]);
        } else {
            editText.setText("");
            editText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
