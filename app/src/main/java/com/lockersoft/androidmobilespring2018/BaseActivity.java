package com.lockersoft.androidmobilespring2018;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class BaseActivity extends AppCompatActivity{

  Toolbar toolbar;
  public static final String DATABASE_NAME = "events_db";
  public AppDatabase eventDatabase;

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base);


    if( eventDatabase == null ) {
      eventDatabase = Room.databaseBuilder(getApplicationContext(),
          AppDatabase.class, DATABASE_NAME)
          .fallbackToDestructiveMigration()
          .build();
    }
    // Sets the Toolbar to act as the ActionBar for this Activity window.
    // Make sure the toolbar exists in the activity and is not null
//    getMenuInflater().inflate(R.menu.menu, menu);

//    toolbar = (Toolbar) findViewById(R.id.toolbar);
//    setSupportActionBar(toolbar);
//    toolbar.setTitle("Events");
  }

  public void FindEventByNum(final Integer recordNumber ){
    new Thread(new Runnable() {
      @Override
      public void run() {
        eventDatabase.eventDao().findEventById( recordNumber );
      }
    }).start();
  }

  public void SaveEvent(final Event event ) {
    new Thread(new Runnable() {
      @Override
      public void run() {
//        Event event = new Event(eventName, "Description Test", new Date(), new Date());
//        event.setEventID( 2 );
//        event.setName(eventName);
//        event.setDescription("Android Crud Application");
//        event.setStartDate(new Date());
//        event.setEndDate(new Date());
        eventDatabase.eventDao().addEvent(event);
      }
    }).start();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu, menu);
//    toolbar = (Toolbar) findViewById(R.id.toolbar);
//    setSupportActionBar(toolbar);
//    setTitle("Activity Title");
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
    Intent intent;
    switch (item.getItemId()) {
      case R.id.menuEdit:
        Toast.makeText(getApplicationContext(), "clicking on Edit", Toast.LENGTH_SHORT).show();
        intent = new Intent(this, EditActivity.class);
        startActivity(intent);
        return true;

      case R.id.menuViewAll:
        Toast.makeText(getApplicationContext(), "clicking on View All", Toast.LENGTH_SHORT).show();
        intent = new Intent(this, MainActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void toastIt( String msg ){
    Toast.makeText( getApplicationContext(),
        msg, Toast.LENGTH_SHORT ).show();
  }


  public Uri getImageUri(Context inContext, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    return Uri.parse(path);
  }

  public String getRealPathFromURI(Uri uri) {
    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
    cursor.moveToFirst();
    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
    return cursor.getString(idx);
  }
}
