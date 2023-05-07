package com.example.hobbymanagementapp;

import java.util.Calendar;

public class Hobby {
    //Properties of the hobby
    public String hobbyName;
    public MyDate startDate;
    public Boolean[] weekDays;  //The days of the week we need to do the task on. It is only used in RepeatType.Weekly
    public RepeatType repeatType;   //Specifies how the task will repeat

    private Boolean done;   //Boolean for knowing whether a task is finished for today or not

    public final int defaultScore = 13; //Used for reseting the score of a task
    private MyDate lastCheckDate;   //Last time the app was opened. Is used for updating the score of a task if multiple days have passed since opening it.

    /*
    Will go from 0 to 26, 0 means that you skipped it a lot, 26 means you were diligent in it.
    The score increases by 1 each day and decreases by 2 each missed day.
     */
    public int hobbyScore;

    public Boolean getDone() { return done; }
    public void setDone(Boolean value){
        done = value;
    }

    public Hobby(String name, Calendar calendar, Boolean[] days, RepeatType repeat, int score){
        hobbyName = name;
        startDate = new MyDate(calendar);
        weekDays = days.clone();
        repeatType = repeat;

        hobbyScore = score;
        done = false;
    }

    public Hobby(String name, MyDate myDate, Boolean[] days, RepeatType repeat, int score){
        hobbyName = name;
        startDate = myDate.clone();
        weekDays = days.clone();
        repeatType = repeat;

        hobbyScore = score;
        done = false;
    }

    public String toString(){
        return hobbyName;
    }

    public void updateHobby(MyDate currentDate){
        //If we didn't check the task up until now, then set it to the current date
        if(lastCheckDate == null)
            lastCheckDate = currentDate.clone();

        if(currentDate.compareTo(lastCheckDate) != 0){
            //If a day or a couple of days passed since we opened the up, then we need to update the scores
            MyDate auxDate = lastCheckDate.clone();

            switch(repeatType){
                case None:
                    //In the none case, if a task is done, it is marked perfectly, if not, it is marked with the lowest color
                    if(done)
                        hobbyScore = 26;
                    else
                        hobbyScore = 0;
                    break;
                case Weekly:
                    //In weekly, start from the last check date and move towards the current date.
                    while(auxDate.compareTo(currentDate) < 0){
                        auxDate.increment();
                        //If the task applies to the current day of the week the auxDate is on
                        if(weekDays[auxDate.getDayOfTheWeek()] == true){
                            //If the task was done previously, increase it's score
                            if(done){
                                if(hobbyScore < 26)
                                    hobbyScore++;
                                //And mark it as not done, for future occurrences, because it was only done once, not for the whole duration of the skip
                                done = false;
                            }
                            else if(hobbyScore > 0)
                                hobbyScore--;   //If it isn't done, decrease it's score
                        }
                    }
                    break;
                case Monthly:
                    //In monthly, start from the last check date and move towards the current date.
                    while(auxDate.compareTo(currentDate) < 0){
                        auxDate.increment();
                        //If it has the same day of the month, then it means it was the day for a deadline
                        if(auxDate.getDay() == startDate.getDay()){
                            //Increase or decrease it's score based on whether or not it was done
                            if(done){
                                if(hobbyScore < 26)
                                    hobbyScore++;
                                //And mark it as not done, for future occurrences, because it was only done once, not for the whole duration of the skip
                                done = false;
                            }
                            else if(hobbyScore > 0)
                                hobbyScore--;
                        }
                    }
                    break;
                case Yearly:
                    //In yearly, start from the last check date and move towards the current date.
                    while(auxDate.compareTo(currentDate) < 0){
                        auxDate.increment();
                        //If it has the same day of the month and the same month, then it means it was the day for a deadline
                        if(auxDate.getMonth() == startDate.getMonth() && auxDate.getDay() == startDate.getDay()){
                            //Increase or decrease it's score based on whether or not it was done
                            if(done){
                                if(hobbyScore < 26)
                                    hobbyScore++;
                                //And mark it as not done, for future occurrences, because it was only done once, not for the whole duration of the skip
                                done = false;
                            }
                            else if(hobbyScore > 0)
                                hobbyScore--;
                        }
                    }
                    break;
            }

            lastCheckDate = currentDate.clone();
        }
    }

    //Convert the due date to a nicely shown string.
    public String getDueDateString(Calendar currentDate){
        MyDate nowDate = new MyDate(currentDate);
        MyDate nextDay = getDueDate(currentDate);
        if(nextDay == null)
            return null;

        int daysDiff = nowDate.compareTo(nextDay);
        if(daysDiff == -1)
            return "yesterday";
        else if(daysDiff == 0)
            return "today";
        else if(daysDiff == 1)
            return "tomorrow";
        else
            return nextDay.toString();
    }

    //Get the next due date starting from the current date. It is influenced by startDate
    public MyDate getDueDate(Calendar currentDate){
        MyDate nowDate = new MyDate(currentDate);
        MyDate nextDay = new MyDate(currentDate);
        switch (repeatType) {
            case None:
                nextDay = new MyDate(startDate.getYear(), startDate.getMonth(), startDate.getDay(), startDate.getDayOfTheWeek());
                break;
            case Weekly:
                nextDay = nowDate.getDateByWeekDay(weekDays);
                if(nextDay == null)
                    return null;  //No due date
                break;
            case Monthly:
                nextDay = nowDate.getDateByMonthDay(startDate);
                break;
            case Yearly:
                nextDay = nowDate.getDateByYearDay(startDate);
                break;
        }
        return nextDay;
    }
}
