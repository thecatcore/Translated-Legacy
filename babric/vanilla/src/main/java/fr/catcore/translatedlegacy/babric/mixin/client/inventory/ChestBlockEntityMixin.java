package fr.catcore.translatedlegacy.babric.mixin.client.inventory;

import fr.catcore.translatedlegacy.babric.TranslationHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.ChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Environment(EnvType.CLIENT)
@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin {
    /**
     * @author CatCore
     * @reason return translation
     */
    @Overwrite
    public String getName() {
        return TranslationHelper.get("backport.container.chest", "container.chest");
    }
}
