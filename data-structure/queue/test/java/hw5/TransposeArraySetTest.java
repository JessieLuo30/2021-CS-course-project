package hw5;

/**
 * Instantiate the TransposeArraySetTest to test.
 */
public class TransposeArraySetTest extends SetTest {

  @Override
  protected Set<String> createUnit() {
    return new TransposeArraySet<>();
  }

}