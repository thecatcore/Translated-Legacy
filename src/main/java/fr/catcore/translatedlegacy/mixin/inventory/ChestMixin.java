package fr.catcore.translatedlegacy.mixin.inventory;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.tile.entity.Chest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Chest.class)
public class ChestMixin {

    /**
     * @author CatCore
     */
    @Overwrite
    public String getContainerName() {
        return I18n.translate("container.chest");
    }
}
