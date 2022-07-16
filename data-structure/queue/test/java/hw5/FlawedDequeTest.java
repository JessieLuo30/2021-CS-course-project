package hw5;

import obfuscate.FlawedDeque;

public class FlawedDequeTest extends DequeTest {

    @Override
    protected Deque<String> createDeque() {
        return new FlawedDeque<>();
    }
}