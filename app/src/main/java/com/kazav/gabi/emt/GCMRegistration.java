package com.kazav.gabi.emt;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Patterns;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class GCMRegistration {

    public static void register_me(Context c) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(c);
        if (resultCode != ConnectionResult.SUCCESS) {
            AppFlow.show_message(c, "אין אפשרות לפתוח את שירותי גוגל פליי", false);
        }
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(c).getAccounts();
        ArrayList<String> emails = new ArrayList<>();
        for (Account account : accounts) {
            if ((emailPattern.matcher(account.name).matches()) && (account.type.equalsIgnoreCase("com.google"))) {
                emails.add(account.name);
            }
        }
        RegistrationAsyncTask reg_task = new RegistrationAsyncTask();
        reg_task.setEmails(emails);
        reg_task.execute(c);

    }

    private static class RegistrationAsyncTask extends AsyncTask<Context, Void, Void> {
        Context c;
        String token;
        ArrayList<String> emails;
        String emails_str;

        @Override
        protected Void doInBackground(Context... params) {
            c=params[0];
            try {
                InstanceID iid = InstanceID.getInstance(c);
                token = iid.getToken(c.getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                HttpHandler post_reg = new HttpHandler("http://app.emt-it.com/add.php", "POST");
                List<NameValuePair> post_vals = new ArrayList<>();
                post_vals.add(new BasicNameValuePair("emails", emails_str));
                post_vals.add(new BasicNameValuePair("token", token));
                post_reg.makeCall(post_vals);
            }catch (IOException | AppFlow.NoNetException e) { e.printStackTrace(); }
            return null;
        }

        public void setEmails(ArrayList<String> e) {
            this.emails = e;
            Collections.sort(this.emails);
            this.emails_str = "";
            for (String estr: this.emails) {
                this.emails_str = this.emails_str.concat(estr).concat(" ");
            }
        }

    }

}
