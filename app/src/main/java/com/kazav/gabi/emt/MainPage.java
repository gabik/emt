package com.kazav.gabi.emt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainPage extends AppCompatActivity {

    private View main_view;
    WebView my_webview;
    String mish_phone = "+972509166011";
//    String mish_phone = "+972527772457";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_page);
        ((TextView)findViewById(R.id.line1txt)).setTextSize(6 * getResources().getDisplayMetrics().density);
        main_view = getLayoutInflater().inflate(R.layout.activity_main_page, null, false);
        Drawable main_bg = main_view.findViewById(R.id.main_layout).getBackground();
        main_bg.setAlpha(127);
        GCMRegistration.register_me(this);
        my_webview = (WebView)main_view.findViewById(R.id.webview);
        my_webview.getSettings().setJavaScriptEnabled(true);
        my_webview.getSettings().setLoadWithOverviewMode(true);
        my_webview.getSettings().setUseWideViewPort(true);
        my_webview.loadUrl("http://www.emt-it.com/#!sales/c1w1q");
        my_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                thread_done();
            }
        });
    }

    private void thread_done()
    {
        my_webview.setInitialScale(1);
        setContentView(main_view);
    }

    public void call_me(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mish_phone));
        startActivity(callIntent);
    }

    public void open_ticket(View view) {
        Intent ticket = new Intent(this, OpenTicket.class);
        startActivity(ticket);
    }

    public void msg_me(View view) {
        //PendingIntent sms_pi = PendingIntent.getActivity(this, 3, new Intent(this, SMSHandler.class), 0);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("בחר רמת דחיפות");
        final String[] severities = new String[] {
                "דחוף", "רגיל", "לא דחוף"
        };
        adb.setItems(severities, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String clickedItemValue = Arrays.asList(severities).get(which);
                SmsManager sms_mn = SmsManager.getDefault();
                String msg = "היי, עזרתך נדרשת [" + clickedItemValue + "]. תודה";
                sms_mn.sendTextMessage(mish_phone, null, msg, null, null);
                Toast.makeText(getApplicationContext(), "נשלח. נחזור אליך בהקדם.", Toast.LENGTH_LONG).show();
            }
        });
        adb.setNegativeButton("בטל שליחה", null);
        adb.show();
    }
}
