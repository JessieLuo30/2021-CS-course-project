package hw5;

/**
 * Instantiate the MoveToFrontLinkedSetTest to test.
 */
public class MoveToFrontLinkedSetTest extends SetTest {
  @Override
  protected Set<String> createUnit() {
    return new MoveToFrontLinkedSet<>();
  }
}