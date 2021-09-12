package fr.catcore.translatedlegacy.mixin.inventory;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Minecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Minecart.class)
public class MinecartMixin {

    /**
     * @author CatCore
     */
    @Overwrite
    public String getContainerName() {
        return I18n.translate("container.minecart");
    }
}
