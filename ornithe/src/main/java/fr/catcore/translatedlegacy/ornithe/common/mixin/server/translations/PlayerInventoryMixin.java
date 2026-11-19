package fr.catcore.translatedlegacy.ornithe.common.mixin.server.translations;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.mob.player.PlayerInventory;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.SERVER)
@Conditional(minecraftVersion = @Version("<12w04a"))
@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    /**
     * @author CatCore
     * @reason return translation key
     */
    @Overwrite
    public String getInventoryName() {
        return "container.inventory";
    }
}
