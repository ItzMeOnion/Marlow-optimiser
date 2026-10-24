package com.marlow.optimiser;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

/** The menu: ON / OFF buttons and a box for the amount you want to see. */
public class OptimiserScreen extends Screen {

    private ButtonWidget onButton;
    private ButtonWidget offButton;
    private TextFieldWidget amountField;

    public OptimiserScreen() {
        super(Text.literal("Marlow's Crystal Optimiser"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int top = this.height / 2 - 62;

        onButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("ON"), b -> {
            MarlowsCrystalOptimiser.enabled = true;
            MarlowsCrystalOptimiser.save();
            refresh();
        }).dimensions(cx - 102, top + 30, 100, 20).build());

        offButton = this.addDrawableChild(ButtonWidget.builder(Text.literal("OFF"), b -> {
            MarlowsCrystalOptimiser.enabled = false;
            MarlowsCrystalOptimiser.save();
            refresh();
        }).dimensions(cx + 2, top + 30, 100, 20).build());

        amountField = new TextFieldWidget(this.textRenderer, cx - 102, top + 72, 150, 20, Text.literal("Amount"));
        amountField.setMaxLength(24);
        amountField.setPlaceholder(Text.literal("e.g. 5B or 5000000000").formatted(Formatting.DARK_GRAY));
        amountField.setText(MarlowsCrystalOptimiser.fakeAmount);
        this.addDrawableChild(amountField);
        this.setInitialFocus(amountField);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Apply"), b -> apply())
                .dimensions(cx + 52, top + 72, 50, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> this.close())
                .dimensions(cx - 50, top + 104, 100, 20).build());

        refresh();
    }

    private void apply() {
        String t = amountField.getText().trim();
        if (!t.isEmpty()) {
            MarlowsCrystalOptimiser.fakeAmount = t;
        }
        MarlowsCrystalOptimiser.save();
        refresh();
    }

    private void refresh() {
        boolean on = MarlowsCrystalOptimiser.enabled;
        onButton.setMessage(Text.literal("ON").formatted(on ? Formatting.GREEN : Formatting.GRAY));
        offButton.setMessage(Text.literal("OFF").formatted(on ? Formatting.GRAY : Formatting.RED));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            apply();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        apply();
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int cx = this.width / 2;
        int top = this.height / 2 - 62;
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, cx, top, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer,
                Text.literal("Scoreboard money - your screen only").formatted(Formatting.GRAY), cx, top + 14, 0xAAAAAA);
        context.drawTextWithShadow(this.textRenderer,
                Text.literal("Amount to show").formatted(Formatting.GRAY), cx - 102, top + 60, 0xAAAAAA);
    }
}
