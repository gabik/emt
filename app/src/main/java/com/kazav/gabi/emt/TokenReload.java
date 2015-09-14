package com.kazav.gabi.emt;

import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

public class TokenReload extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Log.w("TokenReload", "Yes");
        GCMRegistration.register_me(this);
    }
}
