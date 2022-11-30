package org.example;

import java.util.Stack;

public class History {
    private final Stack<Character> history;

    History() {
        history = new Stack<>();
    }

    void pop() {
        history.pop();
    }

    char top() {
        return history.peek();
    }

    void push(char newColor) {
        history.push(newColor);
    }

    int size() {
        return history.size();
    }
}
