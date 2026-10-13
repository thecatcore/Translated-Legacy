package fr.catcore.translatedlegacy.babric.mixin.inventory;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public String getName() {
        return I18n.getTranslation("container.inventory");
    }
}
