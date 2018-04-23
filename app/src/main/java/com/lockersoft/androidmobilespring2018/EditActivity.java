package com.lockersoft.androidmobilespring2018;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends BaseActivity {

  EditText edtEditName;
  EditText edtEditDescription;
  EditText edtEditStartDate;
  EditText edtEditEndDate;
  EditText edtRecordNum;
  ImageView edtImageView;
  String imageFilePath;

  LiveData<Event> event;
  private static final int REQUEST_CAPTURE_IMAGE = 100;   // TODO: Camera
  private int PICK_IMAGE_REQUEST = 1;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit);

    if (eventDatabase == null) {
      eventDatabase = Room.databaseBuilder(getApplicationContext(),
          AppDatabase.class, DATABASE_NAME)
          .fallbackToDestructiveMigration()
          .build();
    }

    edtEditName = findViewById(R.id.edtEditName);
    edtEditDescription = findViewById(R.id.edtEditDescription);
    edtEditStartDate = findViewById(R.id.edtEditStartDate);
    edtEditEndDate = findViewById(R.id.edtEditEndDate);
    edtRecordNum = findViewById(R.id.edtRecordNum);
    edtImageView = findViewById(R.id.edtImageView);

    edtImageView.bringToFront();
    edtImageView.setClickable(true);
    edtImageView.setOnClickListener(  new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        openCameraIntent();
        openCameraIntentToFile();
      }
    });

    event = eventDatabase.eventDao().findEventById( 2 );
    event.observe(this, new Observer<Event>() {
      @Override
      public void onChanged(@Nullable Event event) {
        Log.d("EDITDAVE", "Event changed");
        edtEditName.setText( event.getName() );
        edtEditDescription.setText( event.getDescription() );
//        edtEditStartDate.setText( event.getStartDate().toString() );
//        edtEditEndDate.setText( event.getEndDate().toString() );
      }
    });
  }

  public void AlertDialog(){

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to Delete this record?")
        .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
           toastIt( "You clicked YES");
          }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
            toastIt( "You clicked NO");

          }
        });
    AlertDialog alert = builder.create();
    alert.show();

//    Intent intent = new Intent();
//// Show only images, no videos or anything else
//    intent.setType("image/*");
//    intent.setAction(Intent.ACTION_GET_CONTENT);
//// Always show the chooser (if there are multiple options available)
//    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
  }


//  https://android.jlelse.eu/androids-new-image-capture-from-a-camera-using-file-provider-dd178519a954

  private void openCameraIntent() { // TODO: Camera
    Intent pictureIntent = new Intent(
        MediaStore.ACTION_IMAGE_CAPTURE
    );
    if(pictureIntent.resolveActivity(getPackageManager()) != null) {
      startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
    }
  }

  private void openCameraIntentToFile() {
    Intent pictureIntent = new Intent(
        MediaStore.ACTION_IMAGE_CAPTURE);
    if(pictureIntent.resolveActivity(getPackageManager()) != null){
      //Create a file to store the image
      File photoFile = null;
      try {
        photoFile = createImageFile();
      } catch (IOException ex) {
        // Error occurred while creating the File
            toastIt( "File Error" );
      }
      if (photoFile != null) {
   //     Uri photoURI = FileProvider.getUriForFile(this, "com.lockersoft.androidmobilespring2018", photoFile);  // TODO:  Add provider to manifest
        Uri photoURI = FileProvider.getUriForFile(this, "com.lockersoft.androidmobilespring2018.fileprovider", photoFile);  // TODO:  Add provider to manifest
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
            photoURI);
        startActivityForResult(pictureIntent,
            REQUEST_CAPTURE_IMAGE);
      }
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Bitmap bitmap = null;

    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == REQUEST_CAPTURE_IMAGE &&
        resultCode == RESULT_OK &&
        data != null ) {// &&
       // data.getData() != null) {

      if( data.getData() == null ){
        bitmap = (Bitmap)data.getExtras().get("data");
      } else {
        try {
          bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
        } catch( IOException e ) {
          e.printStackTrace();
        }
        if( bitmap != null ) {
          ImageView imageView = (ImageView) findViewById( R.id.edtImageView );
          imageView.setImageBitmap( bitmap );
        } else {
          toastIt( "No image found" );
        }
      }

    }
  }

//  https://developer.android.com/training/camera/photobasics.html#TaskPath
//  https://android.jlelse.eu/androids-new-image-capture-from-a-camera-using-file-provider-dd178519a954
private File createImageFile() throws IOException {
  // Create an image file name
  String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
  String imageFileName = "JPEG_" + timeStamp + "_";
  File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
  File image = File.createTempFile(
      imageFileName,  /* prefix */
      ".jpg",         /* suffix */
      storageDir      /* directory */
  );

  // Save a file: path for use with ACTION_VIEW intents
  imageFilePath = image.getAbsolutePath();
  return image;
}
}
