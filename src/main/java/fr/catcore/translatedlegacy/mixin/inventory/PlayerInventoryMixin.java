package fr.catcore.translatedlegacy.mixin.inventory;

import net.minecraft.class_629;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    /**
     * @author CatCore
     */
    @Overwrite
    public String getName() {
        return class_629.method_2049("container.inventory");
    }
}
