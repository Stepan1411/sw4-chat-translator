package org.stepan1411.sw4ChatTranslator.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import org.stepan1411.sw4ChatTranslator.config.TranslatorConfig;
import org.stepan1411.sw4ChatTranslator.i18n.Messages;

import java.util.concurrent.CompletableFuture;

public class MyLanguageCommand {
    
    private static final String[] LANGUAGES = {
        "english", "french", "german", "russian", "spanish", "off"
    };
    
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("sw4-translator")
            .then(ClientCommandManager.literal("mylanguage")
                .executes(MyLanguageCommand::showCurrentLanguage)
                .then(ClientCommandManager.argument("language", StringArgumentType.word())
                    .suggests(MyLanguageCommand::suggestLanguages)
                    .executes(MyLanguageCommand::setLanguage))));
    }
    
    private static CompletableFuture<Suggestions> suggestLanguages(
            CommandContext<FabricClientCommandSource> context, 
            SuggestionsBuilder builder) {
        
        String remaining = builder.getRemaining().toLowerCase();
        for (String lang : LANGUAGES) {
            if (lang.toLowerCase().startsWith(remaining)) {
                builder.suggest(lang);
            }
        }
        return builder.buildFuture();
    }
    
    private static int setLanguage(CommandContext<FabricClientCommandSource> context) {
        String language = StringArgumentType.getString(context, "language");
        
        if (language.equalsIgnoreCase("off")) {
            String currentLang = TranslatorConfig.getMyLanguage();
            if (currentLang == null) currentLang = "en";
            
            TranslatorConfig.clearMyLanguage();
            context.getSource().sendFeedback(
                Text.literal(Messages.get("language.disabled", currentLang))
            );
            return 1;
        }
        
        String langCode = org.stepan1411.sw4ChatTranslator.translator.TranslationService.getLanguageCode(language);
        
        TranslatorConfig.setMyLanguage(langCode);
        
        context.getSource().sendFeedback(
            Text.literal(Messages.get("language.set", langCode, language, langCode))
        );
        context.getSource().sendFeedback(
            Text.literal(Messages.get("language.auto_translate", langCode))
        );
        
        return 1;
    }
    
    private static int showCurrentLanguage(CommandContext<FabricClientCommandSource> context) {
        if (TranslatorConfig.isTranslationEnabled()) {
            String lang = TranslatorConfig.getMyLanguage();
            context.getSource().sendFeedback(
                Text.literal(Messages.get("language.current", lang, lang))
            );
            context.getSource().sendFeedback(
                Text.literal(Messages.get("language.disable_hint", lang))
            );
        } else {
            context.getSource().sendFeedback(
                Text.literal(Messages.get("language.not_set", "en"))
            );
            context.getSource().sendFeedback(
                Text.literal(Messages.get("language.available", "en"))
            );
        }
        return 1;
    }
}
