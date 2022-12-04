package org.Cell;

public class Cell {
    private char color;
    private final History cellHistory;

    public Cell() {
        color = 'o';
        cellHistory = new History();
        cellHistory.push(color);
    }

    public Cell(char startColor) {
        cellHistory = new History();
        color = startColor;
        cellHistory.push(color);
    }

    public char getColor() {
        return color;
    }

    public void setColor(char newColor) {
        color = newColor;
    }

    public void addToHistory(char color) {
        cellHistory.push(color);
    }

    public boolean stepBack() {
        boolean res = color != 'o';
        if (cellHistory.size() >= 2) {
            cellHistory.pop();
            color = cellHistory.top();
        }
        return res && (color == 'o');
    }
}
