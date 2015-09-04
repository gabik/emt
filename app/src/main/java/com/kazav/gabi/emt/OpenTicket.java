package com.kazav.gabi.emt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class OpenTicket extends ActionBarActivity {

    int REQUEST_IMAGE_CAPTURE = 0;
    File imageFile;
    boolean has_pic = false;
    boolean pause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_page);
        ((EditText)findViewById(R.id.txt_name)).setText(AppFlow.loadString(this, "name"));
        ((EditText)findViewById(R.id.txt_phone)).setText(AppFlow.loadString(this, "phone"));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main_page, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void send_mail(View view) {
        if (((EditText)findViewById(R.id.txt_name)).getText().toString().equals("")) { AppFlow.show_message(this, "אנא מלא שם", false); }
        else if (((EditText)findViewById(R.id.txt_phone)).getText().toString().equals("")) { AppFlow.show_message(this, "אנא מלא טלפון", false); }
        else {
            AppFlow.saveString(this, "name", ((EditText)findViewById(R.id.txt_name)).getText().toString());
            AppFlow.saveString(this, "phone", ((EditText)findViewById(R.id.txt_phone)).getText().toString());
            String subject = "פתיחת תקלה עבור: " + ((EditText) findViewById(R.id.txt_name)).getText();
            String body = "שם: " + ((EditText) findViewById(R.id.txt_name)).getText() + "<BR>טלפון: " + ((EditText) findViewById(R.id.txt_phone)).getText() + "<BR><BR>" + ((EditText) findViewById(R.id.txt_msg)).getText();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"eliran@mishkis.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
            if (has_pic) {
                i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(imageFile));
//                i.setType("image/png");
            }
            startActivity(Intent.createChooser(i, "בחר תוכנת אימייל"));
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (pause) {
//            super.onResume();
//            onBackPressed();
//            pause = false;
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        pause = true;
//    }

    public void take_pic(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                File root = Environment.getExternalStorageDirectory();
                imageFile = new File(root, "screenshot.png");
                FileOutputStream save_pic = new FileOutputStream(imageFile);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, save_pic);
                save_pic.flush();
                save_pic.close();
                has_pic = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            AppFlow.show_message(OpenTicket.this, "Cannot take pic", false);
        }
    }

}
