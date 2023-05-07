package com.example.hobbymanagementapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.hobbymanagementapp.HobbyActivity;
import com.example.hobbymanagementapp.R;
import com.example.hobbymanagementapp.SortType;

//Class that manages properties for the sorting options
public class SortOptionsActivity extends Activity {

    //Reference to ui elements
    RadioButton sortByNameBtn;
    RadioButton sortByColorBtn;
    RadioButton sortByDueDateBtn;
    RadioGroup radioGroup;
    Switch sortAscendingSwitch;

    //On create is called when the activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_options);

        //Scale the window to not fill the entire screen and place it in the center
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels * 0.8f), (int)(dm.heightPixels * 0.7f));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        //Find ui references
        sortByNameBtn = findViewById(R.id.sortByNameBtn);
        sortByColorBtn = findViewById(R.id.sortByColorBtn);
        sortByDueDateBtn = findViewById(R.id.sortByDueDate);
        radioGroup = findViewById(R.id.sortRadioGroup);
        sortAscendingSwitch = findViewById(R.id.sortAscSwitch);

        //Set the default values of ui elements based on what is the current setting for them
        switch (HobbyActivity.sortType){
            case Name:
                radioGroup.check(R.id.sortByNameBtn);
                break;
            case Color:
                radioGroup.check(R.id.sortByColorBtn);
                break;
            case DueDate:
                radioGroup.check(R.id.sortByDueDate);
                break;
        }
        sortAscendingSwitch.setChecked(HobbyActivity.sortAscending);

        //Click listeners that will change the settings based on what is clicked
        sortByNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HobbyActivity.sortType = SortType.Name;
            }
        });
        sortByColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HobbyActivity.sortType = SortType.Color;
            }
        });
        sortByDueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HobbyActivity.sortType = SortType.DueDate;
            }
        });
        sortAscendingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HobbyActivity.sortAscending = sortAscendingSwitch.isChecked();
            }
        });

        //When we click on the close button, end the current activity so we can go back to the previous activity
        findViewById(R.id.sortCloseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}