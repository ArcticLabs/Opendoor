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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class UserListAdapter extends BaseAdapter implements ListAdapter{
    private ArrayList<User> userList = new ArrayList<>();
    private Context context;

    UserListAdapter(ArrayList<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i){
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup parent) {
        final User user = userList.get(i);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.content_user_list, null);
        }

        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(user.toString());

        ImageButton deleteBtn = (ImageButton) view.findViewById(R.id.delete_btn);
        ImageButton editBtn = (ImageButton) view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(context)
                        .setMessage(
                                context.getString(R.string.user_list_adapter_delete_question) + " " + user.name + context.getString(R.string.user_list_adapter_delete_question_mark)) //NON-NLS
                        .setTitle(context.getString(R.string.user_list_adapter_delete_user));

                deleteBuilder.setPositiveButton(context.getString(R.string.general_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        Database.removeUser(context, user);
                        userList.remove(i);
                        notifyDataSetChanged();
                    }
                });

                deleteBuilder.setNegativeButton(context.getString(R.string.general_no), null);

                AlertDialog deleteDialog = deleteBuilder.create();
                deleteDialog.show();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent userActivity = new Intent(context, UserActivity.class);

                userActivity.putExtra(UserActivity.EDIT_USER_KEY, true);
                userActivity.putExtra(UserActivity.EDIT_USER_NAME_KEY, user.name);
                userActivity.putExtra(UserActivity.EDIT_USER_PASSWORD_KEY, user.password);
                userActivity.putExtra(UserActivity.EDIT_USER_PHONE_KEY, user.number);

                context.startActivity(userActivity);

                notifyDataSetChanged();
            }
        });

        return view;
    }
}