package ru.terra.ndo.android;

import android.app.Application;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * Date: 24.06.14
 * Time: 18:08
 */
@ReportsCrashes(formUri = "http://terranz.mine.nu/jbrss/errors/do.error.report/terrando",
        httpMethod = HttpSender.Method.POST,
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.error_caught)
public class TerrandoApplication extends Application {
    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();
    }
}