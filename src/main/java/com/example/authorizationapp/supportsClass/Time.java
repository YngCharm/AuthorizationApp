package com.example.authorizationapp.supportsClass;

import java.time.LocalTime;

public class Time {
    public String setTime() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();

        if (hour >= 6 && hour < 12) {
            return "Доброе утро";
        } else if (hour >= 12 && hour < 18) {
            return "Добрый день";
        } else if (hour >= 18 && hour < 22) {
            return "Добрый вечер";
        } else {
            return "Доброй ночи";
        }
    }
}
