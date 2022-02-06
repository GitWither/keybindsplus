package daniel.keybinds_plus.client.actions;

import net.minecraft.client.MinecraftClient;

public interface ActionType {
    void init();
    void execute(MinecraftClient client);
}
