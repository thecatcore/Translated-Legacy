package fr.catcore.translatedlegacy.babric.mixin.client.inventory;

import fr.catcore.translatedlegacy.babric.TranslationHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.CLIENT)
@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    /**
     * @author CatCore
     * @reason return translated name
     */
    @Overwrite
    public String getName() {
        return TranslationHelper.get("backport.container.inventory", "container.inventory");
    }
}
