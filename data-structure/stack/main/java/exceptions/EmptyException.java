package exceptions;

/**
 * Exception for empty data structure.
 * Data structures that can be empty throw EmptyException
 * if asked to produce a value when they have none.
 */
public class EmptyException extends RuntimeException {
  /**
   * Constructs a new EmptyException.
   */
  public EmptyException() {
  }

  /**
   * Constructs a new EmptyException with the specified detail message.
   *
   * @param message the detail message. The detail message is saved for
   *                later retrieval by the getMessage() method.
   */
  public EmptyException(String message) {
    super(message);
  }
}