package fr.catcore.translatedlegacy.ornithe.common.mixin.server.translations;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.ChestBlockEntity;
import net.ornithemc.conditionalmixin.annotations.Conditional;
import net.ornithemc.conditionalmixin.annotations.Version;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.SERVER)
@Conditional(minecraftVersion = @Version("<12w04a"))
@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin {
    /**
     * @author CatCore
     * @reason return translation key
     */
    @Overwrite
    public String getInventoryName() {
        return "container.chest";
    }
}
