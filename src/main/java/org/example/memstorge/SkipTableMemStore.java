package org.example.memstorge;

import org.example.memstorge.utils.ByteUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author : kairbon
 * @date : 2021/3/18
 */
public class SkipTableMemStore implements KVStore {

  private final static Comparator<byte[]> BYTE_COMPARATOR = ByteUtils.getComparator();

  private final ConcurrentNavigableMap<byte[], byte[]> db = new ConcurrentSkipListMap<byte[], byte[]>(BYTE_COMPARATOR);

  private final Map<ByteArray, Long> sequenceDB = new ConcurrentHashMap<>();


  @Override
  public boolean init() {
    return true;
  }

  @Override
  public void shutdown() {
    db.clear();
  }

  @Override
  public void get(byte[] key, StorageClosure closure) {
    try {
      byte[] value = this.db.get(key);
      closure.setData(value);
      closure.setSuccess(true);
    } catch (Exception e) {
      //TODO: report something
      closure.setErrorMsg("get error");
    }
  }

  @Override
  public void put(byte[] key, byte[] value, StorageClosure closure) {
    try {
      this.db.put(key, value);
      closure.setSuccess(true);
    } catch (Exception e) {
      //TODO: report something
      closure.setErrorMsg("put error");
    }
  }

  @Override
  public void containKey(byte[] key, StorageClosure closure) {
    try {
      boolean isContain = this.db.containsKey(key);
      closure.setData(isContain);
    } catch (Exception e) {
      //TODO: report something
      closure.setErrorMsg("contain error");
    }
  }

  @Override
  public void scan(byte[] startKey, byte[] endKey, int limit, boolean readOnlySafe, boolean returnValue, StorageClosure closure) {
    final List<KVEntry> entries = new ArrayList<>();
    normalizeLimit(limit);
    byte[] rskey = ByteUtils.nullToEmpty(startKey);
    final ConcurrentNavigableMap<byte[], byte[]> subMap;
    if (endKey == null) {
      subMap = this.db.tailMap(rskey);
    } else {
      subMap = this.db.subMap(rskey, endKey);
    }

    try {
      for (Map.Entry<byte[], byte[]> entry : subMap.entrySet()) {
        entries.add(new KVEntry(entry.getKey(), returnValue ? entry.getValue() : null));
        if (entries.size() > limit) {
          break;
        }
      }
      closure.setData(entries);
      closure.setSuccess(true);
    } catch (Exception e) {
      closure.setErrorMsg("scan error");
    }
  }

  @Override
  public void getSequence(byte[] seqKey, int step, StorageClosure closure) {
    try {
      final ByteArray wrappedKey = ByteArray.wrap(seqKey);
      Long startVal = this.sequenceDB.get(wrappedKey);
      startVal = startVal == null ? 0 : startVal;
      if (step < 0) {
        // never get here
        closure.setErrorMsg("getSequence error");
        return;
      }
      if (step == 0) {
        closure.setData(new Sequence(startVal, startVal));
        closure.setSuccess(true);
        return;
      }
      final long endVal = getSafeEndValueForSequence(startVal, step);
      if (startVal != endVal) {
        this.sequenceDB.put(wrappedKey, endVal);
      }
      closure.setSuccess(true);
      closure.setData(new Sequence(startVal, endVal));
    } catch (final Exception e) {

      closure.setErrorMsg("Fail to [GET_SEQUENCE]");
    }
  }


  /**
   * If limit == 0, it will be modified to Integer.MAX_VALUE on the server
   * and then queried.  So 'limit == 0' means that the number of queries is
   * not limited. This is because serialization uses var-int to compress
   * numbers.  In the case of 0, only 1 byte is occupied, and Integer.MAX_VALUE
   * takes 5 bytes.
   *
   * @param limit input limit
   * @return normalize limit
   */
  protected int normalizeLimit(final int limit) {
    return limit > 0 ? limit : Integer.MAX_VALUE;
  }

  protected long getSafeEndValueForSequence(final long startVal, final int step) {
    return Math.max(startVal, Long.MAX_VALUE - step < startVal ? Long.MAX_VALUE : startVal + step);
  }

}
