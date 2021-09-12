package fr.catcore.translatedlegacy.mixin.inventory;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    /**
     * @author CatCore
     */
    @Overwrite
    public String getContainerName() {
        return I18n.translate("container.inventory");
    }
}
