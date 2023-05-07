package com.example.hobbymanagementapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class SecondFragment extends Fragment {

    int selectedDropdownItem = 0;   //The item from the repeatType dropdown
    int taskId = 0;                 //The currently selected task. If it is a new task, it will be -1, otherwise it will be the index of the task on mainActivity.hobbies.

    //Reference to the main activity in order to access it's tasks
    HobbyActivity hobbyActivity;

    //Reference to ui elements
    Spinner dropdown;
    EditText taskTitle;
    EditText startDateText;

    LinearLayout weekDaysLayout;
    Switch mondaySwitch;
    Switch tuesdaySwitch;
    Switch wednesdaySwitch;
    Switch thursdaySwitch;
    Switch fridaySwitch;
    Switch saturdaySwitch;
    Switch sundaySwitch;

    //Reference to date properties
    MyDate startDate;
    DatePickerDialog datePicker;

    public SecondFragment(HobbyActivity _hobbyActivity){
        hobbyActivity = _hobbyActivity;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //Receive the data from the previous fragment, in order to know what task we need to edit/add
        Bundle bundle = this.getArguments();
        taskId = bundle.getInt("taskId");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get and initialize dropdown ui
        dropdown = (Spinner)view.findViewById(R.id.repeatDropdown);
        ArrayAdapter dropdownOptions = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, RepeatType.values());
        dropdown.setAdapter(dropdownOptions);

        //Get ui references
        taskTitle = (EditText)view.findViewById(R.id.taskTitle);
        startDateText = (EditText)view.findViewById(R.id.startDateInput);

        weekDaysLayout = (LinearLayout)view.findViewById(R.id.weekDaysLayout);
        mondaySwitch = (Switch)view.findViewById(R.id.mondaySwitch);
        tuesdaySwitch = (Switch)view.findViewById(R.id.tuesdaySwitch);
        wednesdaySwitch = (Switch)view.findViewById(R.id.wednesdaySwitch);
        thursdaySwitch = (Switch)view.findViewById(R.id.thursdaySwitch);
        fridaySwitch = (Switch)view.findViewById(R.id.fridaySwitch);
        saturdaySwitch = (Switch)view.findViewById(R.id.saturdaySwitch);
        sundaySwitch = (Switch)view.findViewById(R.id.sundaySwitch);

        if(taskId != -1) {
            //If we are editing an existing task, then update the ui to suit the given task
            taskTitle.setText(hobbyActivity.hobbies.get(taskId).hobbyName, TextView.BufferType.EDITABLE);
            dropdown.setSelection(hobbyActivity.hobbies.get(taskId).repeatType.ordinal());
            startDate = hobbyActivity.hobbies.get(taskId).startDate.clone();
            startDateText.setText(startDate.toString());

            //If we are in repeat weekly, display the switches for week days, other modes don't need them, so they will be hidden.
            if(hobbyActivity.hobbies.get(taskId).repeatType == RepeatType.Weekly)
                weekDaysLayout.setVisibility(View.VISIBLE);
            else
                weekDaysLayout.setVisibility(View.GONE);

            //Initialize the switches.
            mondaySwitch.setChecked(hobbyActivity.hobbies.get(taskId).weekDays[1]);
            tuesdaySwitch.setChecked(hobbyActivity.hobbies.get(taskId).weekDays[2]);
            wednesdaySwitch.setChecked(hobbyActivity.hobbies.get(taskId).weekDays[3]);
            thursdaySwitch.setChecked(hobbyActivity.hobbies.get(taskId).weekDays[4]);
            fridaySwitch.setChecked(hobbyActivity.hobbies.get(taskId).weekDays[5]);
            saturdaySwitch.setChecked(hobbyActivity.hobbies.get(taskId).weekDays[6]);
            sundaySwitch.setChecked(hobbyActivity.hobbies.get(taskId).weekDays[0]);
        }
        else{
            //If we are adding a new task, the initialize the ui elements to suit it
            taskTitle.setText("Task Title", TextView.BufferType.NORMAL);
            weekDaysLayout.setVisibility(View.GONE);
            startDate = new MyDate(Calendar.getInstance());
        }

        //-----------------------------------------------------------------------------------Back btn
        view.findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go back to the first task, which will display all tasks
                FirstFragment fragment = new FirstFragment(hobbyActivity);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
            }
        });

        //-----------------------------------------------------------------------------------Delete
        view.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If we clicked delete, then delete the task and return to the first fragment
                if(taskId != -1)
                    hobbyActivity.hobbies.remove(taskId);
                FirstFragment fragment = new FirstFragment(hobbyActivity);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
            }
        });

        //-----------------------------------------------------------------------------------Save
        view.findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save the modifications to the task
                String hobbyName = taskTitle.getText().toString();
                RepeatType repeatType = RepeatType.values()[selectedDropdownItem];
                Boolean[] weekDays = new Boolean[7];
                //The days of the week start from Sunday(0) to Saturday(6)
                weekDays[1] = mondaySwitch.isChecked();
                weekDays[2] = tuesdaySwitch.isChecked();
                weekDays[3] = wednesdaySwitch.isChecked();
                weekDays[4] = thursdaySwitch.isChecked();
                weekDays[5] = fridaySwitch.isChecked();
                weekDays[6] = saturdaySwitch.isChecked();
                weekDays[0] = sundaySwitch.isChecked();

                if(taskId == -1) {
                    //If it is a new task, then add it to the list
                    hobbyActivity.hobbies.add(new Hobby(hobbyName, startDate, weekDays, repeatType, 13));
                }
                else{
                    //Otherwise, edit an existing task
                    hobbyActivity.hobbies.get(taskId).hobbyName = hobbyName;
                    hobbyActivity.hobbies.get(taskId).repeatType = repeatType;
                    hobbyActivity.hobbies.get(taskId).startDate = startDate.clone();
                    hobbyActivity.hobbies.get(taskId).weekDays = weekDays.clone();
                }
                //Go back to the first fragment to view all tasks
                FirstFragment fragment = new FirstFragment(hobbyActivity);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
            }
        });

        //-----------------------------------------------------------------------------------Repeat Dropdown
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Remember the selected repeat type and display/hide the week days based on selection
                selectedDropdownItem = position;
                if(selectedDropdownItem == 1)
                    weekDaysLayout.setVisibility(View.VISIBLE);
                else
                    weekDaysLayout.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //-----------------------------------------------------------------------------------Date Picker
        view.findViewById(R.id.pickDateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display a date picker in order to pick the start date for the task
                datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int mYear, int mMonth, int mDayOfMonth) {
                        startDateText.setText(mDayOfMonth + "." + (mMonth + 1) + "." + mYear);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(mYear, mMonth, mDayOfMonth);
                        //Once we selected a date, remember it
                        startDate = new MyDate(calendar);
                    }
                }, startDate.getYear(), startDate.getMonth(), startDate.getDay());
                datePicker.show();
            }
        });

        //-----------------------------------------------------------------------------------Reset Progress Button
        view.findViewById(R.id.resetProgressBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskId != -1) {
                    //If we chose to reset progress, the reset the hobbyScore to it's default value
                    hobbyActivity.hobbies.get(taskId).hobbyScore = hobbyActivity.hobbies.get(taskId).defaultScore;
                    FirstFragment fragment = new FirstFragment(hobbyActivity);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
                }
            }
        });
    }
}