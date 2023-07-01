package daniel.keybinds_plus.client.actions;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

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
            client.options.getBobView().setValue(!client.options.getBobView().getValue());
            client.player.sendMessage(Text.literal("View Bobbing " + (client.options.getBobView().getValue() ? "enabled" : "disabled")), true);
        }

        while (increaseBrightnessKeybind.wasPressed()) {
            client.options.getGamma().setValue(client.options.getGamma().getValue() + 0.1);
            client.player.sendMessage(Text.literal("Brightness was increased to " + fovFormat.format(client.options.getGamma().getValue())), true);

        }

        while (decreaseBrightnessKeybind.wasPressed()) {
            client.options.getGamma().setValue(client.options.getGamma().getValue() - 0.1);
            client.player.sendMessage(Text.literal("Brightness was decreased to " + fovFormat.format(client.options.getGamma().getValue())), true);

        }

        while (increaseFovKeybind.wasPressed()) changeFov(client, +1);
        while (decreaseFovKeybind.wasPressed()) changeFov(client, -1);
    }

    public void changeFov(MinecraftClient client, int delta) {
        int oldValue = client.options.getFov().getValue();
        int expectedValue = oldValue + delta;
        client.options.getFov().setValue(expectedValue);
        if (expectedValue != client.options.getFov().getValue()) {
            client.options.getFov().setValue(oldValue);
        }
        else {
            client.player.sendMessage(Text.literal(String.format("FOV was %s to %s",
                    delta > 0 ? "increased" : "decreased", fovFormat.format(client.options.getFov().getValue()))), true);
        }
    }
}
