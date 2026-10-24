package com.marlow.optimiser.mixin;

import com.marlow.optimiser.MoneyRewriter;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Keeps the scoreboard panel width matching the rewritten number. */
@Mixin(TextRenderer.class)
public class TextRendererMixin {

    @ModifyVariable(
            method = "getWidth(Lnet/minecraft/text/StringVisitable;)I",
            at = @At("HEAD"),
            argsOnly = true)
    private StringVisitable marlow$width(StringVisitable visitable) {
        if (visitable instanceof Text text) {
            return MoneyRewriter.rewrite(text);
        }
        return visitable;
    }
}
