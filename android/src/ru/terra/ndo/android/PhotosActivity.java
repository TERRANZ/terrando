package ru.terra.ndo.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Date: 24.06.14
 * Time: 18:08
 */
public class PhotosActivity extends Activity {
    private ImageView imageView;

    private class LoadPhotoAsyncTask extends AsyncTask<String, Void, Void> {
        private Bitmap bitmap;

        @Override
        protected Void doInBackground(String... strings) {
            InputStream in = null;
            try {
                in = new URL(strings[0]).openStream();
                //Files.copy(in, Paths.get(Environment.getExternalStorageDirectory().toURI()), StandardCopyOption.COPY_ATTRIBUTES);
                bitmap = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_photo);
        imageView = (ImageView) findViewById(R.id.ivPhoto);
        new LoadPhotoAsyncTask().execute(getIntent().getStringExtra("url"));
    }
}
