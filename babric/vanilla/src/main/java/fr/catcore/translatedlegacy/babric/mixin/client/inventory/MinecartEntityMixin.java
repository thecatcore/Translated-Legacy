package fr.catcore.translatedlegacy.babric.mixin.client.inventory;

import fr.catcore.translatedlegacy.babric.TranslationHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.vehicle.MinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.CLIENT)
@Mixin(MinecartEntity.class)
public class MinecartEntityMixin {
    /**
     * @author CatCore
     * @reason return translated name
     */
    @Overwrite
    public String getName() {
        return TranslationHelper.get("backport.container.minecart", "container.minecart");
    }
}
