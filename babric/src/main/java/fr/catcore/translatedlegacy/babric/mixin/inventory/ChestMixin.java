package fr.catcore.translatedlegacy.babric.mixin.inventory;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ChestBlockEntity.class)
public class ChestMixin {

    /**
     * @author CatCore
     * @reason We don't want any of the original code to run.
     */
    @Overwrite
    public String getName() {
        return I18n.getTranslation("container.chest");
    }
}
