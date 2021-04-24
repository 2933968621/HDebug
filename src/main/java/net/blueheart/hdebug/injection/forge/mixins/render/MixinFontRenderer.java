package net.blueheart.hdebug.injection.forge.mixins.render;

import net.blueheart.hdebug.HDebug;
import net.blueheart.hdebug.event.TextEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
@SideOnly(Side.CLIENT)
public class MixinFontRenderer {

    @ModifyVariable(method = "renderString", at = @At("HEAD"), ordinal = 0)
    private String renderString(final String string) {
        if (string == null || HDebug.eventManager == null)
            return string;

        final TextEvent textEvent = new TextEvent(string);
        HDebug.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), ordinal = 0)
    private String getStringWidth(final String string) {
        if (string == null || HDebug.eventManager == null)
            return string;

        final TextEvent textEvent = new TextEvent(string);
        HDebug.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }
}