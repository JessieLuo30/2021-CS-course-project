package hw5;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Set implemented using plain Java arrays.
 *
 * @param <T> Element type.
 */
public class ArraySet<T> implements Set<T> {
  protected int numElements;
  protected T[] data;

  /**
   * Make a set.
   */
  public ArraySet() {
    data = (T[]) new Object[1];
    numElements = 0;
  }

  @Override
  public void insert(T t) {
    if (full()) {
      grow();
    }
    if (!has(t)) {
      data[numElements++] = t;
    }
  }

  private boolean full() {
    return numElements >= data.length;
  }

  // Double the capacity of data array.
  private void grow() {
    T[] bigger = (T[]) new Object[2 * numElements];
    System.arraycopy(data, 0, bigger, 0, numElements);
    data = bigger;
  }

  // Returns the index of t in data or -1 if it is not found.
  // Pre: t != null
  protected int find(T t) {
    for (int i = 0; i < numElements; i++) {
      if (t.equals(data[i])) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public void remove(T t) {
    int i = find(t);
    if (i != -1) {
      data[i] = data[numElements - 1];
      data[numElements - 1] = null;
      numElements--;
    }
  }

  @Override
  public boolean has(T t) {
    return find(t) != -1;
  }

  @Override
  public int size() {
    return numElements;
  }

  @Override
  public Iterator<T> iterator() {
    return new SetIterator();
  }

  // Iterate from element at position 0 to numElements
  private class SetIterator implements Iterator<T> {
    private int current;

    SetIterator() {
      current = 0;
    }

    @Override
    public boolean hasNext() {
      return current < numElements;
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return data[current++];
    }
  }
}
