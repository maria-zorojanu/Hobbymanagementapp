package com.example.hobbymanagementapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Calendar;

public class HobbyActivity extends AppCompatActivity {

    public static FilterOptions filterOptions = FilterOptions.DisplayAll;   //The way we are filtering tasks
    public static SortType sortType = SortType.Name;                        //The way we are sorting tasks
    public static boolean sortAscending = true;                             //True - sort ascending; False - sort descending

    private DataSaver dataSaver;    //Class that will handle saving and loading data for us

    HobbyActivity hobbyActivity = this;   //Reference to this script. Needed because some methods need a reference to this script, but they are called from onClick listeners, and you can't get the reference there.
    public ArrayList<Hobby> hobbies = new ArrayList<>();    //List that will hold all tasks in the app. It is accessed by multiple scripts.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        MyDate currentDate = new MyDate(Calendar.getInstance());    //Find the date of today
        dataSaver = new DataSaver(getSharedPreferences("shared preferences", MODE_PRIVATE));    //Initialize the data saver
        hobbies = dataSaver.LoadData(); //Load the data from disk (if there is any saved data)
        //For each hobby, update the hobby. Some days may have passed since we last opened the app, so the score needs to decrease if we didn't do the tasks with deadline.
        for(int index = 0; index < hobbies.size(); index++){
            hobbies.get(index).updateHobby(currentDate);
        }

        //Load the first fragment, that is responsible for drawing the tasks
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, new FirstFragment(this)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //This method will process click events on the items from the toolbar dropdown.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //If we clicked reset all progress, it will show a popup message first
        if (id == R.id.resetAllProgress) {
            new AlertDialog.Builder(this).setTitle("Reset All Progress").setMessage("Are you sure you want to reset progress for all tasks?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //If we clicked "ok" in the popup, reset all hobbies to their default scores
                            for(int index = 0; index < hobbies.size(); index++)
                                hobbies.get(index).hobbyScore = hobbies.get(index).defaultScore;

                            //Load the first frame again, in order to update the ui for the tasks
                            getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, new FirstFragment(hobbyActivity)).commit();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        if(id == R.id.filterOption){
            //If we chose the filter item, then load it's popup
            Intent intent = new Intent(getApplicationContext(), FilterOptionsActivity.class);
            startActivity(intent);
        }
        if(id == R.id.sortOption){
            //If we chose the sort item, then load it's popup
            Intent intent = new Intent(getApplicationContext(), SortOptionsActivity.class);
            startActivity(intent);
        }
        if(id == R.id.resetEverything){
            //If we chose to reset everything, then show a popup first
            new AlertDialog.Builder(this).setTitle("Reset Everything").setMessage("This will delete all tasks and save files, are you sure?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //If we clicked "ok", then delete all hobbies, clear shared prefs and reload the first fragment, to update the ui.
                            hobbies.clear();
                            dataSaver.ClearSharedPrefs();
                            getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, new FirstFragment(hobbyActivity)).commit();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        if(id == R.id.insertDemoTasks){
            //If we chose to insert demo tasks, then add a couple of tasks that we can play with
            Boolean[] dayInit = new Boolean[7];
            for(int index = 0; index < dayInit.length; index++)
                dayInit[index] = false;

            MyDate date = new MyDate(Calendar.getInstance());

            hobbies.add(new Hobby("First", date, dayInit, RepeatType.None, 0));
            date.decrement();
            date.decrement();
            date.decrement();
            hobbies.add(new Hobby("Second", date, dayInit, RepeatType.None, 5));
            date.increment();
            hobbies.add(new Hobby("Third", date, dayInit, RepeatType.None, 10));
            date.increment();
            hobbies.add(new Hobby("asd", date, dayInit, RepeatType.None, 15));
            date.increment();
            date.increment();
            date.increment();
            hobbies.add(new Hobby("asdf", date, dayInit, RepeatType.None, 20));
            date.increment();
            hobbies.add(new Hobby("aadf", date, dayInit, RepeatType.None, 25));
            date.increment();
            hobbies.add(new Hobby("zxcvzxczxczx", date, dayInit, RepeatType.None, 30));

            //Load the first fragment in order to update the ui
            getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, new FirstFragment(this)).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    //Will be called when the app resumes, or control is given back to this activity (when the popup activities call finish())
    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction().add(R.id.frameContainer, new FirstFragment(this)).commit();
    }

    //When the app pauses or closes, save the data
    @Override
    protected void onPause() {
        super.onPause();
        dataSaver.SaveData(hobbies);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dataSaver.SaveData(hobbies);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataSaver.SaveData(hobbies);
    }
}