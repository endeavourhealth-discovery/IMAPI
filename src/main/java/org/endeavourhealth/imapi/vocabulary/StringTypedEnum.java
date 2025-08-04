package org.endeavourhealth.imapi.vocabulary;

import java.util.stream.IntStream;

public class StringTypedEnum implements CharSequence {
  public static final StringTypedEnum IM = new StringTypedEnum("http://endhealth.info/im#");
  public static final StringTypedEnum PROV = new StringTypedEnum("http://endhealth.info/prov#");
  public static final StringTypedEnum CEG = new StringTypedEnum("http://endhealth.info/ceg#");
  public static final StringTypedEnum IM1 = new StringTypedEnum("http://endhealth.info/im1#");

  private String value;

  public StringTypedEnum() {
  }

  public StringTypedEnum(String value) {
    this.value = value;
  }

  @Override
  public int length() {
    return value.length();
  }

  @Override
  public char charAt(int index) {
    return value.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return value.subSequence(start, end);
  }

  @Override
  public boolean isEmpty() {
    return value.isEmpty();
  }

  @Override
  public IntStream chars() {
    return value.chars();
  }

  @Override
  public IntStream codePoints() {
    return value.codePoints();
  }

  @Override
  public String toString() {
    return value;
  }
}
