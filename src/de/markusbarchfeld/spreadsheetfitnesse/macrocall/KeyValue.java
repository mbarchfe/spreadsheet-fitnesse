package de.markusbarchfeld.spreadsheetfitnesse.macrocall;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class KeyValue {

  private String key;
  private String value;

  public KeyValue(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    KeyValue rhs = (KeyValue) obj;
    return new EqualsBuilder().append(key, rhs.key).append(value, rhs.value)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(key).append(value).hashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append(key).append(value).toString();
  }

}
