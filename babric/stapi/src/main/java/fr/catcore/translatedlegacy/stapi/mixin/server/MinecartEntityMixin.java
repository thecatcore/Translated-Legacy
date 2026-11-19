package fr.catcore.translatedlegacy.stapi.mixin.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.vehicle.MinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.SERVER)
@Mixin(MinecartEntity.class)
public class MinecartEntityMixin {
    /**
     * @author CatCore
     * @reason return translation key
     */
    @Overwrite
    public String getName() {
        return "container.minecart";
    }
}
