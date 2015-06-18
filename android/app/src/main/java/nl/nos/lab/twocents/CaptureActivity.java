package nl.nos.lab.twocents;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CaptureActivity extends Activity implements Camera.PictureCallback {

    private static final String TAG = "CaptureActivity";
    private Camera camera;

    private EditText nameField;
    private View addImageButton;
    private LinearLayout imageRow;
    private EditText titleField;

    private List<Bitmap> createdBitmapList = new LinkedList<>();
    private View finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        nameField = (EditText) findViewById(R.id.name_field);
        titleField = (EditText) findViewById(R.id.title_field);
        addImageButton = findViewById(R.id.action_button);
        imageRow = (LinearLayout) findViewById(R.id.image_row);
        finishButton = findViewById(R.id.add_image_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFields();
            }
        });
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
        nameField.getBackground()
                .setColorFilter(getResources().getColor(R.color.name_text_color), PorterDuff.Mode.SRC_ATOP);
        titleField.getBackground()
                .setColorFilter(getResources().getColor(R.color.name_text_color), PorterDuff.Mode.SRC_ATOP);

        camera = Camera.open();
        CameraPreview preview = new CameraPreview(this, camera);

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        container.setLayoutParams(new RelativeLayout.LayoutParams(
                width, width
        ));

        preview.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        container.addView(preview);
    }

    private void saveFields() {
        finish();
    }

    private void addImage() {
        camera.takePicture(null, null, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_capture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);

        int padding = getResources().getDimensionPixelSize(R.dimen.image_preview_padding);
        bmp = rotateBitmap(bmp, 90);
        int scaledHeight = imageRow.getHeight();
        bmp = Bitmap.createScaledBitmap(bmp, 400, 300, false);
        if (bmp != null) {
            createdBitmapList.add(bmp);
            ImageView capturedImage = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(scaledHeight, scaledHeight);
            params.leftMargin = params.rightMargin = padding;
            capturedImage.setLayoutParams(params);
            capturedImage.setImageBitmap(bmp);
            imageRow.addView(capturedImage);
        }

        camera.startPreview();
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
