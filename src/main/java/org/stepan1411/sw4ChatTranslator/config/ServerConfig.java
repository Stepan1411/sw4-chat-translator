package org.stepan1411.sw4ChatTranslator.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ServerConfig {
    private boolean showColoredNames = true;
    private boolean showLanguageTag = false;
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static ServerConfig instance;
    
    public static ServerConfig getInstance() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }
    
    public static ServerConfig load() {
        File configDir = new File("config/sw4-translator");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        
        File configFile = new File(configDir, "config-server.json");
        
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                return GSON.fromJson(reader, ServerConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        ServerConfig config = new ServerConfig();
        config.save();
        return config;
    }
    
    public void save() {
        File configDir = new File("config/sw4-translator");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        
        File configFile = new File(configDir, "config-server.json");
        
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isShowColoredNames() {
        return showColoredNames;
    }
    
    public void setShowColoredNames(boolean showColoredNames) {
        this.showColoredNames = showColoredNames;
        save();
    }
    
    public boolean isShowLanguageTag() {
        return showLanguageTag;
    }
    
    public void setShowLanguageTag(boolean showLanguageTag) {
        this.showLanguageTag = showLanguageTag;
        save();
    }
}
