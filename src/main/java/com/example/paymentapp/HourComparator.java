package com.example.paymentapp;

import java.util.Comparator;

public class HourComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        int a = iFindHour(str1);
        int b = iFindHour(str2);
        if(a < b){
            return -1;
        } else if(a > b){
            return 1;
        } else {
            if(iFindMinutes(str1) <= iFindMinutes(str2)){
                return -1;
            } else {
                return 1;
            }
        }
    }

    int iFindHour(String hour){
        String sub = hour.substring(0, iFindSep(hour));
        int newhour = Integer.parseInt(sub);
        return newhour;
    }

    int iFindSep(String hour){
        for(int i=0; i<hour.length(); i++){
            if(hour.charAt(i) == ':'){
                return i;
            }
        }
        return 0;
    }

    int iFindMinutes(String hour){
        String sub = hour.substring((iFindSep(hour)+1));
        int newminutes = Integer.parseInt(sub);
        return newminutes;
    }
}
