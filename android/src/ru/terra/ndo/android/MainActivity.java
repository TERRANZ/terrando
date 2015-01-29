package ru.terra.ndo.android;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import ru.terra.ndo.android.task.LoadCaptchaAsyncTask;
import ru.terra.ndo.android.task.SaveInBackground;
import ru.terra.ndo.android.task.SendPhotoAsyncTask;
import ru.terra.ndo.android.util.Wod;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Date: 24.06.14
 * Time: 18:08
 */
public class MainActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, SurfaceHolder.Callback, View.OnClickListener, Camera.PictureCallback, Camera.PreviewCallback, Camera.AutoFocusCallback {
    private Camera camera;
    private SurfaceHolder surfaceHolder;
    private SurfaceView preview;
    private Button shotBtn;
    private SharedPreferences sharedPreferences;
    private LocationClient mLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        preview = (SurfaceView) findViewById(R.id.surfaceView);

        surfaceHolder = preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // кнопка имеет имя Button01
        shotBtn = (Button) findViewById(R.id.button);
        shotBtn.setOnClickListener(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getString("install", "").length() == 0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("install", UUID.randomUUID().toString());
            editor.commit();
        }

        Intent intent = new Intent(this, GetPhotoService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, pendingIntent);

        mLocationClient = new LocationClient(this, this, this);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Camera.Parameters parameters = camera.getParameters();

        //MODIFIED HERE
        List<Camera.Size> sizes = camera.getParameters().getSupportedPictureSizes();
//        Camera.Size optimalSize = getOptimalPreviewSize(sizes, w, h);
        Camera.Size optimalSize = sizes.get(2);
        parameters.setPreviewSize(optimalSize.width, optimalSize.height);
        parameters.setPictureSize(optimalSize.width, optimalSize.height);

        camera.setParameters(parameters);
        camera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;

        int previewSurfaceWidth = preview.getWidth();
        int previewSurfaceHeight = preview.getHeight();

        ViewGroup.LayoutParams lp = preview.getLayoutParams();

        // здесь корректируем размер отображаемого preview, чтобы не было искажений

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            // портретный вид
            camera.setDisplayOrientation(90);
            lp.height = previewSurfaceHeight;
            lp.width = (int) (previewSurfaceHeight / aspect);
        } else {
            // ландшафтный
            camera.setDisplayOrientation(0);
            lp.width = previewSurfaceWidth;
            lp.height = (int) (previewSurfaceWidth / aspect);
        }

        preview.setLayoutParams(lp);
        camera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onClick(View v) {
        if (v == shotBtn) {
            try {
                camera.autoFocus(this);
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка автофокуса", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPictureTaken(byte[] paramArrayOfByte, final Camera paramCamera) {
        new SaveInBackground(new Wod() {
            @Override
            public void wod(final String... param) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.d_send_photo);
                final ImageView imageView = (ImageView) dialog.findViewById(R.id.ivCaptcha);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new LoadCaptchaAsyncTask(imageView).execute();
                    }
                });
                new LoadCaptchaAsyncTask(imageView).execute();
                final EditText editText = (EditText) dialog.findViewById(R.id.edtCapVal);
                Button sendBtn = (Button) dialog.findViewById(R.id.btnSend);
                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Location currentLocation = mLocationClient.getLastLocation();
                        if (currentLocation != null)
                            new SendPhotoAsyncTask(MainActivity.this, sharedPreferences.getString("install", UUID.randomUUID().toString()), currentLocation.getLongitude(), currentLocation.getLatitude(), (String) imageView.getTag(), editText.getText().toString(), new File(param[0])).execute();
                        else
                            Toast.makeText(MainActivity.this, "Unable to get location", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        paramCamera.startPreview();
                    }
                });
            }
        }).execute(paramArrayOfByte);
    }

    @Override
    public void onAutoFocus(boolean paramBoolean, Camera paramCamera) {
        if (paramBoolean) {
            // если удалось сфокусироваться, делаем снимок
            paramCamera.takePicture(null, null, null, this);
        }
    }

    @Override
    public void onPreviewFrame(byte[] paramArrayOfByte, Camera paramCamera) {
        // здесь можно обрабатывать изображение, показываемое в preview
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera = getCameraInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }

    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {

        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }

        return optimalSize;
    }         // Define a DialogFragment that displays the error dialog

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failure : " +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT
        ).show();
    }

    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}
