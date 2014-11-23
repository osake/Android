package com.eftimoff.fonts.utils;


import android.app.Activity;

import hotchemi.android.rate.AppRate;

public class AppRateManager {

    private static AppRateManager ourInstance = new AppRateManager();

    private AppRateManager() {
    }

    public static AppRateManager getInstance() {
        return ourInstance;
    }

    public void initAppRater(final Activity activity) {
        AppRate.with(activity)
                .setInstallDays(1) // default 10, 0 means install day.
                .setLaunchTimes(5) // default 10
                .setRemindInterval(1) // default 1
                .setShowNeutralButton(true) // default true
                .monitor();
        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(activity);
    }
}
