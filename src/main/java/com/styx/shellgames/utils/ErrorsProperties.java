package com.styx.shellgames.utils;

import java.util.ResourceBundle;

public class ErrorsProperties {
    private static ResourceBundle resourceBundle = null;

    public static void loadProperties(String lang) {
        ErrorsProperties.resourceBundle = ResourceBundle.getBundle("i18n_%s_errors".formatted(lang));
    }

    public static String getProperty(String key) {
        return "\u001B[31m" + ErrorsProperties.resourceBundle.getString(key) + "\u001B[0m";
    }
}
