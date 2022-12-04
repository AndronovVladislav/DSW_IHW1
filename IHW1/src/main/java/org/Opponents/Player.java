package org.Opponents;

import org.Field.Field;
import java.util.ArrayList;

public class Player {
    private final Field gameField;
    private int bestScore;
    private final char color;

    public Player(Field newField, char newColor) {
        bestScore = 2;
        gameField = newField;
        color = newColor;
    }
    public int getBestScore() {
        return bestScore;
    }
    public void setBestScore() {
        bestScore = Math.max(bestScore, estimateScore());
    }
    public int estimateScore() {
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

    public char getColor() {
        return color;
    }

    public ArrayList<String> getPossibleMoves(char color, char oppositeColor) {
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
