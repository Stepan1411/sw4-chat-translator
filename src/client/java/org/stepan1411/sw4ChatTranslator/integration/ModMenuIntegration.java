package org.stepan1411.sw4ChatTranslator.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent);
    }
    
    private static class ConfigScreen extends Screen {
        private final Screen parent;
        
        protected ConfigScreen(Screen parent) {
            super(Text.literal("SW4 Chat Translator"));
            this.parent = parent;
        }
        
        @Override
        public void close() {
            if (this.client != null) {
                this.client.setScreen(parent);
            }
        }
        
        @Override
        public void render(net.minecraft.client.gui.DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            
            context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
            context.drawCenteredTextWithShadow(this.textRenderer, 
                Text.literal("Используйте команду:"), this.width / 2, 60, 0xAAAAAA);
            context.drawCenteredTextWithShadow(this.textRenderer, 
                Text.literal("/sw4-translator translate \"текст\" язык"), this.width / 2, 80, 0x00FF00);
            context.drawCenteredTextWithShadow(this.textRenderer, 
                Text.literal("Пример: /sw4-translator translate \"привет мир\" english"), this.width / 2, 100, 0xFFFF00);
            context.drawCenteredTextWithShadow(this.textRenderer, 
                Text.literal("Поддерживаемые языки: english, french, german, russian, spanish"), this.width / 2, 120, 0xAAAAAA);
        }
    }
}
