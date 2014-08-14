package ru.terra.ndo.server.constants;

import ru.terra.server.config.Config;

/**
 * Date: 24.06.14
 * Time: 17:50
 */
public class FilePatchConstants {
    private static Config config = Config.getConfig();

    public static String getMainFolder() {
        return config.getValue("main.dir", "web/html/");
    }

    public static String getResFolder() {
        return config.getValue("resources.dir", "res/");
    }

    public static String getPiczFolder() {
        return getResFolder() + "picz/";
    }
}

