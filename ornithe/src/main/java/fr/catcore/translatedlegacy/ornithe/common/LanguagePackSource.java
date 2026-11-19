package fr.catcore.translatedlegacy.ornithe.common;

import fr.catcore.translatedlegacy.TranslatedLegacy;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.lifecycle.api.client.MinecraftClientEvents;
import net.ornithemc.osl.localization.api.L10n;
import net.ornithemc.osl.localization.api.language.Language;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.repository.PackSourcePosition;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

import java.util.function.Consumer;

public class LanguagePackSource implements ResourcePackRepository.Source {
    private static final boolean LANG_IN_REPORT = MinecraftVersion.resolve().compareTo("1.6.2-091847") >= 0;

    public static void init() {
        ClientResourceLoaderEvents.INIT_RESOURCE_PACK_REPOSITORY.register((consumer) -> {
            consumer.addSource(new LanguagePackSource(), PackSourcePosition.FIRST);
        });
        if (!LANG_IN_REPORT) MinecraftClientEvents.START.register(minecraft -> {
            minecraft.addCrashListener((report, section) -> section.addDetail("Current Language", () -> {
                Language language = L10n.getLanguage();
                return language != null ? language.name() + " (" + language.region() + ")" : "Unknown";
            }));
        });
    }

    @Override
    public void loadResourcePacks(Consumer<ResourcePackSummary> consumer) {
        TranslatedLegacy.updateLanguageList();

        consumer.accept(ResourcePackSummary.create(
                new LanguagePack(TranslatedLegacy.getLangsFolder(MinecraftVersion.resolve().gameVersion()).toPath()),
                "language-pack",
                true,
                false,
                PackPosition.BOTTOM
        ));
    }
}
