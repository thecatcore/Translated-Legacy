package fr.catcore.translatedlegacy.mixin.compat;

import net.modificationstation.stationapi.api.lang.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

import java.io.IOException;
import java.util.Properties;

@Pseudo
@Mixin(I18n.class)
public class StationAPII18n {

    /**
     * @author Cat Core
     * @reason Not needed because it's already handled by Translated Legacy.
     */
    @Overwrite(remap = false)
    private static void loadLang(Properties translations, String path, String modid) throws IOException {

    }
}
