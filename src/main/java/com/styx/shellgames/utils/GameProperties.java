package com.styx.shellgames.utils;

import com.styx.shellgames.generic.GameBoard;

import java.util.ResourceBundle;

public class GameProperties {
    private static ResourceBundle resourceBundle = null;

    public static void loadProperties(String name) {
        GameProperties.resourceBundle = ResourceBundle.getBundle("i18n_%s_%s".formatted(GameBoard.languageSelected, name));
    }

    public static String getProperty(String key) {
        return GameProperties.resourceBundle.getString(key);
    }
}
