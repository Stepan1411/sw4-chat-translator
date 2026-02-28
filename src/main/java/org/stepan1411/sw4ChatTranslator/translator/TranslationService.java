package org.stepan1411.sw4ChatTranslator.translator;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.minecraft.text.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class TranslationService {
    private static final String TRANSLATE_API = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=%s&dt=t&q=%s";

    public static CompletableFuture<String> translate(String text, String targetLang) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);
                String urlStr = String.format(TRANSLATE_API, targetLang, encodedText);
                
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                
                JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
                JsonArray translations = jsonArray.get(0).getAsJsonArray();
                
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < translations.size(); i++) {
                    result.append(translations.get(i).getAsJsonArray().get(0).getAsString());
                }
                
                return result.toString();
            } catch (Exception e) {
                return "Ошибка перевода: " + e.getMessage();
            }
        });
    }
    
    public static String getLanguageCode(String language) {
        return switch (language.toLowerCase()) {
            case "english", "английский" -> "en";
            case "russian", "русский" -> "ru";
            case "spanish", "испанский" -> "es";
            case "french", "французский" -> "fr";
            case "german", "немецкий" -> "de";
            case "chinese", "китайский" -> "zh-CN";
            case "japanese", "японский" -> "ja";
            default -> language.toLowerCase();
        };
    }
}
