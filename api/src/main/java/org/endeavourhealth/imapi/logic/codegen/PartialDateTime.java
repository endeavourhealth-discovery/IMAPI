package org.endeavourhealth.imapi.logic.codegen;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.time.*;
import java.util.Date;
import java.util.Objects;

@Getter
@JsonSerialize(using = PartialDateTimeSerializer.class)
@JsonDeserialize(using = PartialDateTimeDeserializer.class)
public class PartialDateTime {

  private static final String dateRegex = "(\\d(\\d(\\d[1-9]|[1-9]0)|[1-9]00)|[1-9]000)(-(0[1-9]|1[0-2])(-(0[1-9]|[1-2]\\d|3[0-1])(T([01]\\d|2[0-3]):[0-5]\\d:([0-5]\\d|60)(\\.\\d+)?(Z|(\\+|-)((0\\d|1[0-3]):[0-5]\\d|14:00)))?)?)?";
  private final OffsetDateTime dateTime;
  private final Precision precision;

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

  public PartialDateTime(TimeUnits timeUnits, Precision precision, ZoneOffset offset) {
    this.dateTime = OffsetDateTime.of(timeUnits.getYear(), timeUnits.getMonth(), timeUnits.getDayOfMonth(), timeUnits.getHour(), timeUnits.getMinute(), timeUnits.getSecond(), timeUnits.getNanoSecond(), offset);
    this.precision = precision;
  }

  public static PartialDateTime parse(String date) {

    if (!date.matches(dateRegex))
      throw new IllegalArgumentException("Given String does not match required format. Allowed formats are e.g. YYYY, YYYY-MM, YYYY-MM-DD, YYYY-MM-DDTHH:MM:SS, YYYY-MM-DDTHH:MM:SS.NNN, YYYY-MM-DDTHH:MM:SS.NNN+-ZZ:ZZ");

    PartialDateTime partialDateTime = null;
    String offset = null;
    String[] dateComponents = date.replaceAll("\\D", " ").split(" ");
    TimeUnits timeUnits;
    switch (dateComponents.length) {
      case 1:
        timeUnits = new TimeUnits(Integer.parseInt(dateComponents[0]));
        partialDateTime = new PartialDateTime(timeUnits, Precision.YYYY, ZoneOffset.UTC);
        break;
      case 2:
        timeUnits = new TimeUnits(Integer.parseInt(dateComponents[0]), Integer.parseInt(dateComponents[1]));
        partialDateTime = new PartialDateTime(timeUnits, Precision.YYYY_MM, ZoneOffset.UTC);
        break;
      case 3:
        timeUnits = new TimeUnits(Integer.parseInt(dateComponents[0]), Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]));
        partialDateTime = new PartialDateTime(timeUnits, Precision.YYYY_MM_DD, ZoneOffset.UTC);
        break;
      case 6:
        timeUnits = new TimeUnits(Integer.parseInt(dateComponents[0]), Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]), Integer.parseInt(dateComponents[3]), Integer.parseInt(dateComponents[4]), Integer.parseInt(dateComponents[5]));
        partialDateTime = new PartialDateTime(timeUnits, Precision.YYYY_MM_DD_HH_MM_SS, ZoneOffset.UTC);
        break;
      case 7:
        timeUnits = new TimeUnits(Integer.parseInt(dateComponents[0]), Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]), Integer.parseInt(dateComponents[3]), Integer.parseInt(dateComponents[4]), Integer.parseInt(dateComponents[5]), Integer.parseInt(dateComponents[6]));
        partialDateTime = new PartialDateTime(timeUnits, Precision.YYYY_MM_DD_HH_MM_SS_NNN, ZoneOffset.UTC);
        break;
      case 9:
        if (date.contains("+"))
          offset = "+";
        else
          offset = "-";
        timeUnits = new TimeUnits(Integer.parseInt(dateComponents[0]),
          Integer.parseInt(dateComponents[1]), Integer.parseInt(dateComponents[2]),
          Integer.parseInt(dateComponents[3]), Integer.parseInt(dateComponents[4]),
          Integer.parseInt(dateComponents[5]), Integer.parseInt(dateComponents[6]));
        partialDateTime = new PartialDateTime(timeUnits, Precision.YYYY_MM_DD_HH_MM_SS_NNN_ZZZZ,
          ZoneOffset.of(String.format("%s%s:%s", offset, dateComponents[7], dateComponents[8])));
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

    formattedDate = switch (this.precision) {
      case YYYY -> String.valueOf(year);
      case YYYY_MM -> String.format("%04d-%02d", year, month);
      case YYYY_MM_DD -> String.format("%04d-%02d-%02d", year, month, day);
      case YYYY_MM_DD_HH_MM_SS ->
        String.format("%04d-%02d-%02dT%02d:%02d:%02dZ", year, month, day, hour, minute, second);
      case YYYY_MM_DD_HH_MM_SS_NNN ->
        String.format("%04d-%02d-%02dT%02d:%02d:%02d.%3dZ", year, month, day, hour, minute, second, nano);
      case YYYY_MM_DD_HH_MM_SS_NNN_ZZZZ ->
        String.format("%04d-%02d-%02dT%02d:%02d:%02d.%3d%s", year, month, day, hour, minute, second, nano, offset);
    };
    if (!formattedDate.matches(dateRegex))
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
