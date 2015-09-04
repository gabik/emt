package com.kazav.gabi.emt;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainPage extends ActionBarActivity {

//    private JSONArray deals;
    private ArrayList<Deal> deals_list = new ArrayList<>();
//    private WebView webview = new WebView(this);
    private String web_html;
    private View main_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_page);
//        new LoadJsonThread().execute();
        main_view = getLayoutInflater().inflate(R.layout.activity_main_page, null, false);
        Drawable main_bg = main_view.findViewById(R.id.main_layout).getBackground();
        main_bg.setAlpha(127);
        WebView my_webview = (WebView)main_view.findViewById(R.id.webview);
        my_webview.getSettings().setJavaScriptEnabled(true);
        my_webview.loadUrl("http://www.emt-it.com/#!sales/c1w1q");
        my_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                thread_done();
            }
        });
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

    private class LoadJsonThread extends AsyncTask<Void, Integer, Void> {

        private boolean is_error = false;

        @Override
        protected Void doInBackground(Void... params) {
//            HttpHandler http = new HttpHandler("http://www.emt-it.com/#!sales/c1w1q", "GET");

            try {
//                web_html = http.makeCall();
//                deals = new JSONArray(http_response);
//                for (int i = 0 ; i< deals.length() ; i++) {
//                    deals_list.add(new Deal(deals.getJSONObject(i).getString("name"), Float.parseFloat(deals.getJSONObject(i).getString("price"))));

                Thread.sleep(2000);
            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (AppFlow.NoNetException e) { is_error=true; return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (is_error) AppFlow.no_network_close(MainPage.this);
            else thread_done();
        }
    }

    private void thread_done()
    {
//        try { Thread.sleep(5000); } catch (InterruptedException e) { }
        setContentView(main_view);
//        setContentView(R.layout.activity_main_page);
//        Drawable main_bg = findViewById(R.id.main_layout).getBackground();
//        main_bg.setAlpha(127);
//        WebView my_webview = (WebView)findViewById(R.id.webview);
//        my_webview.getSettings().setJavaScriptEnabled(true);
//        my_webview.loadUrl("http://www.emt-it.com/#!sales/c1w1q");
    }

    public void call_me(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:+972509166011"));
        startActivity(callIntent);
    }

    public void open_ticket(View view) {
        Intent ticket = new Intent(this, OpenTicket.class);
        startActivity(ticket);
    }
}
