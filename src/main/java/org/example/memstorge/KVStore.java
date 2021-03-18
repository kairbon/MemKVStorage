package org.example.memstorge;

/**
 * @author : kairbon
 * @date : 2021/3/18
 */
public interface KVStore {

  /*
   * init storage.
   */
  boolean init();

  /*
   * shutdown storage
   */
  void shutdown();

  /*
   * get value from store.
   */
  void get(byte[] key, StorageClosure closure);

  /*
   * put key and value to store.
   */
  void put(byte[] key, byte[] value, StorageClosure closure);

  /*
   * is this key contain?
   */
  void containKey(byte[] key, StorageClosure closure);

  /**
   * Query all data in the key range of [startKey, endKey),
   * {@code limit} is the max number of keys.
   *
   * Provide consistent reading if {@code readOnlySafe} is true.
   *
   * Only return keys(ignore values) if {@code returnValue} is false.
   */
  void scan(final byte[] startKey, final byte[] endKey, final int limit, final boolean readOnlySafe,
            final boolean returnValue, final StorageClosure closure);

  /**
   * Get a globally unique auto-increment sequence.
   *
   * Be careful do not to try to get or update the value of {@code seqKey}
   * by other methods, you won't get it.
   */
  void getSequence(final byte[] seqKey, final int step, final StorageClosure closure);
}
