package daniel.keybinds_plus.client.actions;

import daniel.keybinds_plus.client.KeybindsPlus;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

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
            client.options.getMouseSensitivity().setValue(client.options.getMouseSensitivity().getValue() + 0.005);
            client.player.sendMessage(Text.literal("Sensitivity was increased to " + sensitivityFormat.format(client.options.getMouseSensitivity().getValue())), true);
        }
        while (decreaseSensitivity.wasPressed()) {
            client.options.getMouseSensitivity().setValue(client.options.getMouseSensitivity().getValue() - 0.005);
            client.player.sendMessage(Text.literal("Sensitivity was decreased to " + sensitivityFormat.format(client.options.getMouseSensitivity().getValue())), true);
        }
        while (autoJumpKeybind.wasPressed()) {
            client.options.getAutoJump().setValue(!client.options.getAutoJump().getValue());
            client.player.sendMessage(Text.literal("Auto Jump " + (client.options.getAutoJump().getValue() ? "enabled" : "disabled")), true);
        }
    }
}
