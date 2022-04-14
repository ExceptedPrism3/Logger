package me.prism3.logger.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringJoiner;

public class Dump implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        try {

            main();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    private static final String API_URL = "http://pastebin.com/api/api_post.php";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String DEFAULT_INV = "\0";

    public static String postPaste(String devKey, String paste) throws IOException {
        Map<String, String> arguements = new HashMap<>();
        arguements.put("api_option", "paste");
        arguements.put("api_dev_key", devKey);
        arguements.put("api_paste_code", paste);
        String postData = postMap(arguements);
        byte[] postDataB = postData.getBytes("UTF-8");
        HttpURLConnection con = (HttpURLConnection) new URL(API_URL).openConnection();
        con.setDoOutput(true);
        con.setFixedLengthStreamingMode(postDataB.length);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.connect();
        try (OutputStream os = con.getOutputStream()) {
            os.write(postDataB);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        con.disconnect();
        return response.toString();
    }
    public static String postPaste(PasteRequest request) throws IOException {
        Map<String, String> arguements = new HashMap<>();
        arguements.put("api_option", "paste");
        arguements.put("api_dev_key", request.getDevKey());
        arguements.put("api_paste_code", request.getPaste());
        if(request.hasUserKey())
        {
            arguements.put("api_user_key", request.getUserKey());
        }
        if(request.hasPasteName())
        {
            arguements.put("api_paste_name", request.getPasteName());
        }
        if(request.hasPasteFormat())
        {
            arguements.put("api_paste_format", request.getPasteFormat());
        }
        if(request.hasPasteState())
        {
            arguements.put("api_paste_private", request.getPasteState() + "");
        }
        if(request.hasPasteExpire())
        {
            arguements.put("api_paste_expire_date", request.getPasteExpire());
        }
        String postData = postMap(arguements);
        byte[] postDataB = postData.getBytes("UTF-8");
        HttpURLConnection con = (HttpURLConnection) new URL(API_URL).openConnection();
        con.setDoOutput(true);
        con.setFixedLengthStreamingMode(postDataB.length);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.connect();
        try (OutputStream os = con.getOutputStream()) {
            os.write(postDataB);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        con.disconnect();
        return response.toString();
    }
    private static String postMap(Map<String, String> arguements) throws UnsupportedEncodingException {
        StringJoiner joiner = new StringJoiner("&");
        for (Entry<String, String> entry : arguements.entrySet()) {
            joiner.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return joiner.toString();
    }

    public static class PasteRequest {
        private String devKey;
        private String paste;
        private String userKey = DEFAULT_INV;
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

        //
        public boolean hasUserKey() {
            return userKey != DEFAULT_INV;
        }

        public boolean hasPasteName() {
            return pasteName != DEFAULT_INV;
        }

        public boolean hasPasteFormat() {
            return pasteFormat != DEFAULT_INV;
        }

        public boolean hasPasteState() {
            return pasteState != -1;
        }

        public boolean hasPasteExpire() {
            return pasteExpire != DEFAULT_INV;
        }


        public void setUserKey(String userKey) {
            this.userKey = userKey;
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

        public String postPaste() throws IOException
        {
            return postPaste();
        }

    }

    public static void main() throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL("http://pastebin.com/raw/AhRgs2g1").openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringJoiner response = new StringJoiner("\n");
        while ((inputLine = in.readLine()) != null) {
            response.add(inputLine);
        }
        in.close();
        con.disconnect();
        //Getting the code from the site ^^
        PasteRequest request = new PasteRequest("r_Cgj_xsPsCUrQi13xyxfGo6uiBDIjB9\n", response.toString());
        request.setPasteName("Paste Java Wrap (Ligh AF)");//To set title
        request.setPasteFormat("java");//To make it a java format
        request.setPasteState(1);//To make unlisted
        request.setPasteExpire("1H");//Make it live 1 hour
        System.out.println(request.postPaste());//Prints the paste url
    }

}