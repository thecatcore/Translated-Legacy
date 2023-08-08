package fr.catcore.translatedlegacy.babric.mixin;

import net.minecraft.class_629;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(World.class)
public class LevelMixin {

    @ModifyArg(method = "method_195", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_62;method_1795(Ljava/lang/String;)V"))
    private String saveLevel$translate(String string) {
        return class_629.method_2049("menu.savingLevel");
    }


    @ModifyArg(method = "method_195", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_62;method_1796(Ljava/lang/String;)V"))
    private String saveLevel$translate2(String string) {
        return class_629.method_2049("menu.savingChunks");
    }
}
