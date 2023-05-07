package com.example.hobbymanagementapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class FirstFragment extends Fragment {

    //Reference to ui elements
    ListView tasksListView;
    HobbyListAdapter tasksAdapter;

    //Reference to the main activity in order to access it's hobbies.
    HobbyActivity hobbyActivity;

    public FirstFragment(HobbyActivity _hobbyActivity){
        hobbyActivity = _hobbyActivity;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Find the ui references and display the tasks
        tasksListView = view.findViewById(R.id.taskList);
        RefreshTasks();

        tasksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //When we clicked on an item, load the create a bundle, which is used for sending data between fragments
                Bundle bundle = new Bundle();
                bundle.putInt("taskId", position);

                //Load the second fragment, which is responsible for editing a task
                SecondFragment fragment = new SecondFragment(hobbyActivity);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
            }
        });

        view.findViewById(R.id.addBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //If we clicked the plus button, add a new task, which is specified by passing it the taskId = -1
                Bundle bundle = new Bundle();
                bundle.putInt("taskId", -1);

                //Load the second fragment, which is responsible for editing a task
                SecondFragment fragment = new SecondFragment(hobbyActivity);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).commit();
            }
        });
    }

    public void RefreshTasks(){

        //Sort the tasks based on selected option. It uses selection sort because I was too tired to implement something else or to figure out how java sort works.
        for(int index = 0; index < hobbyActivity.hobbies.size() - 1; index++){
            for(int index2 = index + 1; index2 < hobbyActivity.hobbies.size(); index2++) {
                int compare = 0;    //Will specify whether or not we need to swap the items, it is different for each sort mode.
                switch (HobbyActivity.sortType) {
                    case Name:
                        //If we are sorting by name, use compare to, because it is perfect for sorting alphabetically
                        compare = hobbyActivity.hobbies.get(index).hobbyName.compareTo(hobbyActivity.hobbies.get(index2).hobbyName);
                        break;
                    case Color:
                        //If we are sorting by color, get compare from subtracting the scores of the two items
                        compare = hobbyActivity.hobbies.get(index).hobbyScore - hobbyActivity.hobbies.get(index2).hobbyScore;
                        break;
                    case DueDate:
                        //If we are sorting by dates, the compare to does exatly what we want.
                        MyDate date1 = hobbyActivity.hobbies.get(index).getDueDate(Calendar.getInstance());
                        MyDate date2 = hobbyActivity.hobbies.get(index2).getDueDate(Calendar.getInstance());

                        //caller.compareTo(other)
                        //if caller < other     return positive days between them
                        //if caller > other     return negative days between them
                        //if caller == other    return 0
                        compare = date2.compareTo(date1);
                        break;
                }

                //Based on the sorting option, swap the items
                if(HobbyActivity.sortAscending){
                    if(compare > 0) {
                        Hobby aux = hobbyActivity.hobbies.get(index);
                        hobbyActivity.hobbies.set(index, hobbyActivity.hobbies.get(index2));
                        hobbyActivity.hobbies.set(index2, aux);
                    }
                }
                else{
                    if(compare < 0) {
                        Hobby aux = hobbyActivity.hobbies.get(index);
                        hobbyActivity.hobbies.set(index, hobbyActivity.hobbies.get(index2));
                        hobbyActivity.hobbies.set(index2, aux);
                    }
                }
            }
        }

        //Filter the options based on the selected filter
        ArrayList<Hobby> displayHobbies = new ArrayList<>();    //This list will hold the tasks that will be displayed
        for(int index = 0; index < hobbyActivity.hobbies.size(); index++){
            switch (HobbyActivity.filterOptions){
                case DisplayAll:
                    //If we want to display everything, then add every item to the list
                    displayHobbies.add(hobbyActivity.hobbies.get(index));
                    break;
                case DisplayDue:
                    //If we want to display the ones due today, then check their due dates, and if their due dates are today, add them to the list
                    MyDate dueDate = hobbyActivity.hobbies.get(index).getDueDate(Calendar.getInstance());
                    if(dueDate.compareTo(new MyDate(Calendar.getInstance())) == 0)
                        displayHobbies.add(hobbyActivity.hobbies.get(index));
                    break;
                case DisplayNotComplete:
                    //If we want to display the tasks that arent't complete yet, then check whether they re finished or not first.
                    if(hobbyActivity.hobbies.get(index).getDone() == false)
                        displayHobbies.add(hobbyActivity.hobbies.get(index));
                    break;
            }
        }

        //Display the list
        tasksAdapter = new HobbyListAdapter(getContext(), R.layout.listview_row, displayHobbies, hobbyActivity, getActivity());
        tasksListView.setAdapter(tasksAdapter);
    }
}