package org.stepan1411.sw4ChatTranslator.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.stepan1411.sw4ChatTranslator.config.ServerConfig;
import org.stepan1411.sw4ChatTranslator.config.TranslatorConfig;
import org.stepan1411.sw4ChatTranslator.translator.TranslationService;

@Mixin(ChatHud.class)
public class ChatHudMixin {
    
    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", 
            at = @At("HEAD"))
    private void onAddMessage(Text message, MessageSignatureData signature, MessageIndicator indicator, CallbackInfo ci) {
        if (!TranslatorConfig.isTranslationEnabled()) {
            return;
        }
        
        String originalText = message.getString();
        
        // Пропускаем системные сообщения, команды и короткие сообщения
        if (originalText.startsWith("[") || originalText.startsWith("§") || originalText.length() < 3) {
            return;
        }
        
        // Извлекаем имя игрока и сообщение
        String playerName = null;
        String chatMessage = originalText;
        
        if (originalText.contains("<") && originalText.contains(">")) {
            int start = originalText.indexOf("<");
            int end = originalText.indexOf(">");
            if (end > start) {
                playerName = originalText.substring(start + 1, end);
                chatMessage = originalText.substring(end + 1).trim();
            }
        }
        
        if (chatMessage.isEmpty() || chatMessage.length() < 2) {
            return;
        }
        
        final String finalPlayerName = playerName;
        final String finalChatMessage = chatMessage;
        final Text originalMessage = message;
        
        // Переводим асинхронно
        TranslationService.translate(finalChatMessage, TranslatorConfig.getMyLanguage()).thenAccept(translatedText -> {
            // Проверяем, отличается ли перевод от оригинала
            if (!translatedText.equals(finalChatMessage) && !translatedText.startsWith("Ошибка")) {
                MinecraftClient.getInstance().execute(() -> {
                    ChatHud chatHud = (ChatHud) (Object) this;
                    ServerConfig config = ServerConfig.getInstance();
                    
                    // Создаем переведенное сообщение, копируя стиль оригинала
                    Text translatedMessage = createTranslatedMessage(originalMessage, finalPlayerName, translatedText, config);
                    
                    chatHud.addMessage(translatedMessage, null, indicator);
                });
            }
        });
    }
    
    private Text createTranslatedMessage(Text originalMessage, String playerName, String translatedText, ServerConfig config) {
        // Получаем стиль оригинального сообщения
        if (playerName != null) {
            // Создаем сообщение с тем же форматом
            Text result = Text.empty();
            
            // Добавляем тег языка если включено
            if (config.isShowLanguageTag()) {
                result = Text.literal("§7[" + TranslatorConfig.getMyLanguage() + "] §r");
            }
            
            // Добавляем имя игрока с цветом
            result = result.copy().append(Text.literal("<"));
            
            if (config.isShowColoredNames()) {
                result = result.copy().append(Text.literal(playerName).styled(style -> style.withColor(0x55FFFF)));
            } else {
                result = result.copy().append(Text.literal(playerName));
            }
            
            result = result.copy().append(Text.literal("> "));
            
            // Добавляем переведенный текст, копируя стиль оригинального сообщения
            result = result.copy().append(Text.literal(translatedText).setStyle(originalMessage.getStyle()));
            
            return result;
        } else {
            // Для сообщений без имени игрока просто копируем стиль
            return Text.literal(translatedText).setStyle(originalMessage.getStyle());
        }
    }
}
