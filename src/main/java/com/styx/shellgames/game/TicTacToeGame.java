package com.styx.shellgames.game;

import com.styx.shellgames.generic.ExitGameException;
import com.styx.shellgames.generic.GameBoard;
import com.styx.shellgames.utils.GameProperties;
import com.styx.shellgames.utils.print.formater.PrintFormatter;
import com.styx.shellgames.utils.scanner.ScanInput;
import com.styx.shellgames.utils.scanner.ScanInputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicTacToeGame extends Game {
    Map<Player, Integer> score = null;
    private char[][] grid = null;
    Player currentPlayer = null;

    public TicTacToeGame() {
        super();
        this.init();
    }

    @Override
    protected void init() {
        this.score = new HashMap<>() {{
            put(Player.FIRST, 0);
            put(Player.SECOND, 0);
        }};
    }

    @Override
    protected void reset() {
        this.grid = new char[][] {
                {'.', '.', '.'},
                {'.', '.', '.'},
                {'.', '.', '.'}
        };
        this.currentPlayer = Player.FIRST;
    }

    @Override
    public void startGame() throws ExitGameException {
        while (true) {
            this.reset();

            while (!this.isWon() && !this.isLost()) {
                try {
                    this.print();
                    System.out.printf("%s > ", GameProperties.getProperty("input.issue"));
                    int inputUser = ScanInput.ScanChooseMenu(9);

                    int row = (inputUser - 1) / 3;
                    int column = (inputUser - 1) % 3;

                    if (grid[row][column] == '.') {
                        grid[row][column] = currentPlayer == Player.FIRST ? 'X' : 'O';

                        if (!this.isWon()) {
                            currentPlayer = currentPlayer == Player.FIRST ? Player.SECOND : Player.FIRST;
                        }
                    } else {
                        throw new ScanInputException(GameProperties.getProperty("game.caseUsed"));
                    }
                } catch (ScanInputException e) {
                    GameBoard.errorMessage = e.getMessage();
                }
            }

            while (true) {
                try {
                    boolean showScore = GameBoard.errorMessage == null;
                    this.print();
                    if (showScore) {
                        if (isWon()) {
                            int newScore = this.score.get(this.currentPlayer) + 1;
                            this.score.put(this.currentPlayer, newScore);
                            System.out.printf("\u001B[32m%s\u001B[0m", GameProperties.getProperty("game.isWin").formatted(this.currentPlayer == Player.FIRST ? "Player 1" : "Player 2"));
                        } else if (this.isLost()) {
                            System.out.printf("\u001B[31m%s\u001B[0m", GameProperties.getProperty("game.isDraw"));
                        }
                    }

                    System.out.printf("%n%s > ", GameProperties.getProperty("game.again"));
                    char replayAnswer = ScanInput.ScanChar();
                    if (replayAnswer == 'O' || replayAnswer == 'o' || replayAnswer == 'Y' || replayAnswer == 'y') {
                        break;
                    }
                    return;
                } catch (ScanInputException e) {
                    GameBoard.errorMessage = e.getMessage();
                }
            }
        }
    }

    @Override
    protected boolean isWon() {
        for (int i=0; i < 3; i++) {
            boolean checkLine = grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2] && grid[i][2] != '.';
            boolean checkColumn = grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i] && grid[2][i] != '.';

            if (checkLine || checkColumn) {
                return true;
            }
        }
        boolean checkDiagonal1 = grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[2][2] != '.';
        boolean checkDiagonal2 = grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[2][0] != '.';

        return checkDiagonal1 || checkDiagonal2;
    }

    @Override
    protected boolean isLost() {
        for (char[] row : grid) {
            for (char cell : row) {
                if (cell == '.') {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    protected void print() {
        List<String> lines = new ArrayList<>();
        String gameName = GameProperties.getProperty("game.name");

        lines.add("Score : (X) Player 1 = %s | (O) Player 2 = %s".formatted(this.score.get(Player.FIRST), this.score.get(Player.SECOND)));
        lines.add("");

        int index = 0;
        for (char[] rows: grid) {
            if (index > 0) {
                lines.add("");
            }
            lines.add("%s  %s   %s   %s".formatted(" ".repeat(15), rows[0], rows[1], rows[2]));
            index++;
        }

        PrintFormatter.print(gameName, lines, 5);
    }

    public enum Player {
        FIRST,
        SECOND
    }
}
