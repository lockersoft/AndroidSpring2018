package com.lockersoft.androidmobilespring2018;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EditActivity extends BaseActivity {

  EditText edtEditName;
  EditText edtEditDescription;
  EditText edtEditStartDate;
  EditText edtEditEndDate;
  EditText edtRecordNum;

  LiveData<Event> event;

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
}
