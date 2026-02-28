package org.stepan1411.sw4ChatTranslator.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.stepan1411.sw4ChatTranslator.command.MyLanguageCommand;
import org.stepan1411.sw4ChatTranslator.config.TranslatorConfig;
import org.stepan1411.sw4ChatTranslator.i18n.Messages;

public class Sw4ChatTranslatorClient implements ClientModInitializer {
    private static boolean reminderShown = false;
    private static int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            MyLanguageCommand.register(dispatcher);
        });
        
        // Показываем напоминание через 5 секунд после входа в мир
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!reminderShown && client.player != null && client.world != null) {
                tickCounter++;
                
                // 20 тиков = 1 секунда, 100 тиков = 5 секунд
                if (tickCounter >= 100) {
                    if (!TranslatorConfig.isTranslationEnabled()) {
                        client.player.sendMessage(
                            Text.literal(Messages.get("language.please_set", "en")),
                            false
                        );
                    }
                    reminderShown = true;
                }
            }
            
            // Сброс при выходе из мира
            if (client.world == null) {
                reminderShown = false;
                tickCounter = 0;
            }
        });
    }
}
