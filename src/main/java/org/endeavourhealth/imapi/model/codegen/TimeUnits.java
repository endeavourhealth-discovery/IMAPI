package org.endeavourhealth.imapi.model.codegen;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeUnits {
  private int year;
  private int month;
  private int dayOfMonth;
  private int hour;
  private int minute;
  private int second;
  private int nanoSecond;

  public TimeUnits(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoSecond) {
    this.year = year;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
    this.nanoSecond = nanoSecond;
  }

  public TimeUnits(int year, int month, int dayOfMonth, int hour, int minute, int second) {
    this.year = year;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
    this.nanoSecond = 0;
  }

  public TimeUnits(int year, int month, int dayOfMonth, int hour, int minute) {
    this.year = year;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.hour = hour;
    this.minute = minute;
    this.second = 0;
    this.nanoSecond = 0;
  }

  public TimeUnits(int year, int month, int dayOfMonth, int hour) {
    this.year = year;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.hour = hour;
    this.minute = 0;
    this.second = 0;
    this.nanoSecond = 0;
  }

  public TimeUnits(int year, int month, int dayOfMonth) {
    this.year = year;
    this.month = month;
    this.dayOfMonth = dayOfMonth;
    this.hour = 0;
    this.minute = 0;
    this.second = 0;
    this.nanoSecond = 0;
  }

  public TimeUnits(int year, int month) {
    this.year = year;
    this.month = month;
    this.dayOfMonth = 1;
    this.hour = 0;
    this.minute = 0;
    this.second = 0;
    this.nanoSecond = 0;
  }

  public TimeUnits(int year) {
    this.year = year;
    this.month = 1;
    this.dayOfMonth = 1;
    this.hour = 0;
    this.minute = 0;
    this.second = 0;
    this.nanoSecond = 0;
  }
}
