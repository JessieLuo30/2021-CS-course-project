package hw7.hashing;

import hw7.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ChainingHashMap<K, V> implements Map<K, V> {
  ArrayList<ArrayList<Node>> arrayOfList;
  int num;
  int size;
  final double loadFac = 0.75;

  /**
   * Instantiate ChainingHashMap.
   */
  public ChainingHashMap() {
    arrayOfList = new ArrayList<>();
    num = 17;
    for (int i = 0; i < num; i++) {
      arrayOfList.add(null);
    }
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null || has(k)) {
      throw new IllegalArgumentException();
    }
    int index = harsh(k);
    ArrayList<Node> target = arrayOfList.get(index);
    Node add = new Node(k, v);
    if (target != null) {
      target.add(add);
    } else {
      target = new ArrayList<>();
      target.add(add);
    }
    arrayOfList.set(index, target);
    size++;
    grow();
  }

  private void grow() {
    if ((double)size / num >= loadFac) {
      num = num * 2;
      size = 0;
      ArrayList<ArrayList<Node>> current = arrayOfList;
      arrayOfList = new ArrayList<>();
      for (int i = 0; i < num; i++) {
        arrayOfList.add(null);
      }
      for (ArrayList<Node> list : current) {
        if (list != null) {
          for (Node n : list) {
            if (n != null) {
              insert(n.key, n.value);
            }
          }
        }
      }
    }
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = harsh(k);
    ArrayList<Node> target = arrayOfList.get(index);
    Node get = find(target, k);
    if (get == null) {
      throw new IllegalArgumentException();
    } else {
      target.remove(get);
      arrayOfList.set(index, target);
    }
    size--;
    return get.value;
  }

  private Node find(ArrayList<Node> target, K k) {
    if (target == null) {
      return null;
    }
    for (Node object : target) {
      if (object != null && object.key.equals(k)) {
        return object;
      }
    }
    return null;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    int index = harsh(k);
    ArrayList<Node> target = arrayOfList.get(index);
    Node get = find(target, k);
    if (get == null) {
      throw new IllegalArgumentException();
    } else {
      target.remove(get);
      Node add = new Node(k, v);
      target.add(add);
      arrayOfList.set(index, target);
    }
  }

  private int harsh(K k) {
    int harsh = k.hashCode();
    if (harsh < 0) {
      harsh = -harsh;
    }
    return harsh % num;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    if (k == null || !has(k)) {
      throw new IllegalArgumentException();
    }
    int index = harsh(k);
    ArrayList<Node> target = arrayOfList.get(index);
    Node get = find(target, k);
    if (get == null) {
      throw new IllegalArgumentException();
    }
    return get.value;
  }

  @Override
  public boolean has(K k) {
    if (k == null) {
      return false;
    }
    int index = harsh(k);
    ArrayList<Node> target = arrayOfList.get(index);
    return find(target, k) != null;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Iterator<K> iterator() {
    return new ChainingIterator();
  }

  class ChainingIterator implements Iterator<K> {
    int currentList;
    int currentNode;
    int count;

    public boolean hasNext() {
      return count < size;
    }

    public K next() throws NoSuchElementException {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      while (arrayOfList.get(currentList) == null || currentNode >= arrayOfList.get(currentList).size()) {
        currentList++;
        currentNode = 0;
      }
      Node target = arrayOfList.get(currentList).get(currentNode);
      if (target == null) {
        do {
          currentList++;
        } while (arrayOfList.get(currentList) == null);
        currentNode = 0;
        target = arrayOfList.get(currentList).get(currentNode);
      }
      currentNode++;
      count++;
      return target.key;
    }

  }

  class Node {
    K key;
    V value;

    Node(K k, V v) {
      this.key = k;
      this.value = v;
    }
  }
}
