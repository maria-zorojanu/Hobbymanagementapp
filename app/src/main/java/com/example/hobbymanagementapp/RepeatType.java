package com.example.hobbymanagementapp;

public enum RepeatType {
    None,       //The task won't repeat and it's start date will be considered the deadline
    Weekly,     //The task will repeat every week based on the selected days of the week
    Monthly,    //The task will repeat at the same date each month (if it's a date that isn't in the current month (ex: 31 for february), it will take the last date in the month)
    Yearly      //The task will repeat at the same date each year
}
