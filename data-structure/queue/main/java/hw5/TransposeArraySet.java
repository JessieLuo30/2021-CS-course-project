package hw5;

/**
 * Set implemented using plain Java arrays and transpose-sequential-search heuristic.
 *
 * @param <T> Element type.
 */
public class TransposeArraySet<T> extends ArraySet<T> {

  //  each time a value is accessed. Override the relevant method(s) from ArraySet.
  @Override
  protected int find(T t) {
    T temp;
    for (int i = 0; i < numElements; i++) {
      if (t.equals(data[i])) {
        if (i == 0) {
          return i;
        }
        temp = data[i];
        data[i] = data[i - 1];
        data[i - 1] = temp;
        return i - 1;
      }
    }
    return -1;
  }

  /**
   * Perform experiment to test transpose-sequential-search heuristic.
   *
   * @param args method argument
   */
  public static void main(String[] args) {
    TransposeArraySet<String> test = new TransposeArraySet<>();
    test.insert("9");
    test.insert("12");
    test.insert("3");
    test.find("3");
    test.find("3");
    test.insert("4");
    test.find("9");
    test.insert("11");
    test.insert("7");
    test.find("11");
    test.remove("4");
    System.out.println(test.has("7") ? "The Deque contains 7." : "The Deque doesn't contain 7.");
    System.out.println("There are now " + test.size() + " elements in the Deque");
    test.remove("3");
    test.find("11");
    test.has("12");
    test.find("9");
  }
}
