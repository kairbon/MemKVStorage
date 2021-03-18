package org.example.memstorge;

import org.example.memstorge.utils.ByteUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author : kairbon
 * @date : 2021/3/18
 */
public class ByteArray implements Comparable<ByteArray>, Serializable {

  private static final long serialVersionUID = 3030232535108421145L;

  private final byte[] bytes;
  // Cache the hash code, default to 0
  private int hashCode;

  public static ByteArray wrap(final byte[] bytes) {
    return new ByteArray(bytes);
  }

  ByteArray(byte[] bytes) {
    this.bytes = bytes;
    // Initialize hash code to 0
    this.hashCode = 0;
  }

  public byte[] getBytes() {
    return bytes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ByteArray that = (ByteArray) o;
    // We intentionally use the function to compute hashcode here
    return hashCode() == that.hashCode() && Arrays.equals(bytes, that.bytes);
  }

  @Override
  public int hashCode() {
    if (hashCode == 0) { // Lazy initialize
      hashCode = Arrays.hashCode(bytes);
    }
    return hashCode;
  }

  @Override
  public int compareTo(ByteArray o) {
    return ByteUtils.compare(this.bytes, o.bytes);
  }
}
