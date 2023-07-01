package daniel.keybinds_plus.client.mixins;

import daniel.keybinds_plus.client.KeybindsPlus;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseScrollMixin {

    private static final int HOTBAR_SIZE = 9;

    @Inject(method = "onMouseScroll(JDD)V", at = @At("HEAD"))
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo info) {
        if (MinecraftClient.getInstance().currentScreen != null) {
            // Only apply scrolling key binds while there is no screen visible.
            // We only know how to reverse scrolling when the player scrolls in the hotbar.
            return;
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }

        int scrollDirection = getScrollDirection(vertical);

        long windowHandle = MinecraftClient.getInstance().getWindow().getHandle();
        int modifierKeyCode = KeybindsPlus.scrollModifierFov.getDefaultKey().getCode();
        if (InputUtil.isKeyPressed(windowHandle, modifierKeyCode)) {
            undoHotbarScroll(player, scrollDirection);
            switch (scrollDirection) {
                case 1 -> KeybindsPlus.graphicsActions.increaseFov(MinecraftClient.getInstance());
                case -1 -> KeybindsPlus.graphicsActions.decreaseFov(MinecraftClient.getInstance());
            }
        }
    }

    private void undoHotbarScroll(ClientPlayerEntity player, int scrollDirection) {
        int selectedHotbarSlot = player.getInventory().selectedSlot;
        switch (scrollDirection) {
            case 1 -> player.getInventory().selectedSlot = (selectedHotbarSlot + 1) % HOTBAR_SIZE;
            case -1 -> player.getInventory().selectedSlot = (selectedHotbarSlot - 1 + HOTBAR_SIZE) % HOTBAR_SIZE;
        }
    }

    private int getScrollDirection(double offset) {
        if (offset > 0) {
            return 1;
        }
        else if (offset < 0) {
            return -1;
        }
        return 0;
    }
}
