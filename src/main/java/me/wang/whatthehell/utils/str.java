package me.wang.whatthehell.utils;

public class str {
    public static String repeat(String s,int m){
        String string = "";
        for (int i=0;i<m;i++){
            string = string+s;
        }
        return string;
    }
}
