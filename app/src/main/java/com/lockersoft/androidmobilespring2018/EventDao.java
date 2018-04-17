package com.lockersoft.androidmobilespring2018;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EventDao {
  @Query("SELECT name FROM events")
  LiveData<List<String>> getAll();

  @Query("SELECT * FROM events WHERE eventID IN (:eventIDs)")
  List<Event> loadAllByIds(int[] eventIDs);

  @Query("SELECT * FROM events WHERE name LIKE :event_name LIMIT 1")
  Event findByName(String event_name);

  @Insert
  void addEvent(Event event);

  @Insert
  void addMultipleEvents (List<Event> eventsList);

  @Query ("SELECT * FROM events WHERE eventID = :eventID")
  LiveData<Event> findEventById( int eventID );

  @Update
  void updateEvent( Event event );

  @Delete
  void deleteEvent( Event event );
}