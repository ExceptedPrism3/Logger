package me.prism3.logger_core.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DumpHelper {

    public static final String DEFAULT_PASTEBIN_API = "r_Cgj_xsPsCUrQi13xyxfGo6uiBDIjB9";
    public static final String PASTE_EXPIRATION = "10M";
    private static final int MAX_FILE_BYTES = 256 * 1024; // 256 KB max per file

    /**
     * Resolves the Pastebin API key across:
     * 1. System environment variable (PASTEBIN_API)
     * 2. Plugin dataFolder/.env
     * 3. Server root .env
     * 4. Bundled resource stream (.env inside JAR)
     * 5. DEFAULT_PASTEBIN_API fallback
     */
    public static String resolveApiKey(File dataFolder, InputStream bundledEnvStream) {
        // 1. System environment variable
        String envKey = System.getenv("PASTEBIN_API");
        if (envKey != null && !envKey.trim().isEmpty()) {
            return envKey.trim();
        }

        // 2. Plugin data folder .env (auto-extract from bundled stream if missing)
        if (dataFolder != null) {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }
            File pluginEnv = new File(dataFolder, ".env");
            if (!pluginEnv.exists() && bundledEnvStream != null) {
                try {
                    byte[] bytes = readStreamBytes(bundledEnvStream);
                    java.nio.file.Files.write(pluginEnv.toPath(), bytes);
                } catch (Exception ignored) {}
            }
            if (pluginEnv.exists()) {
                String key = readKeyFromEnvFile(pluginEnv, "PASTEBIN_API");
                if (key != null && !key.trim().isEmpty()) {
                    return key.trim();
                }
            }
        }

        // 3. Server root .env
        File rootEnv = new File(".env");
        if (rootEnv.exists()) {
            String key = readKeyFromEnvFile(rootEnv, "PASTEBIN_API");
            if (key != null && !key.trim().isEmpty()) {
                return key.trim();
            }
        }

        // 4. Bundled resource stream (if not consumed)
        if (bundledEnvStream != null) {
            try (InputStream in = bundledEnvStream) {
                Properties props = new Properties();
                props.load(in);
                String resKey = props.getProperty("PASTEBIN_API");
                if (resKey != null && !resKey.trim().isEmpty()) {
                    return resKey.trim();
                }
            } catch (Exception ignored) {}
        }

        // 5. Default fallback
        return DEFAULT_PASTEBIN_API;
    }

    private static byte[] readStreamBytes(InputStream in) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = in.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    private static String readKeyFromEnvFile(File file, String targetKey) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int eq = line.indexOf('=');
                if (eq > 0) {
                    String k = line.substring(0, eq).trim();
                    if (k.equalsIgnoreCase(targetKey)) {
                        String v = line.substring(eq + 1).trim();
                        if ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'"))) {
                            v = v.substring(1, v.length() - 1);
                        }
                        return v;
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    /**
     * Reads a file safely up to MAX_FILE_BYTES. If larger, reads the tail.
     */
    public static String readFile(File file) {
        if (file == null || !file.exists()) {
            return "# File not found: " + (file != null ? file.getPath() : "null");
        }
        try {
            long length = file.length();
            if (length <= MAX_FILE_BYTES) {
                return new String(java.nio.file.Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            } else {
                try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                    long start = length - MAX_FILE_BYTES;
                    raf.seek(start);
                    byte[] bytes = new byte[MAX_FILE_BYTES];
                    raf.readFully(bytes);
                    return "# [TRUNCATED - Showing last " + (MAX_FILE_BYTES / 1024) + " KB of " + (length / 1024) + " KB]\n"
                            + new String(bytes, StandardCharsets.UTF_8);
                }
            }
        } catch (Exception e) {
            return "# Error reading file " + file.getPath() + ": " + e.getMessage();
        }
    }

    /**
     * Builds and posts a dump to Pastebin.
     */
    public static String postDump(String apiKey, String dumpTitle, Map<String, File> filesToDump) throws IOException {
        StringBuilder combined = new StringBuilder();
        combined.append("# ========================================================\n");
        combined.append("# ").append(dumpTitle).append("\n");
        combined.append("# Generated at: ").append(new java.util.Date()).append("\n");
        combined.append("# ========================================================\n\n");

        for (Map.Entry<String, File> entry : filesToDump.entrySet()) {
            combined.append("# --------------------------------------------------------\n");
            combined.append("# FILE: ").append(entry.getKey()).append("\n");
            combined.append("# --------------------------------------------------------\n");
            File file = entry.getValue();
            if (file != null && file.exists()) {
                combined.append(readFile(file)).append("\n\n");
            } else {
                combined.append("# [File not found / not configured]\n\n");
            }
        }

        PasteBin.PasteRequest request = new PasteBin.PasteRequest(apiKey, combined.toString());
        request.setPasteName(dumpTitle);
        request.setPasteFormat("yaml");
        request.setPasteState(1); // Unlisted
        request.setPasteExpire(PASTE_EXPIRATION);

        return request.postPaste();
    }
}
