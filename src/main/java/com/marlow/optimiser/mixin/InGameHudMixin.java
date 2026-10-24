package com.marlow.optimiser.mixin;

import com.marlow.optimiser.MarlowsCrystalOptimiser;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.ScoreboardObjective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Marks the window of time in which the sidebar scoreboard is being drawn. */
@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At("HEAD"))
    private void marlow$start(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        MarlowsCrystalOptimiser.inSidebar = true;
    }

    @Inject(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At("RETURN"))
    private void marlow$end(DrawContext context, ScoreboardObjective objective, CallbackInfo ci) {
        MarlowsCrystalOptimiser.inSidebar = false;
    }
}
