package com.styx.shellgames.generic;

import com.styx.shellgames.utils.ApplicationProperties;
import com.styx.shellgames.utils.ErrorsProperties;
import com.styx.shellgames.utils.GameProperties;
import com.styx.shellgames.utils.print.formater.PrintFormatter;
import com.styx.shellgames.utils.scanner.ScanInput;
import com.styx.shellgames.utils.scanner.ScanInputException;

import java.util.*;

public class GameBoard {
    public static String errorMessage = null;
    private final Map<Integer, String[]> languagesAvailable = new HashMap<>();
    private final Map<Integer, String[]> gamesName = new HashMap<>();
    public static String languageSelected;

    public GameBoard() {
        try {
            this.initializeGame();
            while (true) {
                try {
                    this.selectLanguage();
                    this.selectGame();
                    break;
                } catch (NumberFormatException e) {
                    GameBoard.errorMessage = ErrorsProperties
                            .getProperty("input.notInteger")
                            .formatted(this.languagesAvailable.size());
                } catch (ScanInputException e) {
                    GameBoard.errorMessage = e.getMessage();
                }
            }

        } catch (MissingResourceException e) {
            System.out.println("No language available, please check the 'application.properties' file.");
            System.exit(0);
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

        if (GameBoard.languageSelected != this.languagesAvailable.get(1)[0]) {
            GameBoard.languageSelected = this.languagesAvailable.get(1)[0];
            loadGameByLanguage();
        }

        ErrorsProperties.loadProperties(GameBoard.languageSelected);
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

    private void selectLanguage() throws ScanInputException {

        List<String> lines = new ArrayList<>();

        for (Map.Entry<Integer, String[]> entry : this.languagesAvailable.entrySet()) {
            Integer key = entry.getKey();
            String[] value = entry.getValue();
            lines.add("%d - %s".formatted(key, value[1]));
        }

        PrintFormatter.print("Select a language", lines);

        System.out.print("Enter your choice > ");
        int input = ScanInput.ScanChooseMenu(this.languagesAvailable.size());

        GameBoard.languageSelected = this.languagesAvailable.get(input)[0];
    }

    private void selectGame() throws ScanInputException {
        List<String> lines = new ArrayList<>();

        for (Map.Entry<Integer, String[]> entry : this.gamesName.entrySet()) {
            Integer key = entry.getKey();
            String[] value = entry.getValue();
            lines.add("%d - %s".formatted(key, value[1]));
        }

        PrintFormatter.print("Select a game", lines);

        System.out.print("Enter your choice > ");
        int input = ScanInput.ScanChooseMenu(this.gamesName.size());

        GameProperties.loadProperties(this.gamesName.get(input)[0]);
    }

}
