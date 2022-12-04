package org.example;

public class Field {
    final int deskSize = 8;
    private final Cell[][] field = new Cell[deskSize][deskSize];
    private int emptyCells;
    Field () {
        for (int i = 0; i < deskSize; ++i) {
            for (int j = 0; j < deskSize; ++j) {
                if (i == 3 && j == 3) {
                    field[i][j] = new Cell('W');
                } else if (i == 3 && j == 4) {
                    field[i][j] = new Cell('B');
                } else if (i == 4 && j == 3) {
                    field[i][j] = new Cell('B');
                } else if (i == 4 && j == 4) {
                    field[i][j] = new Cell('W');
                } else {
                    field[i][j] = new Cell();
                }
            }
        }

        emptyCells = 60;
    }

     protected void redraw(char color, char oppositeColor) {
        System.out.print("\n\n\n\n\n\n\n\n");
        System.out.println(Colors.ANSI_YELLOW + "    A  B  C  D  E  F  G  H   " + Colors.ANSI_RESET);

        for (int i = 0; i < deskSize; i++) {
            System.out.print(Colors.ANSI_YELLOW + (9 - (i + 1)) + "  " + Colors.ANSI_RESET);
            for (int j = 0; j < deskSize; ++j) {
                if ((i + j) % 2 == 1) {
                    System.out.print(Colors.ANSI_BLACK_BACKGROUND_BRIGHT + " ");
                } else {
                    System.out.print(Colors.ANSI_WHITE_BACKGROUND_BRIGHT + " ");
                }

                if (field[i][j].getColor() == 'W') {
                    System.out.print(Colors.ANSI_RED + "●" + Colors.ANSI_RESET);
                } else if (field[i][j].getColor() == 'B') {
                    System.out.print(Colors.ANSI_BLUE + "●" + Colors.ANSI_RESET);
                } else if (isPossibleMove(10 * i + j, color, oppositeColor)) {
                    System.out.print(Colors.ANSI_GREEN_BACKGROUND + "\b   " + Colors.ANSI_RESET);
                } else {
                    System.out.print(" ");
                }

                if (!isPossibleMove(10 * i + j, color, oppositeColor)) {
                    if ((i + j) % 2 == 1) {
                        System.out.print(Colors.ANSI_BLACK_BACKGROUND_BRIGHT + " " + Colors.ANSI_RESET);
                    } else {
                        System.out.print(Colors.ANSI_WHITE_BACKGROUND_BRIGHT + " " + Colors.ANSI_RESET);
                    }
                }
            }
            System.out.print("  " + Colors.ANSI_YELLOW + (9 - (i + 1)) + Colors.ANSI_RESET);
            System.out.println();
        }
        System.out.println(Colors.ANSI_YELLOW + "    A  B  C  D  E  F  G  H" + Colors.ANSI_RESET);
    }

    protected char getColor(int cell) {
        return field[cell / 10][cell % 10].getColor();
    }

    protected Cell getCell(int cell) {
        return field[cell / 10][cell % 10];
    }

    protected void setCell(int cell, char color) {
        field[cell / 10][cell % 10].setColor(color);
    }

    protected int getDeskSize() {
        return deskSize;
    }

    protected boolean badMoveCheck(int cell, char color, char oppositeColor) {
        boolean oppositeColorNear = false;

        if (cell > 77 || cell < 0 || cell % 10 > 7 || field[cell / 10][cell % 10].getColor() != 'o') {
            return true;
        }

        int lowerBound = Math.min(Math.max((cell - 10) / 10, 0), 7);
        int upperBound = Math.max(Math.min((cell + 10) / 10, 7), 0);
        int leftBound = Math.min(Math.max(cell % 10 - 1, 0), 7);
        int rightBound = Math.max(Math.min(cell % 10 + 1, 7), 0);

        for (int i = lowerBound; i < upperBound + 1; ++i) {
            for (int j = leftBound; j < rightBound + 1; ++j) {
                if (field[i][j].getColor() == oppositeColor) {
                    oppositeColorNear = true;
                    break;
                }
            }
        }

        boolean oppositeColorClosed = detectSameColorCell(cell, 0, -10, color);

        // right-up
        if (detectSameColorCell(cell, 1, -10, color)) {
            oppositeColorClosed = true;
        }

        // right
        if (detectSameColorCell(cell, 1, 0, color)) {
            oppositeColorClosed = true;
        }

        // right-down
        if (detectSameColorCell(cell, 1, 10, color)) {
            oppositeColorClosed = true;
        }

        // down
        if (detectSameColorCell(cell, 0, 10, color)) {
            oppositeColorClosed = true;
        }

        // left-down
        if (detectSameColorCell(cell, -1, 10, color)) {
            oppositeColorClosed = true;
        }

        // left
        if (detectSameColorCell(cell, -1, 0, color)) {
            oppositeColorClosed = true;
        }

        // left-up
        if (detectSameColorCell(cell, -1, -10, color)) {
            oppositeColorClosed = true;
        }

        return !oppositeColorNear || !oppositeColorClosed;
    }

    protected void recolorDesk(int cell, char color) {
        boolean oppositeColorNotClosed = true;

        // up
        if (detectSameColorCell(cell, 0, -10, color)) {
            recolorWay(cell, 0, -10, color);
            oppositeColorNotClosed = false;
        }

        // right-up
        if (detectSameColorCell(cell, 1, -10, color)) {
            recolorWay(cell, 1, -10, color);
            oppositeColorNotClosed = false;
        }

        // right
        if (detectSameColorCell(cell, 1, 0, color)) {
            recolorWay(cell, 1, 0, color);
            oppositeColorNotClosed = false;
        }

        // right-down
        if (detectSameColorCell(cell, 1, 10, color)) {
            recolorWay(cell, 1, 10, color);
            oppositeColorNotClosed = false;
        }

        // down
        if (detectSameColorCell(cell, 0, 10, color)) {
            recolorWay(cell, 0, 10, color);
            oppositeColorNotClosed = false;
        }

        // left-down
        if (detectSameColorCell(cell, -1, 10, color)) {
            recolorWay(cell, -1, 10, color);
            oppositeColorNotClosed = false;
        }

        // left
        if (detectSameColorCell(cell, -1, 0, color)) {
            recolorWay(cell, -1, 0, color);
            oppositeColorNotClosed = false;
        }

        // left-up
        if (detectSameColorCell(cell, -1, -10, color)) {
            recolorWay(cell, -1, -10, color);
            oppositeColorNotClosed = false;
        }

        if (!oppositeColorNotClosed) {
            field[cell / 10][cell % 10].setColor(color);
        }
    }

     protected boolean detectSameColorCell(int cell, int horizontalShift, int verticalShift, char color) {
        int currentCell = cell;
        boolean detectedSame = false;
        boolean detectedOpposite = false;

        while (((currentCell + horizontalShift) % 10 < 8) && (currentCell + horizontalShift >= 0) &&
                (currentCell + verticalShift < 78) && (currentCell + verticalShift >= 0)) {
            currentCell += horizontalShift + verticalShift;
            if (field[currentCell / 10][currentCell % 10].getColor() == 'o') {
                break;
            }

            if (field[currentCell / 10][currentCell % 10].getColor() == color) {
                detectedSame = true;
                break;
            } else {
                detectedOpposite = true;
            }
        }

        return detectedSame && detectedOpposite;
    }

    private void recolorWay(int cell, int horizontalShift, int verticalShift, char color) {
        int currentCell = cell;

        while (((currentCell + horizontalShift) % 10 < 8) &&
                (currentCell + horizontalShift > 0) && (currentCell + verticalShift < 78)) {
            currentCell += horizontalShift + verticalShift;
            if (field[currentCell / 10][currentCell % 10].getColor() == color) {
                break;
            }
            field[currentCell / 10][currentCell % 10].setColor(color);
        }
    }

    protected boolean isPossibleMove(int cell, char color, char oppositeColor) {
        return !badMoveCheck(cell, color, oppositeColor);
    }

    protected boolean noHasPossibleMove(char color, char oppositeColor) {
        for (int i = 0; i < deskSize; i++) {
            for (int j = 0; j < deskSize; j++) {
                if (isPossibleMove(10 * i + j, color, oppositeColor)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void decreaseEmptyCells() {
        --emptyCells;
    }

    protected int getEmptyCells() {
        return emptyCells;
    }

    protected void cancelMove() {
        for (int i = 0; i < deskSize; i++) {
            for (int j = 0; j < deskSize; j++) {
                if (field[i][j].stepBack()) {
                    ++emptyCells;
                }
            }
        }
    }

    protected void updateHistory() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                field[i][j].addToHistory(field[i][j].getColor());
            }
        }
    }
}
