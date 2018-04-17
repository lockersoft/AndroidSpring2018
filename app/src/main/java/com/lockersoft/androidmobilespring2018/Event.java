package com.lockersoft.androidmobilespring2018;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.sql.Time;
import java.util.Date;

/**
 * Created by dave on 4/2/2018.
 */
@Entity(tableName = "events")

public class Event {

  public Event(String name, String description, Date startDate, Date endDate ) {
    this.name = name;
    this.description = description;
  }

  @PrimaryKey( autoGenerate = true )
  @ColumnInfo(name = "eventID")
  private long eventID;

  @ColumnInfo(name = "name")
  private String name;
  @ColumnInfo(name = "description")
  private String description;

  @TypeConverters(Converters.class)
  @ColumnInfo(name = "startDate")
  private Date  startDate;

  @TypeConverters(Converters.class)
  @ColumnInfo(name = "endDate")
  private Date  endDate;

//  @ColumnInfo(name = "startTime")
//  private Time  startTime;
//  @ColumnInfo(name = "endTime")
//  private Time  endTime;

  public long getEventID() {
    return eventID;
  }

  public void setEventID(long eventID) {
    this.eventID = eventID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

//  public Time getStartTime() {
//    return startTime;
//  }
//
//  public void setStartTime(Time startTime) {
//    this.startTime = startTime;
//  }
//
//  public Time getEndTime() {
//    return endTime;
//  }
//
//  public void setEndTime(Time endTime) {
//    this.endTime = endTime;
//  }
}
