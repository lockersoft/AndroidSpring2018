package com.lockersoft.androidmobilespring2018;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.lockersoft.androidmobilespring2018.Event;
import com.lockersoft.androidmobilespring2018.EventDao;

@Database(entities = {Event.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
  public abstract EventDao eventDao();
}