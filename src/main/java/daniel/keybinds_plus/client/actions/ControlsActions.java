package daniel.keybinds_plus.client.actions;

import daniel.keybinds_plus.client.KeybindsPlus;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;

import java.text.DecimalFormat;

public class ControlsActions implements ActionType {
    private KeyBinding autoJumpKeybind;
    private KeyBinding increaseSensitivity;
    private KeyBinding decreaseSensitivity;

    private final DecimalFormat sensitivityFormat = new DecimalFormat("#.###");


    @Override
    public void init() {
        autoJumpKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.auto_jump",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.CONTROLS_CATEGORY
        ));

        increaseSensitivity = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.increase_sensitivity",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.CONTROLS_CATEGORY
        ));

        decreaseSensitivity = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.decrease_sensitivity",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.CONTROLS_CATEGORY
        ));
    }

    @Override
    public void execute(MinecraftClient client) {
        while (increaseSensitivity.wasPressed()) {
            client.options.mouseSensitivity += 0.005;
            client.player.sendMessage(new LiteralText("Sensitivity was increased to " + sensitivityFormat.format(client.options.mouseSensitivity)), true);
        }
        while (decreaseSensitivity.wasPressed()) {
            client.options.mouseSensitivity -= 0.005;
            client.player.sendMessage(new LiteralText("Sensitivity was decreased to " + sensitivityFormat.format(client.options.mouseSensitivity)), true);
        }
        while (autoJumpKeybind.wasPressed()) {
            client.options.autoJump = !client.options.autoJump;
            client.player.sendMessage(new LiteralText("Auto Jump " + (client.options.autoJump ? "enabled" : "disabled")), true);
        }
    }
}
