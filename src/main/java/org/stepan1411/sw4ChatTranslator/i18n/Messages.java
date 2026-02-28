package org.stepan1411.sw4ChatTranslator.i18n;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private static final Map<String, Map<String, String>> translations = new HashMap<>();
    
    static {
        // English
        Map<String, String> en = new HashMap<>();
        en.put("language.set", "§aYour language has been set to: §e%s §7(%s)");
        en.put("language.auto_translate", "§7All chat messages will now be automatically translated to your language");
        en.put("language.disabled", "§cAutomatic translation disabled");
        en.put("language.current", "§aCurrent language: §e%s");
        en.put("language.disable_hint", "§7To disable translation: §e/sw4-translator mylanguage off");
        en.put("language.not_set", "§cTranslation not configured. Use: §e/sw4-translator mylanguage <language>");
        en.put("language.available", "§7Available languages: english, french, german, russian, spanish");
        en.put("language.please_set", "§e[SW4 Translator] §7Please set your language: §e/sw4-translator mylanguage <language>");
        en.put("translate.error", "Translation error: %s");
        translations.put("en", en);
        
        // Russian
        Map<String, String> ru = new HashMap<>();
        ru.put("language.set", "§aВаш язык установлен: §e%s §7(%s)");
        ru.put("language.auto_translate", "§7Теперь все сообщения в чате будут автоматически переводиться на ваш язык");
        ru.put("language.disabled", "§cАвтоматический перевод отключен");
        ru.put("language.current", "§aТекущий язык: §e%s");
        ru.put("language.disable_hint", "§7Чтобы отключить перевод: §e/sw4-translator mylanguage off");
        ru.put("language.not_set", "§cПеревод не настроен. Используйте: §e/sw4-translator mylanguage <язык>");
        ru.put("language.available", "§7Доступные языки: english, french, german, russian, spanish");
        ru.put("language.please_set", "§e[SW4 Translator] §7Пожалуйста, установите свой язык: §e/sw4-translator mylanguage <язык>");
        ru.put("translate.error", "Ошибка перевода: %s");
        translations.put("ru", ru);
        
        // Spanish
        Map<String, String> es = new HashMap<>();
        es.put("language.set", "§aTu idioma se ha establecido en: §e%s §7(%s)");
        es.put("language.auto_translate", "§7Todos los mensajes del chat ahora se traducirán automáticamente a tu idioma");
        es.put("language.disabled", "§cTraducción automática desactivada");
        es.put("language.current", "§aIdioma actual: §e%s");
        es.put("language.disable_hint", "§7Para desactivar la traducción: §e/sw4-translator mylanguage off");
        es.put("language.not_set", "§cTraducción no configurada. Usa: §e/sw4-translator mylanguage <idioma>");
        es.put("language.available", "§7Idiomas disponibles: english, french, german, russian, spanish");
        es.put("language.please_set", "§e[SW4 Translator] §7Por favor, establece tu idioma: §e/sw4-translator mylanguage <idioma>");
        es.put("translate.error", "Error de traducción: %s");
        translations.put("es", es);
        
        // French
        Map<String, String> fr = new HashMap<>();
        fr.put("language.set", "§aVotre langue a été définie sur: §e%s §7(%s)");
        fr.put("language.auto_translate", "§7Tous les messages du chat seront désormais automatiquement traduits dans votre langue");
        fr.put("language.disabled", "§cTraduction automatique désactivée");
        fr.put("language.current", "§aLangue actuelle: §e%s");
        fr.put("language.disable_hint", "§7Pour désactiver la traduction: §e/sw4-translator mylanguage off");
        fr.put("language.not_set", "§cTraduction non configurée. Utilisez: §e/sw4-translator mylanguage <langue>");
        fr.put("language.available", "§7Langues disponibles: english, french, german, russian, spanish");
        fr.put("language.please_set", "§e[SW4 Translator] §7Veuillez définir votre langue: §e/sw4-translator mylanguage <langue>");
        fr.put("translate.error", "Erreur de traduction: %s");
        translations.put("fr", fr);
        
        // German
        Map<String, String> de = new HashMap<>();
        de.put("language.set", "§aDeine Sprache wurde festgelegt auf: §e%s §7(%s)");
        de.put("language.auto_translate", "§7Alle Chat-Nachrichten werden jetzt automatisch in deine Sprache übersetzt");
        de.put("language.disabled", "§cAutomatische Übersetzung deaktiviert");
        de.put("language.current", "§aAktuelle Sprache: §e%s");
        de.put("language.disable_hint", "§7Um die Übersetzung zu deaktivieren: §e/sw4-translator mylanguage off");
        de.put("language.not_set", "§cÜbersetzung nicht konfiguriert. Verwende: §e/sw4-translator mylanguage <sprache>");
        de.put("language.available", "§7Verfügbare Sprachen: english, french, german, russian, spanish");
        de.put("language.please_set", "§e[SW4 Translator] §7Bitte lege deine Sprache fest: §e/sw4-translator mylanguage <sprache>");
        de.put("translate.error", "Übersetzungsfehler: %s");
        translations.put("de", de);
    }
    
    public static String get(String key, String language, Object... args) {
        Map<String, String> langMap = translations.get(language);
        if (langMap == null) {
            langMap = translations.get("en");
        }
        
        String message = langMap.get(key);
        if (message == null) {
            message = translations.get("en").get(key);
        }
        
        if (message == null) {
            return key;
        }
        
        if (args.length > 0) {
            return String.format(message, args);
        }
        
        return message;
    }
}
