package org.example;

public class Cell {
    private char color;
    private final History cellHistory;

    Cell() {
        color = 'o';
        cellHistory = new History();
        cellHistory.push(color);
    }

    Cell(char startColor) {
        cellHistory = new History();
        color = startColor;
        cellHistory.push(color);
    }

    char getColor() {
        return color;
    }

    void setColor(char newColor) {
        color = newColor;
    }

    void addToHistory(char color) {
        cellHistory.push(color);
    }

    boolean stepBack() {
        boolean res = color != 'o';
        if (cellHistory.size() >= 2) {
            cellHistory.pop();
            color = cellHistory.top();
        }
        return res && (color == 'o');
    }
}
