package com.example.androiddemo.tool;

import android.content.Context;
import android.widget.Toast;

public class Error {
    public static String handleError(Context context, String string) {
        switch (string) {
            case "0101":
            case "0102":
            case "0103":
            case "0201":
            case "0202":
            case "0301":
            case "0401":
            case "0501":
            case "0502":
            case "0503":
                Toast.makeText(context, "ErrCode:" + string, Toast.LENGTH_SHORT).show();
                string = "Error";
                break;

            default:
                break;
        }
        return string;
    }
}
