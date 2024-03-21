package com.styx.shellgames.game;

import com.styx.shellgames.generic.ExitGameException;
import com.styx.shellgames.generic.GameBoard;
import com.styx.shellgames.utils.ErrorsProperties;
import com.styx.shellgames.utils.GameProperties;
import com.styx.shellgames.utils.print.formater.PrintFormatter;
import com.styx.shellgames.utils.scanner.ScanInput;
import com.styx.shellgames.utils.scanner.ScanInputException;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class HangmanGame extends Game {
    private int score;
    private int lifePoints;
    private final String PATH = "data/hangman/";
    private final List<String> words = new ArrayList<>();
    private final List<Character> secretWord = new ArrayList<>();
    private final List<Character> guessWord = new ArrayList<>();
    private final Set<Character> guessedLetters = new HashSet<>();

    public HangmanGame() {
        super();
        this.init();
    }

    @Override
    protected void init() {
        String fileName = "words_%s.txt".formatted(GameBoard.languageSelected);
        File file = new File(PATH + fileName);
        this.score = 0;

        if (!file.exists()) {
            GameBoard.errorMessage = ErrorsProperties.getProperty("file.notFound").formatted(fileName);
            return;
        }

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            fileReader.lines().forEach(words::add);
            Collections.shuffle(words);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void reset() {
        this.lifePoints = 10;
        this.secretWord.clear();
        this.guessWord.clear();
        this.guessedLetters.clear();
        String wordToGuess = words.get(new Random().nextInt(words.size())).toUpperCase();

        for (char c : wordToGuess.toCharArray()) {
            secretWord.add(c);
            guessWord.add('_');
        }
    }

    @Override
    public void startGame() throws ExitGameException {
        while (true) {
            this.reset();

            while (!this.isWon() && !this.isLost()) {
                try {
                    this.print();
                    char guessLetter = this.scanLetter();
                    this.guessLetter(guessLetter);
                } catch (ScanInputException e) {
                    GameBoard.errorMessage = e.getMessage();
                }
            }

            if (this.isWon()) {
                this.score++;
                while (true) {
                    try {
                        boolean showScore = GameBoard.errorMessage == null;
                        this.print();
                        if (showScore) {
                            System.out.printf("\u001B[32m%s\u001B[0m", GameProperties.getProperty("game.isWin").formatted(this.secretWord.toString(), this.score));
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

            if (this.isLost()) {
                this.print();
                System.out.printf("\u001B[31m%s\u001B[0m", GameProperties.getProperty("game.isLost").formatted(this.secretWord.toString(), this.score));
                ScanInput.ScanEnter();
                return;
            }
        }
    }

    private char scanLetter() throws ScanInputException, ExitGameException {
        System.out.printf("%s > ", GameProperties.getProperty("input.guessLetter"));
        final char replayAnswer = String.valueOf(ScanInput.ScanChar()).toUpperCase().charAt(0);

        if (!Pattern.matches("[A-Z]", replayAnswer + "")) {
            throw new ScanInputException(ErrorsProperties.getProperty("input.notSpecialChar"));
        }

        return replayAnswer;
    }

    private void guessLetter(char letter) {
        guessedLetters.add(letter);

        if (secretWord.contains(letter) && !guessWord.contains(letter)) {
            int index = 0;
            for (char c : secretWord) {
                if (c == letter) {
                    guessWord.set(index, letter);
                }
                index++;
            }
        } else {
            lifePoints--;
        }
    }

    @Override
    protected boolean isWon() {
        return !guessWord.contains('_');
    }

    @Override
    protected boolean isLost() {
        return this.lifePoints <= 0;
    }

    @Override
    protected void print() {
        List<String> lines = new ArrayList<>();
        String gameName = GameProperties.getProperty("game.name");

        lines.add("%s : %s".formatted(GameProperties.getProperty("game.foundWords"), this.score));
        lines.add("");
        lines.add("❤ ".repeat(lifePoints));
        lines.add("");
        lines.add("%s : %s".formatted(GameProperties.getProperty("game.usedLetters"), guessedLetters));
        lines.add("");
        lines.add("%s".formatted(guessWord));

        PrintFormatter.print(gameName, lines, 5);
    }
}
