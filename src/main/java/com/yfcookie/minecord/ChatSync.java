package com.yfcookie.minecord;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ChatSync {

    private static final String discord_url = "http://localhost:5000/discord_bot";

    public static void sendMessage(String event, String playerName, String content) {
        try {
            URL obj = new URL(discord_url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            // Set the request method to POST
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Create the JSON payload
            String payload = "{ \"event\":\"" + event + "\",\"player\":\"" + playerName + "\",\"content\":\"" + content
                    + "\" }";

            // Set the request headers
            conn.setRequestProperty("Content-Type", "application/json");

            // Write the payload to the request body
            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            os.close();

            // Get the response from the server
            int responseCode = conn.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the response
            System.out.println("Response Code: " + responseCode);
            System.out.println("Response Body: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
