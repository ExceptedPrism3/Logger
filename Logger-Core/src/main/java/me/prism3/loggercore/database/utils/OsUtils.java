package me.prism3.loggercore.database.utils;

public class OsUtils {

    private OsUtils() {}

    private static boolean isWindows = false;
    private static boolean isLinux = false;
    private static boolean isMacos = false;
    private static boolean isKnownOS = false;

    static {

        final String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            isWindows = true;
            isKnownOS = true;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            isLinux = true;
            isKnownOS = true;
        } else if (os.contains("mac")) {
            isMacos = true;
            isKnownOS = true;
        }
    }

    public static boolean isWindows() {
        return isWindows;
    }
    public static boolean isMacos() {
        return isMacos;
    }
    public static boolean isLinux() {
        return isLinux;
    }
    public static boolean isKnownOS() { return isKnownOS; }
}


