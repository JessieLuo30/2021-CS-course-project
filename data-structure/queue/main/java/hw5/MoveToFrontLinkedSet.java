package hw5;

/**
 * Set implemented using a doubly linked list and move-to-front heuristic.
 *
 * @param <T> Element type.
 */
public class MoveToFrontLinkedSet<T> extends LinkedSet<T> {

  @Override
  protected Node<T> find(T t) {
    for (Node<T> n = head; n != null; n = n.next) {
      if (n.data.equals(t)) {
        remove(n);
        prepend(n);
        return n;
      }
    }
    return null;
  }

  private void prepend(Node<T> target) {
    if (head == null) {
      head = target;
      tail = head;
    } else {
      target.prev = null;
      head.prev = target;
      target.next = head;
      head = target;
    }
  }

  /**
   * Perform experiment to test move-to-front heuristic.
   *
   * @param args method argument
   */
  public static void main(String[] args) {
    MoveToFrontLinkedSet<String> test = new MoveToFrontLinkedSet<>();
    test.insert("9");
    test.insert("3");
    test.append("3");
    test.insert("4");
    test.remove("3");
    test.insert("3");
    test.remove("2");
    test.find("4");
    test.find("9");
    test.append("6");
    System.out.println(test.has("6") ? "The Deque contains 6." : "The Deque doesn't contain 6.");
    System.out.println("There are now " + test.size() + " elements in the Deque");
    test.remove("3");
    test.insert("11");
    test.find("11");
    test.insert("12");
    test.has("12");
  }

}
