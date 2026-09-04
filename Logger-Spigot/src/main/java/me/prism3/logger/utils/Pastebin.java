package me.prism3.logger.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class PasteBin {

    private static final String API_URL = "https://pastebin.com/api/api_post.php";
    private static final String USER_AGENT = "Mozilla/5.0";

    public static String postPaste(PasteRequest request) throws IOException {

        final Map<String, String> arguments = new HashMap<>();
        arguments.put("api_option", "paste");
        arguments.put("api_dev_key", request.getDevKey());
        arguments.put("api_paste_code", request.getPaste());

        if (request.hasUserKey())
            arguments.put("api_user_key", request.getUserKey());
        if (request.hasPasteName())
            arguments.put("api_paste_name", request.getPasteName());
        if (request.hasPasteFormat())
            arguments.put("api_paste_format", request.getPasteFormat());
        if (request.hasPasteState())
            arguments.put("api_paste_private", String.valueOf(request.getPasteState()));
        if (request.hasPasteExpire())
            arguments.put("api_paste_expire_date", request.getPasteExpire());

        final String postData = postMap(arguments);
        final byte[] postDataB = postData.getBytes(StandardCharsets.UTF_8);

        final HttpURLConnection con = (HttpURLConnection) new URL(API_URL).openConnection();
        con.setDoOutput(true);
        con.setFixedLengthStreamingMode(postDataB.length);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        try (final OutputStream os = con.getOutputStream()) {
            os.write(postDataB);
        }

        return readResponse(con);
    }

    private static String postMap(Map<String, String> arguments) {

        final StringJoiner joiner = new StringJoiner("&");

        try {
            for (Map.Entry<String, String> entry : arguments.entrySet()) {
                joiner.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()) + "="
                        + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
            }
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return joiner.toString();
    }

    private static String readResponse(final HttpURLConnection con) throws IOException {

        final StringBuilder response = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (final IOException e) {
            // Read the error stream to capture server's response when there’s an error
            try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        }

        con.disconnect();
        return response.toString();
    }

    public static class PasteRequest {

        private final String devKey;
        private final String paste;
        private final String userKey = null;
        private String pasteName = null;
        private String pasteFormat = null;
        private int pasteState = -1;
        private String pasteExpire = null;

        public PasteRequest(final String devKey, final String paste) {
            this.devKey = devKey;
            this.paste = paste;
        }

        public String getDevKey() {
            return devKey;
        }

        public String getPaste() {
            return paste;
        }

        public String getUserKey() {
            return userKey;
        }

        public String getPasteName() {
            return pasteName;
        }

        public String getPasteFormat() {
            return pasteFormat;
        }

        public int getPasteState() {
            return pasteState;
        }

        public String getPasteExpire() {
            return pasteExpire;
        }

        // Check if values are set (i.e., not null or default)
        public boolean hasUserKey() {
            return userKey != null;
        }

        public boolean hasPasteName() {
            return pasteName != null;
        }

        public boolean hasPasteFormat() {
            return pasteFormat != null;
        }

        public boolean hasPasteState() {
            return pasteState != -1;
        }

        public boolean hasPasteExpire() {
            return pasteExpire != null;
        }

        public void setPasteName(String pasteName) {
            this.pasteName = pasteName;
        }

        public void setPasteFormat(String pasteFormat) {
            this.pasteFormat = pasteFormat;
        }

        public void setPasteState(int pasteState) {
            this.pasteState = pasteState;
        }

        public void setPasteExpire(String pasteExpire) {
            this.pasteExpire = pasteExpire;
        }

        public String postPaste() throws IOException {
            return PasteBin.postPaste(this);
        }
    }
}
