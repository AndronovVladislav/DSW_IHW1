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

    protected char getColor() {
        return color;
    }

    protected void setColor(char newColor) {
        color = newColor;
    }

    protected void addToHistory(char color) {
        cellHistory.push(color);
    }

    protected boolean stepBack() {
        boolean res = color != 'o';
        if (cellHistory.size() >= 2) {
            cellHistory.pop();
            color = cellHistory.top();
        }
        return res && (color == 'o');
    }
}
