package daniel.keybinds_plus.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;

@Environment(EnvType.CLIENT)
public class KeybindsPlus implements ClientModInitializer {

    private static final String BASE_CATEGORY = "category.keybinds_plus.";

    private static final String SKIN_CATEGORY = BASE_CATEGORY + "skins";
    private static final String CONTROLS_CATEGORY = BASE_CATEGORY + "controls";
    private static final String AUDIO_CATEGORY = BASE_CATEGORY + "audio";
    private static final String GRAPHICS_CATEGORY = BASE_CATEGORY + "graphics";

    private static KeyBinding AUTO_JUMP_KEYBIND;
    private static final KeyBinding[] SKIN_KEYBINDS = new KeyBinding[8];
    private static KeyBinding SUBTITLES_KEYBIND;

    private static KeyBinding INCREASE_BRIGHTNESS;
    private static KeyBinding DECREASE_BRIGHTNESS;
    private static KeyBinding DECREASE_FOV;
    private static KeyBinding INCREASE_FOV;
    private static KeyBinding VIEW_BOBBING;
    private static KeyBinding WEATHER_SOUNDS;

    private static final DecimalFormat FOV_FORMAT = new DecimalFormat("#.###");

    @Override
    public void onInitializeClient() {

        //skin
        PlayerModelPart[] playerParts = PlayerModelPart.values();
        for (int i = 0; i < playerParts.length; i++) {
            SKIN_KEYBINDS[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.keybinds_plus.toggle_" + playerParts[i].getName(),
                    InputUtil.Type.KEYSYM,
                    InputUtil.UNKNOWN_KEY.getCode(),
                    SKIN_CATEGORY
            ));
        }

        //graphics
        INCREASE_BRIGHTNESS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.increase_brightness",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));
        DECREASE_BRIGHTNESS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.decrease_brightness",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));

        INCREASE_FOV = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.increase_fov",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));
        DECREASE_FOV = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.decrease_fov",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));

        VIEW_BOBBING = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.toggle_view_bobbing",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                GRAPHICS_CATEGORY
        ));


        //controls
        AUTO_JUMP_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.auto_jump",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                CONTROLS_CATEGORY
        ));

        //audio
        SUBTITLES_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.toggle_subtitles",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                AUDIO_CATEGORY
        ));

        WEATHER_SOUNDS = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.toggle_weather_sounds",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                AUDIO_CATEGORY
        ));



        ClientTickEvents.END_CLIENT_TICK.register(this::clientEndTick);
    }

    private void clientEndTick(MinecraftClient client) {
        if (client.player == null) return;

        while (AUTO_JUMP_KEYBIND.wasPressed()) {
            client.options.autoJump = !client.options.autoJump;
            client.player.sendMessage(new LiteralText("Auto Jump " + (client.options.autoJump ? "enabled" : "disabled")), true);
        }

        while (SUBTITLES_KEYBIND.wasPressed()) {
            client.options.showSubtitles = !client.options.showSubtitles;
            client.player.sendMessage(new LiteralText("Subtitles " + (client.options.showSubtitles ? "enabled" : "disabled")), true);
        }

        for (int i = 0; i < SKIN_KEYBINDS.length; i++) {
            if (SKIN_KEYBINDS[i] == null) continue;

            while (SKIN_KEYBINDS[i].wasPressed()) {
                PlayerModelPart part = PlayerModelPart.values()[i];
                boolean enabled = client.options.isPlayerModelPartEnabled(part);
                client.options.togglePlayerModelPart(part, !enabled);
                client.player.sendMessage(new LiteralText(part.getOptionName().getString() +" " + (enabled ? "enabled" : "disabled")), true);

            }
        }

        while (VIEW_BOBBING.wasPressed()) {
            client.options.bobView = !client.options.bobView;
            client.player.sendMessage(new LiteralText("View Bobbing " + (client.options.bobView ? "enabled" : "disabled")), true);
        }

        while (INCREASE_BRIGHTNESS.wasPressed()) {
            client.options.gamma += 0.1;
            client.player.sendMessage(new LiteralText("Brightness was increased to " + FOV_FORMAT.format(client.options.gamma)), true);

        }

        while (DECREASE_BRIGHTNESS.wasPressed()) {
            client.options.gamma -= 0.1;
            client.player.sendMessage(new LiteralText("Brightness was decreased to " + FOV_FORMAT.format(client.options.gamma)), true);

        }

        while (INCREASE_FOV.wasPressed()) {
            client.options.fov += 0.1;
            client.player.sendMessage(new LiteralText("FOV was increased to " + FOV_FORMAT.format(client.options.fov)), true);
        }

        while (DECREASE_FOV.wasPressed()) {
            client.options.fov -= 0.1;
            client.player.sendMessage(new LiteralText("FOV was decreased to " + FOV_FORMAT.format(client.options.fov)), true);
        }

        while (WEATHER_SOUNDS.wasPressed()) {
            client.options.setSoundVolume(SoundCategory.WEATHER, client.options.getSoundVolume(SoundCategory.WEATHER) == 0 ? 100 : 0);
            client.player.sendMessage(new LiteralText("Weather sounds were " + (client.options.getSoundVolume(SoundCategory.WEATHER) == 0 ? "muted" : "resumed")), true);
        }
    }
}
