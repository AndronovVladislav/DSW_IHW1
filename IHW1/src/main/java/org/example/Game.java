package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private final Field gameField;
    private final Player playerFirst;
    private final Player playerSecond;
    private final Scanner menu = new Scanner(System.in);
    private String difficulty;
    private String opponent;
    private final Bot bot;

    Game() {
        gameField = new Field();
        playerFirst = new Player(gameField, 'B');
        playerSecond = new Player(gameField, 'W');
        bot = new Bot(gameField, 'W');
        opponent = new String();
        difficulty = new String();
    }
    void start() {
        while (true) {
            System.out.println("Choose opponent:");
            opponent = menu.nextLine();
            if (opponent.equals("Computer")) {
                System.out.println("Choose difficulty:");
                while (!difficulty.equals("Normal") && !difficulty.equals("Hard")) {
                    difficulty = menu.nextLine();
                }
                playerAgainstAI();
                break;
            } else if (opponent.equals("Player")) {
                playerAgainstPlayer();
                break;
            }
        }
    }

    void playerAgainstAI() {
        int playerMove;
        ArrayList<String> possibleMoves;

        String desireToPlay = "y";
        while (!desireToPlay.equals("n")) {
            while (desireToPlay.equals("y")) {
                gameField.redraw('B', 'W');
                // получаем все возможные ходы игрока и печатаем их
                possibleMoves = playerFirst.getPossibleMoves(playerFirst.getColor(), bot.getColor());

                for (var possibleMove : possibleMoves) {
                    System.out.print(possibleMove + ' ');
                }
                System.out.print("\b\n");

                // если всё поле заполнено проверяем, кто выиграл
                if (gameField.getEmptyCells() == 0) {
                    if (playerFirst.estimateScore() > (64 - gameField.getEmptyCells()) / 2) {
                        System.out.println("Game over! You won!");
                    } else if (playerFirst.estimateScore() < (64 - gameField.getEmptyCells()) / 2) {
                        System.out.println("Game over! AI won!");
                    } else {
                        System.out.println("Game over! Draw!");
                    }
                    playerFirst.setBestScore();
                    break;
                }

                // если у игрока нет ходов, говорим, что выиграл бот, иначе считываем ход игрока
                if (gameField.noHasPossibleMove('B', 'W')) {
                    System.out.print("Game over! AI won!\n");
                    playerFirst.setBestScore();
                    break;
                } else {
                    playerMove = doMove();
                    if (playerMove == -1) {
                        continue;
                    } else if (playerMove == -2) {
                        break;
                    }
                    playerMove = (7 - playerMove / 10) * 10 + (playerMove % 10);
                }

                // если ход был некорректным, то уведомляем игрока и переходим в следующую итерацию
                // иначе обновляем доску
                if (gameField.badMoveCheck(playerMove, playerFirst.getColor(), bot.getColor())) {
                    System.out.println("Bad move!");
                    continue;
                } else {
                    gameField.recolorDesk(playerMove, playerFirst.getColor());
                    gameField.decreaseEmptyCells();
                }

                // проверяем есть ли у бота ходы, если нет - конец игры,
                // иначе - ход в зависимости от выбранной сложности
                if (gameField.noHasPossibleMove(bot.getColor(), playerFirst.getColor())) {
                    gameField.redraw(playerFirst.getColor(), bot.getColor());
                    System.out.print("Game over! You won!\n");
                    playerFirst.setBestScore();
                    break;
                } else {
                    bot.doMove(difficulty.equals("Normal") ? 1 : 2);
                    gameField.decreaseEmptyCells();
                }

                // обновляем историю, чтобы иметь возможность отменять ходы
                gameField.updateHistory();
            }
            System.out.println("Do you want to play again?(y/n)");
            desireToPlay = menu.nextLine();
        }
        System.out.printf("Your best score: %d points.\n", playerFirst.getBestScore());
    }

    private void playerAgainstPlayer() {
        int playerFirstMove;
        int playerSecondMove;
        ArrayList<String> possibleMoves;

        String desireToPlay = "y";
        while (!desireToPlay.equals("n")) {
            while (desireToPlay.equals("y")) {
                // если всё поле заполнено проверяем, кто выиграл
                if (gameField.getEmptyCells() == 0) {
                    if (playerFirst.estimateScore() > playerSecond.estimateScore()) {
                        System.out.println("Game over! Player1 won!");
                    } else if (playerFirst.estimateScore() < playerSecond.estimateScore()) {
                        System.out.println("Game over! Player2 won!");
                    } else {
                        System.out.println("Game over! Draw!");
                    }
                    playerFirst.setBestScore();
                    playerSecond.setBestScore();
                    break;
                }

                gameField.redraw(playerFirst.getColor(), playerSecond.getColor());
                // получаем все возможные ходы первого игрока и печатаем их
                possibleMoves = playerFirst.getPossibleMoves(playerFirst.getColor(), playerSecond.getColor());

                System.out.print("Player1's possible moves: ");
                for (var possibleMove : possibleMoves) {
                    System.out.print(possibleMove + ' ');
                }
                System.out.print("\b\n");

                // если у первого игрока нет ходов, говорим, что выиграл второй, иначе считываем ход первого игрока,
                // пока не будет введён корректный ход
                if (gameField.noHasPossibleMove(playerFirst.getColor(), playerSecond.getColor())) {
                    System.out.print("Game over! Player2 won!\n");
                    playerFirst.setBestScore();
                    playerSecond.setBestScore();
                    break;
                } else {
                    System.out.print("Player1 move: ");
                    playerFirstMove = doMove();
                    if (playerFirstMove == -1) {
                        gameField.cancelMove();
                        continue;
                    } else if (playerFirstMove == -2) {
                        break;
                    }
                    playerFirstMove = (7 - playerFirstMove / 10) * 10 + (playerFirstMove % 10);
                }

                // если ход плохой, переходим на следующую итерацию и спрашиваем снова
                if (gameField.badMoveCheck(playerFirstMove, playerFirst.getColor(), playerSecond.getColor())) {
                    System.out.println("Bad move!");
                    continue;
                } else {
                    gameField.recolorDesk(playerFirstMove, playerFirst.getColor());
                    gameField.updateHistory();
                    gameField.decreaseEmptyCells();
                }

                // получаем все возможные ходы второго игрока и печатаем их
                possibleMoves = playerSecond.getPossibleMoves(playerSecond.getColor(), playerFirst.getColor());
                gameField.redraw(playerSecond.getColor(), playerFirst.getColor());

                System.out.print("Player2's possible moves: ");
                for (var possibleMove : possibleMoves) {
                    System.out.print(possibleMove + ' ');
                }
                System.out.print("\b\n");

                if (gameField.noHasPossibleMove(playerSecond.getColor(), playerFirst.getColor())) {
                    gameField.redraw(playerFirst.getColor(), playerSecond.getColor());
                    System.out.print("Game over! Player1 won!\n");
                    playerFirst.setBestScore();
                    playerSecond.setBestScore();
                    break;
                } else {
                    System.out.print("Player2 move: ");
                    playerSecondMove = doMove();
                    if (playerSecondMove == -1) {
                        continue;
                    } else if (playerSecondMove == -2) {
                        break;
                    }
                    playerSecondMove = (7 - playerSecondMove / 10) * 10 + (playerSecondMove % 10);
                }

                if (gameField.badMoveCheck(playerSecondMove, playerSecond.getColor(), playerFirst.getColor())) {
                    System.out.println("Bad move!");
                    gameField.cancelMove();
                } else {
                    gameField.recolorDesk(playerSecondMove, playerSecond.getColor());
                    gameField.decreaseEmptyCells();
                }

                gameField.updateHistory();
            }
            System.out.println("Do you want to play again?(y/n)");
            desireToPlay = menu.nextLine();
        }
        System.out.printf("Player1's best score: %d points.\n", playerFirst.getBestScore());
        System.out.printf("Player2's best score: %d points.\n", playerSecond.getBestScore());
    }

    public int doMove() {
        String move;
        int returnCode;
        while (true) {
            move = menu.nextLine();
            if (move.length() >= 2) {
                if (move.equals("back")) {
                    gameField.cancelMove();
                    return -1;
                } else if (move.equals("quit")) {
                    return -2;
                }
                returnCode = move.charAt(0) - 'A';
                returnCode += 10 * (move.charAt(1) - '1');
                break;
            }
        }

        return returnCode;
    }
}
