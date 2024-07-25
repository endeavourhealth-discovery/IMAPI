package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.*;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Objects;

@JsonSerialize(using = PartialDateTimeSerializer.class)
@JsonDeserialize(using = PartialDateTimeDeserializer.class)
public class PartialDateTime {

  public enum Precision {
    YYYY,
    YYYY_MM,
    YYYY_MM_DD,
    YYYY_MM_DD_HH_MM_SS,
    YYYY_MM_DD_HH_MM_SS_NNN,
    YYYY_MM_DD_HH_MM_SS_NNN_ZZZZ

  }

  private final OffsetDateTime dateTime;
  private final Precision precision;

  public OffsetDateTime getDateTime() {
    return dateTime;
  }

  public Precision getPrecision() {
    return precision;
  }

  public PartialDateTime(Date date, Precision precision) {
    this.dateTime = OffsetDateTime.of(LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC")), ZoneOffset.UTC);
    this.precision = precision;
  }

  public PartialDateTime(LocalDateTime date, Precision precision) {
    this.dateTime = OffsetDateTime.of(date, ZoneOffset.UTC);
    this.precision = precision;
  }

  public PartialDateTime(OffsetDateTime date, Precision precision) {
    this.dateTime = date;
    this.precision = precision;
  }

  public PartialDateTime(int year) {
    this.dateTime = OffsetDateTime.of(year, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    this.precision = Precision.YYYY;
  }

  public PartialDateTime(int year, int month) {
    this.dateTime = OffsetDateTime.of(year, month, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    this.precision = Precision.YYYY_MM;
  }

  public PartialDateTime(int year, int month, int dayOfMonth) {
    this.dateTime = OffsetDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0, ZoneOffset.UTC);
    this.precision = Precision.YYYY_MM_DD;
  }

  public PartialDateTime(int year, int month, int dayOfMonth, int hour, int minute, int seconds) {
    this.dateTime = OffsetDateTime.of(year, month, dayOfMonth, hour, minute, seconds, 0, ZoneOffset.UTC);
    this.precision = Precision.YYYY_MM_DD_HH_MM_SS;
  }

  public PartialDateTime(int year, int month, int dayOfMonth, int hour, int minute, int seconds, int nanoSeconds) {
    this.dateTime = OffsetDateTime.of(year, month, dayOfMonth, hour, minute, seconds, nanoSeconds, ZoneOffset.UTC);
    this.precision = Precision.YYYY_MM_DD_HH_MM_SS_NNN;
  }

  public PartialDateTime(int year, int month, int dayOfMonth, int hour, int minute, int seconds, int nanoSeconds, String offset) {
    this.dateTime = OffsetDateTime.of(year, month, dayOfMonth, hour, minute, seconds, nanoSeconds, ZoneOffset.of(offset));
    this.precision = Precision.YYYY_MM_DD_HH_MM_SS_NNN_ZZZZ;
  }

  public static PartialDateTime parse(String date) {

    if (!date.matches("([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]|60)(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?"))
      throw new IllegalArgumentException("Given String does not match required format. Allowed formats are e.g. YYYY, YYYY-MM, YYYY-MM-DD, YYYY-MM-DDTHH:MM:SS, YYYY-MM-DDTHH:MM:SS.NNN, YYYY-MM-DDTHH:MM:SS.NNN+-ZZ:ZZ");

    PartialDateTime partialDateTime = null;
    String offset = null;
    String[] dateComponents = date.replaceAll("\\D", " ").split(" ");

    switch (dateComponents.length) {
      case 1:
        partialDateTime = new PartialDateTime(Integer.parseInt(dateComponents[0]));
        break;
      case 2:
        partialDateTime = new PartialDateTime(Integer.parseInt(dateComponents[0]),
          Integer.parseInt(dateComponents[1]));
        break;
      case 3:
        partialDateTime = new PartialDateTime(Integer.parseInt(dateComponents[0]),
          Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]));
        break;
      case 6:
        partialDateTime = new PartialDateTime(Integer.parseInt(dateComponents[0]),
          Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]),
          Integer.parseInt(dateComponents[3]), Integer.parseInt(dateComponents[4]),
          Integer.parseInt(dateComponents[5]));
        break;
      case 7:
        partialDateTime = new PartialDateTime(Integer.parseInt(dateComponents[0]),
          Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]),
          Integer.parseInt(dateComponents[3]), Integer.parseInt(dateComponents[4]),
          Integer.parseInt(dateComponents[5]), Integer.parseInt(dateComponents[6]));
        break;
      case 9:
        if (date.contains("+"))
          offset = "+";
        else
          offset = "-";
        partialDateTime = new PartialDateTime(Integer.parseInt(dateComponents[0]),
          Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]),
          Integer.parseInt(dateComponents[3]), Integer.parseInt(dateComponents[4]),
          Integer.parseInt(dateComponents[5]), Integer.parseInt(dateComponents[6]),
          String.format("%s%s:%s", offset, dateComponents[7], dateComponents[8]));
        break;
      default:
        throw new IllegalArgumentException("Invalid String");
    }
    return partialDateTime;
  }


  @Override
  public String toString() {
    String formattedDate = null;

    int year = this.dateTime.getYear();
    int month = this.dateTime.getMonthValue();
    int day = this.dateTime.getDayOfMonth();
    int hour = this.dateTime.getHour();
    int minute = this.dateTime.getMinute();
    int second = this.dateTime.getSecond();
    int nano = this.dateTime.getNano();
    ZoneOffset offset = this.dateTime.getOffset();

    switch (this.precision) {
      case YYYY:
        formattedDate = String.valueOf(year);
        break;
      case YYYY_MM:
        formattedDate = String.format("%04d-%02d", year, month);
        break;
      case YYYY_MM_DD:
        formattedDate = String.format("%04d-%02d-%02d", year, month, day);
        break;
      case YYYY_MM_DD_HH_MM_SS:
        formattedDate = String.format("%04d-%02d-%02dT%02d:%02d:%02dZ", year, month, day, hour, minute, second);
        break;
      case YYYY_MM_DD_HH_MM_SS_NNN:
        formattedDate = String.format("%04d-%02d-%02dT%02d:%02d:%02d.%3dZ", year, month, day, hour, minute, second, nano);
        break;
      case YYYY_MM_DD_HH_MM_SS_NNN_ZZZZ:
        formattedDate = String.format("%04d-%02d-%02dT%02d:%02d:%02d.%3d%s", year, month, day, hour, minute, second, nano, offset);
        break;
      default:
        throw new DateTimeException("This PartialDateTime does not have a specified precision");
    }
    if (!formattedDate.matches("([0-9]([0-9]([0-9][1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]|60)(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?"))
      throw new DateTimeException("PartialDateTime String formatting error");

    return formattedDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PartialDateTime that = (PartialDateTime) o;
    return this.toString().equals(that.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateTime, precision);
  }
}
