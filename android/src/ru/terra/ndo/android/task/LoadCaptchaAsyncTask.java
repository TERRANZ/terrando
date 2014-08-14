package ru.terra.ndo.android.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import org.json.JSONException;
import org.json.JSONObject;
import ru.terra.ndo.android.constants.URLConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Date: 25.06.14
 * Time: 18:11
 */
public class LoadCaptchaAsyncTask extends AsyncTask<Void, Void, Bitmap> {
    private ImageView imageView;
    private String cid;

    public LoadCaptchaAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        URL url = null;
        try {
            url = new URL(URLConstants.GET_CAPTCHA);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection urlConn = null;
        try {
            urlConn = url.openConnection();
            urlConn.connect();
            Scanner s = new Scanner(urlConn.getInputStream());
            JSONObject captcha = new JSONObject(s.useDelimiter("\\A").next());
            String capImage = captcha.getString("image");
            cid = captcha.getString("cid");
            if (capImage != null) {
                InputStream in = new URL(capImage).openStream();
                return BitmapFactory.decodeStream(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        if (image != null) {
            imageView.setImageBitmap(image);
            imageView.setTag(cid);
        }
    }
}
