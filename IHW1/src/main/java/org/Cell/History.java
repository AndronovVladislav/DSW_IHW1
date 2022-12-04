package org.Cell;

import java.util.Stack;

public class History {
    private final Stack<Character> history;

    History() {
        history = new Stack<>();
    }

    protected void pop() {
        history.pop();
    }

    protected char top() {
        return history.peek();
    }

    protected void push(char newColor) {
        history.push(newColor);
    }

    protected int size() {
        return history.size();
    }
}
