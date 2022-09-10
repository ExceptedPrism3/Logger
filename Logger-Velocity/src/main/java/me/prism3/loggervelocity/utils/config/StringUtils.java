package me.prism3.loggervelocity.utils.config;

public class StringUtils {

    private StringUtils() {}

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {

        final char[] b = textToTranslate.toCharArray();

        for (int i = 0; i < b.length - 1; ++i) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        } return new String(b);
    }
}
