package com.example.hobbymanagementapp;

import java.util.Calendar;

public class MyDate {
    private int year;
    private int month;  //is 0 based
    private int day;    //is 1 based
    private int dayOfTheWeek;   //0 - sunday, 2 - monday, ..., 6 - saturday

    //The number of days in each month
    private int[] daysPerMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    //Initialize date based on given calendar
    public MyDate(Calendar calendar){
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public MyDate(int _year, int _month, int _day, int _dayOfTheWeek){
        year = _year;
        month = _month;
        day = _day;
        dayOfTheWeek = _dayOfTheWeek;
    }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    public int getDayOfTheWeek() { return dayOfTheWeek; }

    //caller.compareTo(other)
    //if caller < other     return positive days between them
    //if caller > other     return negative days between them
    //if caller == other    return 0
    public int compareTo(MyDate other){
        int yearDiff = other.year - year;
        int dayDiff = other.day - day;
        int monthDiff = other.month - month;

        int monthDays = 0;
        if(monthDiff < 0) {
            for (int index = other.month; index < month; index++)
                monthDays -= daysPerMonth[index];
        }
        else if(monthDiff > 1){
            for (int index = month; index < other.month; index++)
                monthDays += daysPerMonth[index];
        }

        return yearDiff * 365 + monthDays + dayDiff;
    }

    //Clone the current date. Useful to not mess up a date because of passing data by reference by mistake
    public MyDate clone(){
        return new MyDate(year, month, day, dayOfTheWeek);
    }

    //Display the date in string format
    public String toString(){
        return String.format("%02d.%02d.%02d", day, month + 1, year);
    }

    //Increment the current date. It is advised to use this instead of changing the variables directly, because the dayOfTheWeek will be wrong
    public void increment(){
        dayOfTheWeek++;
        if(dayOfTheWeek == 7)
            dayOfTheWeek = 0;

        day++;
        if(day > daysPerMonth[month]){
            day = 1;
            month++;
            if(month == 12){
                month = 0;
                year++;
            }
        }
    }

    //Decrement the current date. It is advised to use this instead of changing the variables directly, because the dayOfTheWeek will be wrong
    public void decrement(){
        dayOfTheWeek--;
        if(dayOfTheWeek == -1)
            dayOfTheWeek = 6;

        day--;
        if(day <= 0){
            month--;
            if(month < 0){
                year--;
                month = 11;
            }
            day = daysPerMonth[month];
        }
    }

    //Get the next date that is on one of the possible week days specified by possibleWeekDays
    //It needs to be in the same format Sunday(0), Monday(1), ..., Saturday(6)
    public MyDate getDateByWeekDay(Boolean[] possibleWeekDays){
        MyDate newDate = new MyDate(year, month, day, dayOfTheWeek);
        int infiniteLoopChecker = 0;
        while(possibleWeekDays[newDate.dayOfTheWeek] == false) {
            newDate.increment();

            //If all values in possibleWeekDays are false, then we may get an infinite loop, check for that here
            infiniteLoopChecker++;
            if(infiniteLoopChecker >= possibleWeekDays.length)
                return null;
        }
        return newDate;
    }

    //Get the next date that has the same month day. Ex current date is 23.01, we give it 17.01, it will give us 17.02
    public MyDate getDateByMonthDay(MyDate startDate){
        MyDate desiredDate = startDate.clone();
        MyDate newDate = new MyDate(year, month, day, dayOfTheWeek);
        while(true){
            boolean cond = false;
            if(desiredDate.year > newDate.year)
                cond = true;
            if(desiredDate.month > newDate.month && newDate.year <= desiredDate.year)
                cond = true;

            //If we are at the right month, if the date isn't in the current month (like 31 for february), then set the date to be the last in the month.
            if(cond == false && desiredDate.day > daysPerMonth[desiredDate.month])
                desiredDate.day = daysPerMonth[desiredDate.month];

            if(desiredDate.day != newDate.day)
                cond = true;

            if(cond)
                newDate.increment();
            else
                break;
        }
        return newDate;
    }

    //Get the next date that has the same month and day. Ex current date is 23.01.2000, we give it 17.01.2000, it will give us 17.01.2001
    public MyDate getDateByYearDay(MyDate startDate){
        MyDate desiredDate = startDate.clone();
        MyDate newDate = new MyDate(year, month, day, dayOfTheWeek);
        while(true){
            boolean cond = false;
            if(desiredDate.year > newDate.year)
                cond = true;
            if(desiredDate.month > newDate.month)
                cond = true;

            if(desiredDate.day != newDate.day)
                cond = true;

            if(cond)
                newDate.increment();
            else
                break;
        }
        return newDate;
    }
}
