package com.example.hobbymanagementapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.hobbymanagementapp.R;
import com.example.hobbymanagementapp.FilterOptions;

//Class that processes the filter options for the tasks
public class FilterOptionsActivity extends Activity {

    //Reference to ui elements
    RadioButton displayAllBtn;
    RadioButton displayDueBtn;
    RadioButton displayNotCompleteBtn;
    RadioGroup radioGroup;

    //On create is called when the activity starts
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_options);

        //Scale the window to not fill the entire screen and place it in the center
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        getWindow().setLayout((int)(dm.widthPixels * 0.8f), (int)(dm.heightPixels * 0.7f));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        //Find ui references
        radioGroup = findViewById(R.id.filterRadioGroup);
        displayAllBtn = findViewById(R.id.displayAllBtn);
        displayDueBtn = findViewById(R.id.displayDueBtn);
        displayNotCompleteBtn = findViewById(R.id.displayNotCompleteBtn);

        //Based on the currently selected filter option, set the default selected radio button
        switch (HobbyActivity.filterOptions){
            case DisplayAll:
                radioGroup.check(R.id.displayAllBtn);
                break;
            case DisplayDue:
                radioGroup.check(R.id.displayDueBtn);
                break;
            case DisplayNotComplete:
                radioGroup.check(R.id.displayNotCompleteBtn);
                break;
        }

        //On click listeners for changing the filter option based on which button is pressed
        displayAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HobbyActivity.filterOptions = FilterOptions.DisplayAll;
            }
        });
        displayDueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HobbyActivity.filterOptions = FilterOptions.DisplayDue;
            }
        });
        displayNotCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HobbyActivity.filterOptions = FilterOptions.DisplayNotComplete;
            }
        });

        //When we click on the close button, end the current activity so we can go back to the previous activity
        findViewById(R.id.filterCloseBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}