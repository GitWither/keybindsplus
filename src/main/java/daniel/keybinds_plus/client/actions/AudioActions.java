package daniel.keybinds_plus.client.actions;

import daniel.keybinds_plus.client.KeybindsPlus;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;

public class AudioActions implements ActionType {
    private final KeyBinding[] soundCategoryKeybinds = new KeyBinding[10];
    private final float[] previousVolumes = new float[10];

    private final SoundCategory[] soundCategories = SoundCategory.values();

    private KeyBinding toggleSubtitlesKeybind;

    @Override
    public void init() {
        toggleSubtitlesKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.keybinds_plus.toggle_subtitles",
                InputUtil.Type.KEYSYM,
                InputUtil.UNKNOWN_KEY.getCode(),
                KeybindsPlus.AUDIO_CATEGORY
        ));

        for (int i = 0; i < soundCategories.length; i++) {
            soundCategoryKeybinds[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.keybinds_plus.toggle_" + soundCategories[i].getName(),
                    InputUtil.Type.KEYSYM,
                    InputUtil.UNKNOWN_KEY.getCode(),
                    KeybindsPlus.AUDIO_CATEGORY
            ));
        }
    }

    @Override
    public void execute(MinecraftClient client) {
        while (toggleSubtitlesKeybind.wasPressed()) {
            client.options.getShowSubtitles().setValue(!client.options.getShowSubtitles().getValue());
            client.player.sendMessage(Text.literal("Subtitles " + (client.options.getShowSubtitles().getValue() ? "enabled" : "disabled")), true);
        }

        for (int i = 0; i < soundCategoryKeybinds.length; i++) {
            while (soundCategoryKeybinds[i].wasPressed()) {
                float currentSoundVolume = client.options.getSoundVolume(soundCategories[i]);

                if (currentSoundVolume == 0 && previousVolumes[i] == 0) {
                    client.options.setSoundVolume(soundCategories[i], 1f);
                    client.player.sendMessage(Text.literal(StringUtils.capitalize(soundCategories[i].getName()) + " sound source was resumed"), true);
                }
                else if (currentSoundVolume > 0) {
                    previousVolumes[i] = currentSoundVolume;
                    client.options.setSoundVolume(soundCategories[i], 0);
                    client.player.sendMessage(Text.literal(StringUtils.capitalize(soundCategories[i].getName()) + " sound source was muted"), true);
                }
                else if (currentSoundVolume == 0 && previousVolumes[i] > 0) {
                    client.options.setSoundVolume(soundCategories[i], previousVolumes[i]);
                    client.player.sendMessage(Text.literal(StringUtils.capitalize(soundCategories[i].getName()) + " sound source was resumed"), true);
                }
            }
        }

        /*
        while (toggleWeatherSoundsKeybind.wasPressed()) {
            float currentSoundVolume = client.options.getSoundVolume(SoundCategory.WEATHER);

            client.options.setSoundVolume(SoundCategory.WEATHER, client.options.getSoundVolume(SoundCategory.WEATHER) == 0 ? 100 : 0);
            client.player.sendMessage(new LiteralText("Weather sounds were " + (client.options.getSoundVolume(SoundCategory.WEATHER) == 0 ? "muted" : "resumed")), true);
        }

         */
    }
}
