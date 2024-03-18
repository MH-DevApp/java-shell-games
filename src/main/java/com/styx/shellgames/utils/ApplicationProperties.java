package com.styx.shellgames.utils;

import java.util.ResourceBundle;

public class ApplicationProperties {
    private static ResourceBundle resourceBundle = null;

    public static void loadProperties() {
        ApplicationProperties.resourceBundle = ResourceBundle.getBundle("application");
    }

    public static String getProperty(String key) {
        return ApplicationProperties.resourceBundle.getString(key);
    }
}
