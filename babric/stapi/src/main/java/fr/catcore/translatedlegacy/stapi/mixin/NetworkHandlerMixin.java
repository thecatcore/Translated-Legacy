package fr.catcore.translatedlegacy.stapi.mixin;

import fr.catcore.translatedlegacy.stapi.TranslatedLegacyNetworkHandler;
import net.minecraft.network.NetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NetworkHandler.class)
public class NetworkHandlerMixin implements TranslatedLegacyNetworkHandler {
    @Unique
    private boolean lf$translationDelegated;

    @Override
    public void setDelegateTranslation(boolean b) {
        lf$translationDelegated = b;
    }

    @Override
    public boolean isTranslationDelegated() {
        return lf$translationDelegated;
    }
}
