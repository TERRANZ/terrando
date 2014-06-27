package ru.terra.ndo.android;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.json.JSONObject;
import ru.terra.ndo.android.constants.URLConstants;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Date: 27.06.14
 * Time: 12:50
 */
public class GetPhotoService extends IntentService {
    public GetPhotoService() {
        super("Get photo Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String imgurl = "";
            URL url = new URL(URLConstants.GET_PHOTO);
            URLConnection urlConn = url.openConnection();
            urlConn.setRequestProperty("Cookie", "uid=" + PreferenceManager.getDefaultSharedPreferences(this).getString("install", ""));
            urlConn.connect();
            Scanner s = new Scanner(urlConn.getInputStream());
            JSONObject json = new JSONObject(s.useDelimiter("\\A").next());
            imgurl = json.getString("url");
            if (!imgurl.isEmpty()) {
                Log.i("GetPhotoService", "received photo url " + imgurl);
                Intent resultIntent = new Intent(this, PhotosActivity.class).putExtra("url", imgurl);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder = builder("New photo!");
                builder.setContentIntent(resultPendingIntent);
                int mNotificationId = 001;
                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, builder.build());
            }
        } catch (Exception e) {
            Log.e("GetPhotoService", "error while getting photo", e);
        }
    }

    private NotificationCompat.Builder builder(String msg) {
        return
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Terrando")
                        .setContentText(msg);
    }
}
