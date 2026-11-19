package fr.catcore.translatedlegacy.stapi.mixin.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.SERVER)
@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    /**
     * @author CatCore
     * @reason return translation key
     */
    @Overwrite
    public String getName() {
        return "container.inventory";
    }
}
