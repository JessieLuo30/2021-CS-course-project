package hw7.hashing;

import hw7.Map;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashMap<K, V> implements Map<K, V> {

  int num;
  Entry<K,V>[] map;
  int size;
  int total;
  final double loadFac = 0.75;
  int[] primes = {47, 97, 197, 397, 797, 1597, 3203, 6421, 12853, 25717, 51437,102877,
    205759, 411527, 823117, 1646237,3292489, 6584983, 13169977};
  int pointer;

  /**
   * Instantiate OpenAddressingHashMap.
   */
  public OpenAddressingHashMap() {
    num = primes[pointer];
    map = (Entry<K,V>[]) Array.newInstance(Entry.class, num);
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null || has(k)) {
      throw new IllegalArgumentException();
    }
    int index = harsh(k);
    if (map[index] == null) {
      Entry<K, V> add = new Entry<>(k, v);
      map[index] = add;
    } else {
      index = getIndex(index);
      Entry<K, V> add = new Entry<>(k, v);
      map[index] = add;
    }
    size++;
    total++;
    grow();
  }

  private int getIndex(int index) {
    for (int i = index; i < index + num; i++) {
      int id = i % num;
      if (map[id] == null) {
        return id;
      }
    }
    return index;
  }

  private void grow() {
    if ((double)total / (double) num >= loadFac) {
      pointer++;
      num = primes[pointer];
      size = 0;
      total = 0;
      Entry<K,V>[] current = map;
      map = (Entry<K,V>[]) Array.newInstance(Entry.class, num);
      for (Entry<K,V> n : current) {
        if (n != null && !n.tomb) {
          insert(n.key, n.value);
        }
      }
    }
  }

  private Entry<K, V> find(int index, K k) {
    for (int i = index; i < index + num; i++) {
      int id = i % num;
      if (map[id] == null) {
        return null;
      }
      if (map[id].key.equals(k) && !map[id].tomb) {
        return map[id];
      }
    }
    return null;
  }

  private int harsh(K k) {
    int harsh = k.hashCode();
    if (harsh < 0) {
      harsh = -harsh;
    }
    return harsh % num;
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int id = harsh(k);
    Entry<K, V> find = find(id, k);
    if (find == null) {
      throw new IllegalArgumentException();
    }
    V value = find.value;
    find.setTomb();
    size--;
    return value;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = harsh(k);
    Entry<K, V> find = find(index, k);
    if (find == null) {
      throw new IllegalArgumentException();
    }
    find.setValue(v);
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    if (k == null || !has(k)) {
      throw new IllegalArgumentException();
    }
    int index = harsh(k);
    Entry<K, V> find = find(index, k);
    if (find == null) {
      throw new IllegalArgumentException();
    }
    return find.value;
  }

  @Override
  public boolean has(K k) {
    if (k == null) {
      return false;
    }
    int id = harsh(k);
    Entry<K, V> find = find(id, k);
    return find != null;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Iterator<K> iterator() {
    return new OpenAddressingIterator();
  }

  class OpenAddressingIterator implements Iterator<K> {
    int count;
    int index;

    public boolean hasNext() {
      return count < size;
    }

    public K next() throws NoSuchElementException {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      while (map[index] == null || map[index].tomb) {
        index++;
      }
      Entry<K,V> target = map[index];
      index++;
      count++;
      return target.key;
    }
  }

  private static class Entry<K,V> {
    K key;
    V value;
    boolean tomb;

    Entry(K k, V v) {
      this.key = k;
      this.value = v;
      tomb = false;
    }

    void setValue(V v) {
      this.value = v;
    }

    void setTomb() {
      this.tomb = true;
    }
  }

}
