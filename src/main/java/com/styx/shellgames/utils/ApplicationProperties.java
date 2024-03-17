package com.styx.shellgames.utils;

import java.util.ResourceBundle;

public class ApplicationProperties {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    public static String getProperty(String key) {
        return resourceBundle.getString(key);
    }
}
