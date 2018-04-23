package com.lockersoft.androidmobilespring2018;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {

  private TextView eventNameTxt;
  LiveData<List<String>> items;
  List<String> tempItems = new ArrayList<String>();
  ArrayAdapter adapter;
  ListView lstViewEvents;
  Runnable run;
  Context thisContext;
  ImageView img;

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    eventNameTxt = findViewById(R.id.editTextEventName);

    thisContext = this;
    adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, tempItems); //new ArrayList<String>(conversionMap.keySet()));
    lstViewEvents = (ListView) findViewById(R.id.lstViewEvents);
    lstViewEvents.setAdapter(adapter);

    lstViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view,
                              int position, long id) {
        Toast.makeText(MainActivity.this, "You Clicked at " + items.getValue().get(position), Toast.LENGTH_SHORT).show();
//        runOnUiThread(run);
      }
    });


    img = (ImageView)findViewById(R.id.imageViewEvent);
    img.setImageResource(R.drawable.event1);
//    int id = getResources().getIdentifier(lowerCountryCode, "drawable", getPackageName());  // Get by name


    if( eventDatabase == null ) {
      eventDatabase = Room.databaseBuilder(getApplicationContext(),
          AppDatabase.class, DATABASE_NAME)
          .fallbackToDestructiveMigration()
          .build();
    }

    items = eventDatabase.eventDao().getAll();
    items.observe(this, new Observer<List<String>>() {
      @Override
      public void onChanged(@Nullable List<String> strings) {
        Log.d( "DAVE", "Strings changed");

//        for (strings:
//             ) {
//
//        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, strings); //new ArrayList<String>(conversionMap.keySet()));
        lstViewEvents.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        lstViewEvents.invalidateViews();
        lstViewEvents.refreshDrawableState();      }
    });
    //GetEventTitles();

    run = new Runnable() {
      public void run() {
        //reload content
     //   GetEventTitles();
        adapter.notifyDataSetChanged();
        lstViewEvents.invalidateViews();
        lstViewEvents.refreshDrawableState();
      }
    };
  }

  // Create New Event
  public void NewEventOnClick( View v ){
    String eventName = eventNameTxt.getText().toString();
    Event event = new Event(eventName, "Description Test", new Date(), new Date());
    SaveEvent( event );
   // GetEventTitles();
    runOnUiThread(run);
  }

  private void GetEventTitles(){
    new Thread(new Runnable() {
      @Override
      public void run() {
        items = eventDatabase.eventDao().getAll();
      }
    }).start();
  }
}

