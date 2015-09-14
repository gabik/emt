package com.kazav.gabi.emt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Void sendNotification(Bundle data, Context c, Class intent_class, Class action_class) {
        String title = data.getString("title");
        String body = data.getString("body");
        String icon = data.getString("icon");
        String do_ticket = data.getString("ticket");

        if ((title == null) ||
            (body == null) ||
            (icon == null) ||
            (do_ticket == null)) { return null; }

        Bitmap bigIcon = AppFlow.getBitmapFromURL(icon);

        Intent intent = new Intent(c, intent_class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(c)
                .setSmallIcon(R.drawable.launcher_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLargeIcon(bigIcon)
                .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(body))
                .setContentIntent(pendingIntent);

        Intent action;
        PendingIntent pending_action;
        if ((action_class != null) && (do_ticket.equals("ticket"))) {
            action = new Intent(c, action_class);
            action.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pending_action = PendingIntent.getActivity(c, 0, action, PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.addAction(R.drawable.ic_action_ticket, "פתח קריאה / צור קשר", pending_action);
        }

        NotificationManager notificationManager =
                (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

        return null;
    }
}
