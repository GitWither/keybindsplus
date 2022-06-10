package daniel.keybinds_plus.client.actions;

import daniel.keybinds_plus.client.KeybindsPlus;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;

public class SkinActions implements ActionType {
    private final KeyBinding[] skinKeybinds = new KeyBinding[7];
    private KeyBinding toggleMainHandKeybind;

    @Override
    public void init() {
        toggleMainHandKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
           "key.keybinds_plus.toggle_main_hand",
           InputUtil.Type.KEYSYM,
           InputUtil.UNKNOWN_KEY.getCode(),
           KeybindsPlus.SKIN_CATEGORY
        ));

        PlayerModelPart[] playerParts = PlayerModelPart.values();
        for (int i = 0; i < playerParts.length; i++) {
            skinKeybinds[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.keybinds_plus.toggle_" + playerParts[i].getName(),
                    InputUtil.Type.KEYSYM,
                    InputUtil.UNKNOWN_KEY.getCode(),
                    KeybindsPlus.SKIN_CATEGORY
            ));
        }
    }

    @Override
    public void execute(MinecraftClient client) {
        while (toggleMainHandKeybind.wasPressed()) {
            client.options.getMainArm().setValue(client.options.getMainArm().getValue() == Arm.LEFT ? Arm.RIGHT : Arm.LEFT);
            client.options.sendClientSettings();
            client.player.sendMessage(Text.literal("Main hand set to " + client.options.getMainArm().toString()), true);
        }

        for (int i = 0; i < skinKeybinds.length; i++) {
            if (skinKeybinds[i] == null) continue;

            while (skinKeybinds[i].wasPressed()) {
                PlayerModelPart part = PlayerModelPart.values()[i];
                boolean enabled = client.options.isPlayerModelPartEnabled(part);
                client.options.togglePlayerModelPart(part, !enabled);
                client.player.sendMessage(Text.literal(part.getOptionName().getString() +" " + (enabled ? "enabled" : "disabled")), true);
            }
        }
    }

    public boolean isAnySet() {
        if (!toggleMainHandKeybind.isUnbound()) {
            return true;
        }

        for (KeyBinding keybinding : skinKeybinds) {
            if (!keybinding.isUnbound()) {
                return true;
            }
        }

        return false;
    }
}
