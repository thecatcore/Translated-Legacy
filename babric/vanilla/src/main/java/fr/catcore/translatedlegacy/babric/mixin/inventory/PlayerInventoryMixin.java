package fr.catcore.translatedlegacy.babric.mixin.inventory;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Objects;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @WrapMethod(method = "getName")
    public String tl$getName(Operation<String> original) {
        String originalName = original.call();

        String key = Objects.equals(originalName, "Inventory") ? "container.inventory" : originalName;
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            return key;
        } else {
            return I18n.getTranslation(key);
        }
    }
}
