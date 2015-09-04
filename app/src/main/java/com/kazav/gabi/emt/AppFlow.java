package com.kazav.gabi.emt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by gabik on 1/15/15.
 * Handle closing app and no net exception
 */

public class AppFlow {

    public static class NoNetException extends Exception { public NoNetException(String msg) {super(msg);}}

    static public void no_network_close(Context context) {
        AlertDialog.Builder no_network = new AlertDialog.Builder(context);
        no_network.setTitle(R.string.network_issue_title);
        no_network.setMessage(R.string.network_issue_msg);
        no_network.setPositiveButton(R.string.network_issue_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        no_network.show();
    }

    static public void show_message(Context context, String msg, final boolean exit_val) {
        AlertDialog.Builder no_network = new AlertDialog.Builder(context);
        no_network.setTitle(R.string.emt);
        no_network.setMessage(msg);
        Log.d("Exiting?", Boolean.toString(exit_val));
        no_network.setPositiveButton(R.string.msg_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (exit_val) {
                    System.exit(0);
                }
            }
        });
        no_network.show();
    }

    public static void contact_us(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String subject = "Android App : ";
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support@gandos.net" });
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(Intent.createChooser(intent, "Send Mail"));
    }

    public static String loadString(Context context, String key){
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String val = prefs.getString(key, "");
        return val;
    }

    public static void saveString(Context context, String key, String val){
        SharedPreferences prefs = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, val);
        editor.apply();
    }
}
