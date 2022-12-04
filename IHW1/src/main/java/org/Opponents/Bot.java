package org.Opponents;

import org.Field.Field;

public class Bot {
     private final Field gameField;
     private final char color;

     public Bot(Field newGameField, char newColor) {
         gameField = newGameField;
         color = newColor;
     }

     public char getColor() {
         return color;
     }

    public void doMove(int depth) {
        double maxSum = -100;
        double currentSum;
        int iMax = 0;
        int jMax = 0;

        if (depth == 0) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    currentSum = estimateCell(10 * i + j);
                    if (currentSum > maxSum) {
                        maxSum = currentSum;
                        iMax = i;
                        jMax = j;
                    }
                }
            }
        } else {
            // обходим всю доску
            for (int i = 0; i < gameField.getDeskSize(); i++) {
                for (int j = 0; j < gameField.getDeskSize(); j++) {
                    // если бот может сходить на клетку - нужно её оценить
                    if (!gameField.badMoveCheck(10 * i + j, 'W', 'B')) {
                        currentSum = estimateCell(10 * i + j);
                        gameField.getCell(10 * i + j).addToHistory(gameField.getColor(10 * i + j));
                        gameField.setCell(10 * i + j, 'W');
                        // после того как мы оценили выигрыш от хода, нужно посчитать максимальный возможный проигрыш
                        // от следующего хода
                        double maxMinus = 0;
                        for (int k = 0; k < gameField.getDeskSize(); k++) {
                            for (int l = 0; l < gameField.getDeskSize(); l++) {
                                maxMinus = Math.max(maxMinus, estimateCell(10 * k + l));
                            }
                        }

                        currentSum -= maxMinus;

                        if (currentSum > maxSum) {
                            maxSum = currentSum;
                            iMax = i;
                            jMax = j;
                        }

                        gameField.getCell(10 * i + j).stepBack();
                    }
                }
            }
        }

        gameField.recolorDesk(10 * iMax + jMax, 'W');
    }

    private double baseEstimator(int cell) {
        double result = 0;

        // up
        if (gameField.detectSameColorCell(cell, 0, -10, 'W')) {
            result += estimateWay(cell, 0, -10);
        }

        // right-up
        if (gameField.detectSameColorCell(cell, 1, -10, 'W')) {
            result += estimateWay(cell, 1, -10);
        }

        // right
        if (gameField.detectSameColorCell(cell, 1, 0, 'W')) {
            result += estimateWay(cell, 1, 0);
        }

        // right-down
        if (gameField.detectSameColorCell(cell, 1, 10, 'W')) {
            result += estimateWay(cell, 1, 10);
        }

        // down
        if (gameField.detectSameColorCell(cell, 0, 10, 'W')) {
            result += estimateWay(cell, 0, 10);
        }

        // left-down
        if (gameField.detectSameColorCell(cell, -1, 10, 'W')) {
            result += estimateWay(cell, -1, 10);
        }

        // left
        if (gameField.detectSameColorCell(cell, -1, 0, 'W')) {
            result += estimateWay(cell, -1, 0);
        }

        // left-up
        if (gameField.detectSameColorCell(cell, -1, -10, 'W')) {
            result += estimateWay(cell, -1, -10);
        }

        return result;
    }

    private double estimateWay(int cell, int horizontalShift, int verticalShift) {
        int currentCell = cell;
        double result = 0;

        while (((currentCell + horizontalShift) % 10 < 8) &&
                (currentCell + horizontalShift > 0) && (currentCell + verticalShift < 78)) {

            currentCell += horizontalShift + verticalShift;
            if (gameField.getColor(currentCell) == 'W') {
                break;
            }

            if (currentCell / 10 == 0 || currentCell / 10 == 7 ||
                    currentCell % 10 == 0 || currentCell % 10 == 7) {
                result += 2;
            } else {
                result += 1;
            }
        }
        return result;
    }

    private double estimateCell(int cell) {
        double result = 0;

        if (!gameField.badMoveCheck(cell, 'W', 'B')) {
            result = baseEstimator(cell);
            if (cell / 10 == 0 || cell / 10 == 7) {
                result += 0.4;
            }

            if (cell % 10 == 0 || cell % 10 == 8) {
                result += 0.4;
            }
        }

        return result;
    }
}
