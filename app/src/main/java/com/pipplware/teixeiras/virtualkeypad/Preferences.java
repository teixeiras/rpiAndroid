package com.pipplware.teixeiras.virtualkeypad;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by teixeiras on 24/03/15.
 */
public class Preferences {
    static String PREFERENCE_IP = "ip";
    static String PREFERENCE_PORT = "port";

    static SharedPreferences prefs;
    static Preferences self;
    static SharedPreferences sharedInstance(Context context) {
        if (self == null) {
            self = new Preferences(context);
        }
        return prefs;
    }

    public Preferences(Context context) {
        prefs = context.getSharedPreferences(
                "com.pipplware.app", context.MODE_PRIVATE);

    }

}
