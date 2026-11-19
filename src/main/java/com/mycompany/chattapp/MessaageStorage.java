package com.mycompany.chattapp;

import java.io.*;

public class MessaageStorage {

    private static final String FILE_NAME = "messages.json";

    public static void saveMessages() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write("[\n");
            for (int i = 0; i < Message.storedMessages.size(); i++) {
                Message m = Message.storedMessages.get(i);
                writer.write("  {\"messageID\":\"" + m.getMessageID() +
                        "\", \"messageHash\":\"" + m.getMessageHash() +
                        "\", \"recipient\":\"" + m.getRecipient() +
                        "\", \"messageText\":\"" + m.getMessageText().replace("\"", "'") + "\"}");
                if (i < Message.storedMessages.size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("]\n");
            System.out.println("Messages saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
        }
    }

    public static void loadMessages() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No stored messages file found yet.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Message.storedMessages.clear();
            Message.messageIDs.clear();
            Message.messageHashes.clear();
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"messageID\"")) {
                    String id = extract(line, "messageID");
                    String hash = extract(line, "messageHash");
                    String recipient = extract(line, "recipient");
                    String text = extract(line, "messageText");
                    Message m = new Message(recipient, text);
                    m.setMessageID(id);
                    m.setMessageHash(hash);
                    Message.storedMessages.add(m);
                    Message.messageIDs.add(id);
                    Message.messageHashes.add(hash);
                }
            }
            System.out.println("Messages loaded: " + Message.storedMessages.size());
        } catch (IOException e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }

    private static String extract(String line, String key) {
        String search = "\"" + key + "\":\"";
        int start = line.indexOf(search) + search.length();
        int end = line.indexOf("\"", start);
        if (start < 0 || end < 0) return "";
        return line.substring(start, end);
    }
}