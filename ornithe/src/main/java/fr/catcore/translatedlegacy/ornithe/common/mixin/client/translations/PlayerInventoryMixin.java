package fr.catcore.translatedlegacy.ornithe.common.mixin.client.translations;

import fr.catcore.translatedlegacy.ornithe.common.TranslationHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.mob.player.PlayerInventory;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.CLIENT)
@Conditional(minecraftVersion = @Version("<12w04a"))
@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    /**
     * @author CatCore
     * @reason return translated name
     */
    @Overwrite
    public String getInventoryName() {
        return TranslationHelper.get("backport.container.inventory", "container.inventory");
    }
}
