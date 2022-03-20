package fr.catcore.translatedlegacy.mixin.inventory;

import net.minecraft.class_549;
import net.minecraft.class_629;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(class_549.class)
public class MinecartMixin {

    /**
     * @author CatCore
     */
    @Overwrite
    public String getName() {
        return class_629.method_2049("container.minecart");
    }
}
