package com.tariqaliiman.tariqaliiman.Application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.onesignal.OneSignal;
import com.tariqaliiman.tariqaliiman.Database.AppPreference;

import java.util.Locale;

/**
 * Class to overwrite application class
 */
public class QuranApplication extends Application {
    private static Context appContext;
    private static QuranApplication mInstanse;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstanse = this;

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        if (appContext == null) {
            //take instance of application context
            appContext = getApplicationContext();
        }
    }

    public static Context getInstance() {

        //Check application language
        Locale locale;
        if (AppPreference.isArabicMood(appContext))
            locale = new Locale("ar");
        else
            locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        appContext.getResources().updateConfiguration(config, appContext.getResources().getDisplayMetrics());

        return appContext;
    }

    public static synchronized QuranApplication newInstanse(){
        return mInstanse;
    }




}
