package ru.terra.ndo.android.task;

import android.os.AsyncTask;
import android.os.Environment;
import org.acra.ACRA;
import ru.terra.ndo.android.util.Wod;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Date: 26.06.14
 * Time: 13:10
 */
public class SaveInBackground extends AsyncTask<byte[], String, String> {
    private Wod wod;

    public SaveInBackground(Wod wod) {
        this.wod = wod;
    }

    @Override
    protected String doInBackground(byte[]... arrayOfByte) {
        try {
            File saveDir = new File(Environment.getExternalStorageDirectory() + "/terrando/");

            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            String fileName = String.format(Environment.getExternalStorageDirectory() + "/terrando/%d.jpg", System.currentTimeMillis());
            FileOutputStream os = new FileOutputStream(fileName);
            os.write(arrayOfByte[0]);
            os.close();
            return fileName;
        } catch (Exception e) {
            ACRA.getErrorReporter().handleSilentException(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (wod != null)
            wod.wod(s);
    }
}