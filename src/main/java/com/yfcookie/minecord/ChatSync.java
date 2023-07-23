package com.yfcookie.minecord;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class ChatSync {
    private HttpServer httpServer;
    private static final String discord_url = "http://localhost:5000/discord_bot";


    public ChatSync() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(5001), 0);
            httpServer.createContext("/minecraft_server", new DiscordMessageHandler());
            httpServer.setExecutor(null);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class DiscordMessageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("POST")) {
                String messageContent = readRequestBody(exchange.getRequestBody());
                // Process the message content as needed
                broadcastMessageInGame(messageContent);
            }

            String response = "OK";
            exchange.sendResponseHeaders(200, response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    private String readRequestBody(InputStream inputStream) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }

    private void broadcastMessageInGame(String message) {
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(message);
            String sender = (String) jsonObject.get("sender");
            String content = (String) jsonObject.get("content");

            if (sender != null && message != null) {
                String formattedMessage = ChatColor.GREEN + "[Discord] " + ChatColor.DARK_PURPLE + sender + ": " + ChatColor.RESET + message;
                Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "[Discord] "+ ChatColor.RESET + sender + ": " + content);
            }
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
    }

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
