package com.marlow.optimiser.mixin;

import com.marlow.optimiser.MoneyRewriter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Rewrites the text right before it is drawn (your screen only). */
@Mixin(DrawContext.class)
public class DrawContextMixin {

    @ModifyVariable(
            method = "drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V",
            at = @At("HEAD"),
            argsOnly = true)
    private Text marlow$rewrite(Text text) {
        return MoneyRewriter.rewrite(text);
    }
}
