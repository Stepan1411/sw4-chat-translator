package org.stepan1411.sw4ChatTranslator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TranslatorConfig {
    private static String myLanguage = null;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    private static class PlayerData {
        Map<String, String> players = new HashMap<>();
    }
    
    public static void setMyLanguage(String language) {
        myLanguage = language;
        savePlayerLanguage(language);
    }
    
    public static String getMyLanguage() {
        if (myLanguage == null) {
            myLanguage = loadPlayerLanguage();
        }
        return myLanguage;
    }
    
    public static boolean isTranslationEnabled() {
        return getMyLanguage() != null && !getMyLanguage().isEmpty();
    }
    
    public static void clearMyLanguage() {
        myLanguage = null;
        savePlayerLanguage(null);
    }
    
    private static String getPlayerUUID() {
        try {
            net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
            if (client.player != null) {
                return client.player.getUuidAsString();
            }
        } catch (Exception e) {
            // Ignore
        }
        return "default";
    }
    
    private static void savePlayerLanguage(String language) {
        File configDir = new File("config/sw4-translator");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        
        File configFile = new File(configDir, "player-data.json");
        PlayerData data = new PlayerData();
        
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                PlayerData loaded = GSON.fromJson(reader, PlayerData.class);
                if (loaded != null && loaded.players != null) {
                    data.players = loaded.players;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        String uuid = getPlayerUUID();
        if (language == null) {
            data.players.remove(uuid);
        } else {
            data.players.put(uuid, language);
        }
        
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String loadPlayerLanguage() {
        File configFile = new File("config/sw4-translator/player-data.json");
        
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                PlayerData data = GSON.fromJson(reader, PlayerData.class);
                if (data != null && data.players != null) {
                    String uuid = getPlayerUUID();
                    return data.players.get(uuid);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
}
