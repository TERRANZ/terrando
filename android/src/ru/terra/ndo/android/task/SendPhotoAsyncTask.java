package ru.terra.ndo.android.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import ru.terra.ndo.android.constants.URLConstants;

import java.io.File;
import java.io.IOException;

/**
 * Date: 24.06.14
 * Time: 18:18
 */
public class SendPhotoAsyncTask extends AsyncTask<Void, Void, Boolean> {
    private final String info;
    private final Double lon;
    private final Double lat;
    private final String captcha;
    private final String capVal;
    private final File photo;
    private final ProgressDialog dialog;
    private Exception exception;

    public SendPhotoAsyncTask(Context context, String info, Double lon, Double lat, String captcha, String capVal, File photo) {
        this.info = info;
        this.lon = lon;
        this.lat = lat;
        this.captcha = captcha;
        this.capVal = capVal;
        this.photo = photo;
        dialog = ProgressDialog.show(context, "Sending", "Sending", true);
    }

    @Override
    protected Boolean doInBackground(Void... strings) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(URLConstants.ADD_PHOTO);

        FileBody uploadFilePart = new FileBody(photo);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", uploadFilePart);

        builder.addPart("captcha", new StringBody(captcha, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("capval", new StringBody(capVal, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("lat", new StringBody(lat.toString(), ContentType.MULTIPART_FORM_DATA));
        builder.addPart("lon", new StringBody(lon.toString(), ContentType.MULTIPART_FORM_DATA));
        builder.addPart("info", new StringBody(info, ContentType.MULTIPART_FORM_DATA));

        httpPost.setEntity(builder.build());
        httpPost.setHeader("Cookie", "uid=" + info);
        try {
            HttpResponse response = httpclient.execute(httpPost);
            Log.i("SendPhotoAsyncTask", response.getStatusLine().toString());
        } catch (IOException e) {
            exception = e;
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        try {
            dialog.dismiss();
        } catch (Exception e) {
        }
    }
}
