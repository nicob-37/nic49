package com.nic49.bot.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class TransactionManager {
    private static final String STORAGE_DIR = "/home/ubuntu/nic49/storage/";
    private static final String DB_FILE = STORAGE_DIR + "transactions.json";

    // Maps Item Name -> Buy Cost
    private Map<String, Integer> activeInvestments = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public TransactionManager() {
        try {
            Files.createDirectories(Paths.get(STORAGE_DIR));
            File file = new File(DB_FILE);
            if (file.exists()) {
                try (Reader reader = new FileReader(file)) {
                    var type = new TypeToken<HashMap<String, Integer>>(){}.getType();
                    activeInvestments = gson.fromJson(reader, type);
                    if (activeInvestments == null) activeInvestments = new HashMap<>();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void buyItem(String item, int cost) {
        // Keeps names lowercase and clean to prevent matching bugs
        activeInvestments.put(item.toLowerCase().trim(), cost);
        saveDatabase();
    }

    public Integer getBuyCost(String item) {
        return activeInvestments.get(item.toLowerCase().trim());
    }

    public synchronized void sellItem(String item) {
        activeInvestments.remove(item.toLowerCase().trim());
        saveDatabase();
    }

    public Map<String, Integer> getActiveInvestments() {
        return activeInvestments;
    }

    private void saveDatabase() {
        try (Writer writer = new FileWriter(DB_FILE)) {
            gson.toJson(activeInvestments, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}