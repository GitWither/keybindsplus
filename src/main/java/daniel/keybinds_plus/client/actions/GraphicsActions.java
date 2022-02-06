package daniel.keybinds_plus.client.actions;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;

import java.text.DecimalFormat;

import static daniel.keybinds_plus.client.KeybindsPlus.GRAPHICS_CATEGORY;

public class GraphicsActions implements ActionType {
    private KeyBinding increaseBrightnessKeybind;
    private KeyBinding decreaseBrightnessKeybind;
    private KeyBinding decreaseFovKeybind;
    private KeyBinding increaseFovKeybind;
    private KeyBinding toggleViewBobbing;

    private final DecimalFormat fovFormat = new DecimalFormat("#.###");

    @Override
    public void init() {
        increaseBrightnessKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.increase_brightness",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));
        decreaseBrightnessKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.decrease_brightness",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));

        increaseFovKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.increase_fov",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));
        decreaseFovKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.decrease_fov",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));

        toggleViewBobbing = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.toggle_view_bobbing",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));
    }

    @Override
    public void execute(MinecraftClient client) {
        while (toggleViewBobbing.wasPressed()) {
            client.options.bobView = !client.options.bobView;
            client.player.sendMessage(new LiteralText("View Bobbing " + (client.options.bobView ? "enabled" : "disabled")), true);
        }

        while (increaseBrightnessKeybind.wasPressed()) {
            client.options.gamma += 0.1;
            client.player.sendMessage(new LiteralText("Brightness was increased to " + fovFormat.format(client.options.gamma)), true);

        }

        while (decreaseBrightnessKeybind.wasPressed()) {
            client.options.gamma -= 0.1;
            client.player.sendMessage(new LiteralText("Brightness was decreased to " + fovFormat.format(client.options.gamma)), true);

        }

        while (increaseFovKeybind.wasPressed()) {
            client.options.fov += 0.1;
            client.player.sendMessage(new LiteralText("FOV was increased to " + fovFormat.format(client.options.fov)), true);
        }

        while (decreaseFovKeybind.wasPressed()) {
            client.options.fov -= 0.1;
            client.player.sendMessage(new LiteralText("FOV was decreased to " + fovFormat.format(client.options.fov)), true);
        }
    }
}
