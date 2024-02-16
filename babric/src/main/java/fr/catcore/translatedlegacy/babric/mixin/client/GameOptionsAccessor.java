package fr.catcore.translatedlegacy.babric.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameOptions.class)
public interface GameOptionsAccessor {
    @Accessor
    Minecraft getMinecraft();
}
