package org.endeavourhealth.imapi.cache;

import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.map.LRUMap;

import java.util.ArrayList;

public class TimedCache<K, T> {
  private final String name;
  private final long timeToLive;
  private final LRUMap<K, CacheObject> cacheMap;

  public TimedCache(String name, long timeToLive, final long interval, int maxItems) {
    this.name = name;
    this.timeToLive = timeToLive * 1000;
    cacheMap = new LRUMap<>(maxItems);
    if (this.timeToLive > 0 && interval > 0) {
      Thread t = new Thread(() -> {
        while (true) {
          try {
            Thread.sleep(interval * 1000);
          } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
          }
          cleanup();
        }
      });
      t.setDaemon(true);
      t.start();
    }
  }

  public void put(K key, T value) {
    synchronized (cacheMap) {

      // put(): Puts a key-value mapping into this map.
      cacheMap.put(key, new CacheObject(value));
    }
  }

  public T get(K key) {
    synchronized (cacheMap) {
      CacheObject c;
      c = cacheMap.get(key);
      if (c == null)
        return null;
      else {
        c.setLastAccessed(System.currentTimeMillis());
        return c.value;
      }
    }
  }

  public void remove(K key) {
    synchronized (cacheMap) {
      cacheMap.remove(key);
    }
  }

  public int size() {
    synchronized (cacheMap) {
      return cacheMap.size();
    }
  }

  public void cleanup() {
    long now = System.currentTimeMillis();
    ArrayList<K> deleteKey;
    synchronized (cacheMap) {
      OrderedMapIterator<K, CacheObject> itr = cacheMap.mapIterator();
      // ArrayList: Constructs an empty list with the specified initial capacity.
      // size(): Gets the size of the map.
      deleteKey = new ArrayList<>((cacheMap.size() / 2) + 1);
      K key;
      CacheObject c;
      while (itr.hasNext()) {
        key = itr.next();
        c = itr.getValue();
        if (c != null && (now > (timeToLive + c.getLastAccessed()))) {

          // add(): Appends the specified element to the end of this list.
          deleteKey.add(key);
        }
      }
    }

    for (K key : deleteKey) {
      synchronized (cacheMap) {
        cacheMap.remove(key);
      }
      Thread.yield();
    }

  }

  protected class CacheObject {
    private long lastAccessed = System.currentTimeMillis();
    private T value;

    protected CacheObject(T value) {
      this.value = value;
    }

    protected long getLastAccessed() {
      return lastAccessed;
    }

    public void setLastAccessed(long lastAccessed) {
      this.lastAccessed = lastAccessed;
    }

    protected T getValue() {
      return value;
    }

    protected void setValue(T value) {
      this.value = value;
    }
  }
}