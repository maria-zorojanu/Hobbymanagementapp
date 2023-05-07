package com.example.hobbymanagementapp;

import android.content.SharedPreferences;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class DataSaver {
    SharedPreferences sharedPreferences;

    public DataSaver(SharedPreferences _shared){
        sharedPreferences = _shared;
    }

    //Save data to json
    public void SaveData(ArrayList<Hobby> hobbies){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(hobbies);
        editor.putString("task list", json);
        editor.apply();
    }

    //Load data from json. If json is not found, then it will return an empty list
    public ArrayList<Hobby> LoadData(){
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<Hobby>>(){}.getType();
        ArrayList<Hobby> result = gson.fromJson(json, type);

        if(result == null)
            result = new ArrayList<>();

        return result;
    }

    //Clear the player prefs for this app
    public void ClearSharedPrefs(){
        sharedPreferences.edit().remove("task list").commit();
    }
}
