package fr.catcore.translatedlegacy.stapi.mixin.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.SERVER)
@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin {
    /**
     * @author CatCore
     * @reason return translation key
     */
    @Overwrite
    public String getName() {
        return "container.chest";
    }
}
