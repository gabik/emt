package com.kazav.gabi.emt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

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
    boolean go_to_cam = false;

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(OpenTicket.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_page);
        ((EditText)findViewById(R.id.txt_name)).setText(AppFlow.loadString(this, "name"));
        ((EditText)findViewById(R.id.txt_phone)).setText(AppFlow.loadString(this, "phone"));
        ((ImageButton)findViewById(R.id.img_btn)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });
//        EditText msg = (EditText)findViewById(R.id.txt_msg);
//        ((EditText)findViewById(R.id.txt_name)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    hideKeyboard(v);
//                }
//            }
//        });
//        ((EditText)findViewById(R.id.txt_phone)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    hideKeyboard(v);
//                }
//            }
//        });
//        msg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    hideKeyboard(v);
//                }
//            }
//        });
//        msg.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                Log.w("key", Integer.toString(keyCode));
//                if (keyCode == 66) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                }
//                return false;
//            }
//        });
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
            AppFlow.saveString(this, "name", ((EditText) findViewById(R.id.txt_name)).getText().toString());
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

    public void bye() {
        Log.w("Back", "1");
        this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pause) {
            super.onResume();
            bye();
            pause = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!go_to_cam) {
            pause = true;
        }
        go_to_cam = false;
    }

    public void take_pic(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        go_to_cam = true;
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
