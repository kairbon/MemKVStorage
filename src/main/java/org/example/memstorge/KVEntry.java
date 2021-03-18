package org.example.memstorge;

/**
 * @author : kairbon
 * @date : 2021/3/18
 */

public class KVEntry {

  private byte[] key;
  private byte[] value;

  public KVEntry() {
  }

  public KVEntry(byte[] key, byte[] value) {
    this.key = key;
    this.value = value;
  }

  public byte[] getKey() {
    return key;
  }

  public void setKey(byte[] key) {
    this.key = key;
  }

  public byte[] getValue() {
    return value;
  }

  public void setValue(byte[] value) {
    this.value = value;
  }

  public int length() {
    return (this.key == null ? 0 : this.key.length) + (this.value == null ? 0 : this.value.length);
  }
}

