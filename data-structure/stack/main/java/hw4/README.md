# Discussion

**Document all error conditions you determined and why they are error
 conditions. Do this by including the inputs that you used to test your
  program and what error conditions they exposed:**

1.
. 20 30 + ? !

ERROR: Not enough arguments.

[50]

“.” Wants display the top value on the stack but in this case the stack is empty, so it should display error warning and continue.

2.
7 0 / ? !

ERROR: Divisor is 0.

[7, 0]

For a/b, b cannot be zero because 0 can’t be a divisor, it will cause ArithmeticException. Therefore, it should display error and not modify the stack.

3.
7 0 % ? !

ERROR: Divisor is 0.

[7, 0]

Similar as above, for a%b, b cannot be zero. Should display error and not modify the stack.

4.
hh hh . ? !

ERROR: bad token

ERROR: bad token

ERROR: Not enough arguments.

[]

When the user enters hh (or anything else that doesn't make sense for an integer calculator), it should display error and make it clear that it can't do anything helpful with that input; but it should not stop at that point.

5.
10 + 10 + ? !

ERROR: Not enough arguments.

[20]

For the first + sign, there is only one operand at the moment which is not enough. It should display error about it and leave the stack unchanged. The program doesn’t stop at that point. After another “10 +” is input, the two 10s become 20.
