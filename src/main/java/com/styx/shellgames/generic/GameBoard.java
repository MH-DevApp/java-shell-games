package com.styx.shellgames.generic;

import com.styx.shellgames.game.Game;
import com.styx.shellgames.utils.ApplicationProperties;
import com.styx.shellgames.utils.ErrorsProperties;
import com.styx.shellgames.utils.GameProperties;
import com.styx.shellgames.utils.print.formater.PrintFormatter;
import com.styx.shellgames.utils.scanner.ScanInput;
import com.styx.shellgames.utils.scanner.ScanInputException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GameBoard {
    public static String errorMessage = null;
    private final Map<Integer, String[]> languagesAvailable = new HashMap<>();
    private final Map<Integer, String[]> gamesName = new HashMap<>();
    public static String languageSelected;
    private Game game;

    public GameBoard() {
        try {
            this.initializeGame();
            this.selectLanguage();
            while (true) {
                try {
                    this.selectGame();
                    this.game.startGame();

                    Character answer = null;
                    while (answer == null) {
                        try {
                            if (GameBoard.errorMessage != null) {
                                System.out.print("\u001B[31m");
                                System.out.println(GameBoard.errorMessage);
                                System.out.print("\u001B[0m");
                                GameBoard.errorMessage = null;
                            }

                            System.out.println(ApplicationProperties.getProperty("%s.exit".formatted(GameBoard.languageSelected)));
                            answer = ScanInput.ScanChar();
                        } catch (ScanInputException e) {
                            GameBoard.errorMessage = e.getMessage();
                        }
                    }

                    if (answer.equals('y') || answer.equals('Y') || answer.equals('o') || answer.equals('O')) {
                        break;
                    }
                } catch (ExitGameException e) {
                    throw new ExitGameException(e.getMessage());
                }
            }
        } catch (MissingResourceException e) {
            System.out.println("\n\u001B[31m" + e.getMessage() + "\u001B[0m");
            System.exit(0);
        } catch (ExitGameException e) {
            System.out.println("\n\u001B[31m" + e.getMessage() + "\u001B[0m");
        }
    }

    private void initializeGame() {
        ApplicationProperties.loadProperties();
        this.initializeDefaultLanguage();
        this.loadGameByLanguage();
    }

    private void initializeDefaultLanguage() throws MissingResourceException {
        String languagesAvailable = ApplicationProperties.getProperty("languages.available");

        if (languagesAvailable.isEmpty()) {
            throw new MissingResourceException(
                    "No language available, please check the 'application.properties' file.",
                    "ApplicationProperties",
                    "languages.available"
            );
        }

        Arrays.stream(
                languagesAvailable
                        .split(",")
        ).forEach((language) -> {
            String[] languages = language.split("\\|");
            this.languagesAvailable.put(this.languagesAvailable.size() + 1, languages);
        });

        GameBoard.languageSelected = ApplicationProperties.getProperty("languages.default");
        this.loadGameByLanguage();
        this.loadErrorsByLanguage();
    }

    private void loadGameByLanguage() {
        this.gamesName.clear();
        String gamesName = ApplicationProperties.getProperty(GameBoard.languageSelected + ".games.name");

        if (gamesName.isEmpty()) {
            throw new MissingResourceException(
                    "No game available, please check the 'application.properties' file.",
                    "ApplicationProperties",
                    "games.available"
            );
        }

        Arrays.stream(
                gamesName
                        .split(",")
        ).forEach((game) -> {
            String[] games = game.split("\\|");
            this.gamesName.put(this.gamesName.size() + 1, games);
        });
    }

    private void loadErrorsByLanguage() {
        ErrorsProperties.loadProperties(GameBoard.languageSelected);
    }

    private void selectLanguage() throws ExitGameException {
        while (true) {
            try {
                List<String> lines = new ArrayList<>();

                for (Map.Entry<Integer, String[]> entry : this.languagesAvailable.entrySet()) {
                    Integer key = entry.getKey();
                    String[] value = entry.getValue();
                    lines.add("%d - %s".formatted(key, value[1]));
                }

                PrintFormatter.print("Select a language", lines);

                System.out.print("Enter your choice > ");
                int input = ScanInput.ScanChooseMenu(this.languagesAvailable.size());

                if (!GameBoard.languageSelected.equals(this.languagesAvailable.get(input)[0])) {
                    GameBoard.languageSelected = this.languagesAvailable.get(input)[0];
                    this.loadGameByLanguage();
                    this.loadErrorsByLanguage();
                }
                break;
            } catch (NumberFormatException e) {
                GameBoard.errorMessage = ErrorsProperties
                        .getProperty("input.notInteger")
                        .formatted(this.languagesAvailable.size());
            } catch (ScanInputException e) {
                GameBoard.errorMessage = e.getMessage();
            }
        }
    }

    private void selectGame() throws ExitGameException, MissingResourceException {
        while (true) {
            try {
                List<String> lines = new ArrayList<>();

                for (Map.Entry<Integer, String[]> entry : this.gamesName.entrySet()) {
                    Integer key = entry.getKey();
                    String[] value = entry.getValue();
                    lines.add("%d - %s".formatted(key, value[1]));
                }

                PrintFormatter.print("%s".formatted(ApplicationProperties.getProperty("%s.select.game".formatted(GameBoard.languageSelected))), lines);

                System.out.printf("%s > ", ApplicationProperties.getProperty("%s.choice".formatted(GameBoard.languageSelected)));
                int input = ScanInput.ScanChooseMenu(this.gamesName.size());

                GameProperties.loadProperties(this.gamesName.get(input)[0]);
                this.game = (Game) Class.forName(this.gamesName.get(input)[2]).getDeclaredConstructor().newInstance();
                break;
            } catch (NumberFormatException e) {
                GameBoard.errorMessage = ErrorsProperties
                        .getProperty("input.notInteger")
                        .formatted(this.languagesAvailable.size());
            } catch (ScanInputException e) {
                GameBoard.errorMessage = e.getMessage();
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new ExitGameException(e.getMessage());
            } catch (MissingResourceException e) {
                throw new MissingResourceException(
                        "class not defined, please check the 'i18n_[lang]_[game].properties' file.",
                        "GameProperties",
                        "class"
                );
            }
        }
    }

}
