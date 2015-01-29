package ru.terra.ndo.android.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import org.acra.ACRA;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import ru.terra.ndo.android.constants.URLConstants;

import java.io.File;

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

    private static final int SOCKET_OPERATION_TIMEOUT = 10 * 1000;

    @Override
    protected Boolean doInBackground(Void... strings) {

        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Terrando 0.1");

        HttpPost httpPost = new HttpPost(URLConstants.ADD_PHOTO);

        FileBody uploadFilePart = new FileBody(photo);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("file", photo, ContentType.MULTIPART_FORM_DATA, photo.getName());

        builder.addPart("captcha", new StringBody(captcha, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("capval", new StringBody(capVal, ContentType.MULTIPART_FORM_DATA));
        builder.addPart("lat", new StringBody(lat.toString(), ContentType.MULTIPART_FORM_DATA));
        builder.addPart("lon", new StringBody(lon.toString(), ContentType.MULTIPART_FORM_DATA));
        builder.addPart("info", new StringBody(info, ContentType.MULTIPART_FORM_DATA));

        httpPost.setEntity(builder.build());
        httpPost.setHeader("Cookie", "uid=" + info);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            Log.i("SendPhotoAsyncTask", response.getStatusLine().toString());
            response.getEntity().consumeContent();
            httpClient.close();
        } catch (Exception e) {
            exception = e;
            ACRA.getErrorReporter().handleException(e);
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
