package daniel.keybinds_plus.client;

import daniel.keybinds_plus.client.actions.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.text.DecimalFormat;

@Environment(EnvType.CLIENT)
public class KeybindsPlus implements ClientModInitializer {

    private static final String BASE_CATEGORY = "category.keybinds_plus.";

    public static final String SCROLL_MODIFIER_CATEGORY = BASE_CATEGORY + "scroll_modifiers";
    public static final String SKIN_CATEGORY = BASE_CATEGORY + "skins";
    public static final String CONTROLS_CATEGORY = BASE_CATEGORY + "controls";
    public static final String AUDIO_CATEGORY = BASE_CATEGORY + "audio";
    public static final String GRAPHICS_CATEGORY = BASE_CATEGORY + "graphics";
    public static final String CAMERA_CATEGORY = BASE_CATEGORY + "camera";

    public static GraphicsActions graphicsActions = new GraphicsActions();

    public ActionType[] actionTypes = new ActionType[] {
            new AudioActions(),
            new CameraActions(),
            new ControlsActions(),
            graphicsActions,
            new SkinActions()
    };

    public static KeyBinding scrollModifierFov = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.keybinds_plus.change_fov",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            SCROLL_MODIFIER_CATEGORY
    ));

    @Override
    public void onInitializeClient() {
        for (ActionType actionType : actionTypes) {
            actionType.init();
        }

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (handler.getConnection().isLocal()) return;
            //this is a bit cringe
            if (((SkinActions)actionTypes[4]).isAnySet() && client != null) {
                client.player.sendMessage(
                        Text.literal("WARNING: Using skin toggling settings may trigger anti-cheat plugins!")
                                .setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.RED))),
                        false
                );
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(this::clientEndTick);
    }

    private void clientEndTick(MinecraftClient client) {
        if (client.player == null) return;

        for (ActionType actionType : actionTypes) {
            actionType.execute(client);
        }
    }
}
