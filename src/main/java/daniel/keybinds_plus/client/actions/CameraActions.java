package daniel.keybinds_plus.client.actions;

import daniel.keybinds_plus.client.KeybindsPlus;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class CameraActions implements ActionType {
    private Perspective lastPerspective = Perspective.FIRST_PERSON;

    private KeyBinding firstPersonKeybind;
    private KeyBinding thirdPersonKeybind;
    private KeyBinding thirdPersonFrontKeybind;

    private KeyBinding tempFirstPersonKeybind;
    private KeyBinding tempThirdPersonKeybind;
    private KeyBinding tempThirdPersonFrontKeybind;

    private boolean holdingFirstPerson = false;
    private boolean holdingThirdPerson = false;
    private boolean holdingThirdPersonFront = false;

    @Override
    public void init() {
        firstPersonKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.set_first_person",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.CAMERA_CATEGORY
        ));

        thirdPersonKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.set_third_person",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.CAMERA_CATEGORY
        ));

        thirdPersonFrontKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.set_third_person_front",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.CAMERA_CATEGORY
        ));

        tempFirstPersonKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.set_first_person_temp",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.CAMERA_CATEGORY
        ));

        tempThirdPersonKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.set_third_person_temp",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.CAMERA_CATEGORY
        ));

        tempThirdPersonFrontKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.set_third_person_front_temp",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.CAMERA_CATEGORY
        ));
    }

    @Override
    public void execute(MinecraftClient client) {
        while (firstPersonKeybind.wasPressed()) {
            client.options.setPerspective(Perspective.FIRST_PERSON);
            client.player.sendMessage(Text.literal("Perspective set to " + client.options.getPerspective().name()), true);
        }

        while (thirdPersonKeybind.wasPressed()) {
            client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            client.player.sendMessage(Text.literal("Perspective set to " + client.options.getPerspective().name()), true);
        }

        while (thirdPersonFrontKeybind.wasPressed()) {
            client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            client.player.sendMessage(Text.literal("Perspective set to " + client.options.getPerspective().name()), true);
        }

        if (!holdingFirstPerson && !holdingThirdPersonFront && !holdingThirdPerson && tempFirstPersonKeybind.isPressed()) {
            holdingFirstPerson = true;
            lastPerspective = client.options.getPerspective();
            client.options.setPerspective(Perspective.FIRST_PERSON);
        }
        else if (holdingFirstPerson && !tempFirstPersonKeybind.isPressed()) {
            holdingFirstPerson = false;
            client.options.setPerspective(lastPerspective);
            lastPerspective = client.options.getPerspective();
        }

        if (!holdingThirdPerson && !holdingFirstPerson && !holdingThirdPersonFront && tempThirdPersonKeybind.isPressed()) {
            holdingThirdPerson = true;
            lastPerspective = client.options.getPerspective();
            client.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        }
        else if (holdingThirdPerson && !tempThirdPersonKeybind.isPressed()) {
            holdingThirdPerson = false;
            client.options.setPerspective(lastPerspective);
            lastPerspective = client.options.getPerspective();
        }

        if (!holdingThirdPersonFront && !holdingThirdPerson && !holdingFirstPerson && tempThirdPersonFrontKeybind.isPressed()) {
            holdingThirdPersonFront = true;
            lastPerspective = client.options.getPerspective();
            client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
        }
        else if (holdingThirdPersonFront && !tempThirdPersonFrontKeybind.isPressed()) {
            holdingThirdPersonFront = false;
            client.options.setPerspective(lastPerspective);
            lastPerspective = client.options.getPerspective();
        }
    }
}
