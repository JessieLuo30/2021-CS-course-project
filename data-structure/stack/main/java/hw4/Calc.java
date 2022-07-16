package hw4;

import exceptions.EmptyException;
import java.util.Scanner;

/**
 * A program for an RPN calculator that uses a stack.
 */
public final class Calc {
  /**
   * The main function.
   *
   * @param args Not used.
   */
  public static void main(String[] args) {
    LinkedStack<Integer> build = new LinkedStack<>();
    Scanner scan = new Scanner(System.in);
    int stop = 0;
    while (stop != 2 && scan.hasNext()) {
      while (scan.hasNextInt()) {
        build.push(scan.nextInt());
      }
      String input = scan.next();
      stop = command(build, input);
      if (stop == 2 || stop == 1) {
        continue;
      }
      if (nonSense(input)) {
        System.out.println("ERROR: bad token");
        continue;
      }
      operator(build, input);
    }
  }

  /**
   * Removes the top element.
   * @param build the stack we are getting int from
   * @return the int on top of stack build.
   * @throws EmptyException when empty() == true.
   */
  private static int moveTop(LinkedStack<Integer> build) throws EmptyException {
    if (build.empty()) {
      throw new EmptyException();
    }
    int b = build.top();
    build.pop();
    return b;
  }

  /**
   * Make change on stack based on what operator we got.
   * @param build the stack we are operating on
   * @param input the operator we are on
   */
  private static void operator(LinkedStack<Integer> build, String input) {
    int a;
    int b;
    try {
      b = moveTop(build);
    } catch (EmptyException except) {
      System.out.println("ERROR: Not enough arguments.");
      return;
    }
    try {
      a = moveTop(build);
    } catch (EmptyException except) {
      System.out.println("ERROR: Not enough arguments.");
      build.push(b);
      return;
    }
    operatorSwitch(a, b, build, input);
  }

  /**
   * Determine which operator it is and react accordingly.
   * @param a the first operand
   * @param b the second operand
   * @param build the stack we are operating on
   * @param input the operator we are on
   */
  private static void operatorSwitch(int a, int b, LinkedStack<Integer> build, String input) {
    if ("+".equals(input)) {
      build.push(a + b);
    } else if ("-".equals(input)) {
      build.push(a - b);
    } else if ("*".equals(input)) {
      build.push(a * b);
    } else if ("/".equals(input)) {
      if (assetZero(build, a, b)) {
        build.push(a / b);
      }
    } else if ("%".equals(input)) {
      if (assetZero(build, a, b)) {
        build.push(a % b);
      }
    }
  }

  /**
   * Determine whether divisor is zero.
   * @param build the stack we put a and b back to if b is 0
   * @param a the dividend
   * @param b divisor
   * @return false b is zero.
   */
  private static boolean assetZero(LinkedStack<Integer> build, int a, int b) {
    if (b == 0) {
      System.out.println("ERROR: Divisor is 0.");
      build.push(a);
      build.push(b);
      return false;
    }
    return true;
  }

  /**
   * Make change on stack based on what operator we got.
   * @param str the stack we are operating on
   * @return true if the string doesn't match any operators.
   */
  private static boolean nonSense(String str) {
    if ("+".equals(str)) {
      return false;
    } else if ("-".equals(str)) {
      return false;
    } else if ("*".equals(str)) {
      return false;
    } else if ("/".equals(str)) {
      return false;
    } else {
      return !"%".equals(str);
    }
  }

  /**
   * Response based on what special command (probably not a command) we got.
   * @param build the stack we are operating on
   * @param input the special command we are on
   * @return 2 if received stop (!), 1 if identify an command, 0 if it's not a command
   */
  private static int command(LinkedStack<Integer> build, String input) {
    switch (input) {
      case "!":
        return 2;
      case ".":
        dot(build);
        return 1;
      case "?":
        System.out.println(build.toString());
        return 1;
      default:
        return 0;
    }
  }

  /**
   * Make change on stack if command is dot.
   * @param build the stack we are operating on
   */
  private static void dot(LinkedStack<Integer> build) {
    int top;
    try {
      top = build.top();
    } catch (EmptyException except) {
      System.out.println("ERROR: Not enough arguments.");
      return;
    }
    System.out.println(top);
  }
}