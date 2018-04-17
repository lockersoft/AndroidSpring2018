package com.lockersoft.androidmobilespring2018;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by dave on 4/2/2018.
 */

// example converter for java.util.Date
public  class Converters {
  @TypeConverter
  public Date fromTimestamp(Long value) {
    return value == null ? null : new Date(value);
  }

  @TypeConverter
  public Long dateToTimestamp(Date date) {
    if (date == null) {
      return null;
    } else {
      return date.getTime();
    }
  }
}
