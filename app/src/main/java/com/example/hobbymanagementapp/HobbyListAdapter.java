package com.example.hobbymanagementapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Calendar;

//Class used to display the list of tasks
public class HobbyListAdapter extends ArrayAdapter<Hobby> {

    private FragmentActivity currentActivity;   //Reference to the fragment that called this
    private HobbyActivity hobbyActivity;          //Reference to the main activity. Used for accessing it's tasks list
    private Context mContext;                   //The current task
    private int mResource;

    public HobbyListAdapter(@NonNull Context context, int resource, ArrayList<Hobby> objects, HobbyActivity _hobbyActivity, FragmentActivity _currentActivity) {
        super(context, resource, objects);

        mContext = context;
        mResource = resource;
        hobbyActivity = _hobbyActivity;
        currentActivity = _currentActivity;
    }

    //This method will be called for each item in the list, in order to display them individually
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Get the properties of the current task in the list
        String name = getItem(position).hobbyName;
        Boolean done = getItem(position).getDone();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        //Get ui references from it's custom view (listview_row)
        TextView text = (TextView) convertView.findViewById(R.id.hobbyText);
        TextView dueDate = (TextView) convertView.findViewById(R.id.hobbyDueDate);
        Switch item = (Switch) convertView.findViewById(R.id.hobbySwitch);

        //Set the ui variables
        text.setText(name);
        item.setChecked(done);
        String dueDateStr = getItem(position).getDueDateString(Calendar.getInstance());
        if (dueDateStr == "")
            dueDate.setText("");
        else
            dueDate.setText("Due: " + dueDateStr);

        convertView.findViewById(R.id.rowItemLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If we clicked on an item's text, then load the second fragment to edit it
                Bundle bundle = new Bundle();
                bundle.putInt("taskId", position);

                SecondFragment fragment = new SecondFragment(hobbyActivity);
                fragment.setArguments(bundle);

                currentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
            }
        });

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If we clicked on the switch of the item
                int index = position;

                if (HobbyActivity.filterOptions == FilterOptions.DisplayNotComplete) {
                    //If we are in display only not complete, then position won't be the position of the task in hobbies, so we need to find out it's position there in order to edit it
                    index = 0;
                    int found = 0;
                    while (found != position + 1) {
                        if (hobbyActivity.hobbies.get(index).getDone() == false)
                            found++;
                        index++;
                    }
                    index--;
                }

                //Mark the task as done/not done based on the switch's value
                hobbyActivity.hobbies.get(index).setDone(item.isChecked());

                //When we set a task to due, refresh tasks, because if we are filtering based on due, it will need to disappear
                FirstFragment fragment = new FirstFragment(hobbyActivity);
                currentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
            }
        });

        //Color the background of the items based on their score
        if (getItem(position).hobbyScore == 0) {
            item.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_failed));
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_failed));
            dueDate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_failed));
        } else if (getItem(position).hobbyScore < 6) {
            item.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_veryBad));
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_veryBad));
            dueDate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_veryBad));
        } else if (getItem(position).hobbyScore < 11) {
            item.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_bad));
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_bad));
            dueDate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_bad));
        } else if (getItem(position).hobbyScore < 16) {
            item.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_normal));
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_normal));
            dueDate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_normal));
        } else if (getItem(position).hobbyScore < 21) {
            item.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_good));
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_good));
            dueDate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_good));
        } else if (getItem(position).hobbyScore < 26) {
            item.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_veryGood));
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_veryGood));
            dueDate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_veryGood));
        } else if (getItem(position).hobbyScore >= 26) {
            item.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_perfect));
            text.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_perfect));
            dueDate.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.hobby_perfect));
        }

        return convertView;
    }
}
