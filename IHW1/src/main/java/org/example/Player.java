package org.example;

import java.util.ArrayList;

public class Player {
    private final Field gameField;
    private int bestScore;
    private final char color;

    Player(Field newField, char newColor) {
        bestScore = 2;
        gameField = newField;
        color = newColor;
    }
    protected int getBestScore() {
        return bestScore;
    }
    protected void setBestScore() {
        bestScore = Math.max(bestScore, estimateScore());
    }
    protected int estimateScore() {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameField.getColor(10 * i + j) == color) {
                    ++result;
                }
            }
        }
        return result;
    }

    protected char getColor() {
        return color;
    }

    protected ArrayList<String> getPossibleMoves(char color, char oppositeColor) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        char[] possibleMove = new char[2];

        for (int i = 0; i < gameField.getDeskSize(); i++) {
            for (int j = 0; j < gameField.getDeskSize(); j++) {
                if (gameField.isPossibleMove(10 * i + j, color, oppositeColor)) {
                    possibleMove[0] = (char) (j + 'A');
                    possibleMove[1] = (char) ((7 - i) + '1');
                    possibleMoves.add(new String(possibleMove));
                }
            }
        }
        return possibleMoves;
    }
}
