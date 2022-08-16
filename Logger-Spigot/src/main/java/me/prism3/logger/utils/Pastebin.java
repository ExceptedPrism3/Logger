package me.prism3.logger.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class Pastebin {

    /**
     *
     * Special Thanks to Redmoogle#3761 | https://github.com/redmoogle for his help!
     */

    private Pastebin() {}

    private static final String API_URL = "https://pastebin.com/api/api_post.php";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String DEFAULT_INV = "\0";

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
            arguments.put("api_paste_private", request.getPasteState() + "");


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

        con.connect();
        try (OutputStream os = con.getOutputStream()) {
            os.write(postDataB);
        }

        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        final StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();
        con.disconnect();
        return response.toString();
    }

    private static String postMap(Map<String, String> arguments) throws UnsupportedEncodingException {

        final StringJoiner joiner = new StringJoiner("&");

        for (Map.Entry<String, String> entry : arguments.entrySet())
            joiner.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));

        return joiner.toString();
    }

    public static class PasteRequest {

        private final String devKey;
        private final String paste;
        private String pasteName = DEFAULT_INV;
        private String pasteFormat = DEFAULT_INV;
        private int pasteState = -1;
        private String pasteExpire = DEFAULT_INV;

        public PasteRequest(String devKey, String paste) {
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
            return DEFAULT_INV;
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

        public boolean hasUserKey() {
            return false;
        }

        public boolean hasPasteName() {
            return !Objects.equals(pasteName, DEFAULT_INV);
        }

        public boolean hasPasteFormat() {
            return !Objects.equals(pasteFormat, DEFAULT_INV);
        }

        public boolean hasPasteState() {
            return pasteState != -1;
        }

        public boolean hasPasteExpire() {
            return !Objects.equals(pasteExpire, DEFAULT_INV);
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

        public String postPaste() throws IOException { return Pastebin.postPaste(this); }
    }
}
