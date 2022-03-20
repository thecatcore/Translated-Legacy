package fr.catcore.translatedlegacy.mixin.inventory;

import net.minecraft.class_225;
import net.minecraft.class_629;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(class_225.class)
public class ChestMixin {

    /**
     * @author CatCore
     */
    @Overwrite
    public String getName() {
        return class_629.method_2049("container.chest");
    }
}
