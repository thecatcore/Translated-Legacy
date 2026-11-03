package fr.catcore.translatedlegacy.ornithe.common;

import fr.catcore.translatedlegacy.TranslatedLegacy;
import net.ornithemc.osl.core.impl.util.MinecraftVersion;
import net.ornithemc.osl.resource.loader.api.client.ClientResourceLoaderEvents;
import net.ornithemc.osl.resource.loader.api.resource.pack.PackPosition;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackRepository;
import net.ornithemc.osl.resource.loader.api.resource.repository.ResourcePackSummary;

import java.util.function.Consumer;

public class LanguagePackSource implements ResourcePackRepository.Source {

    public static void init() {
        ClientResourceLoaderEvents.INIT_RESOURCE_PACK_REPOSITORY.register((consumer) -> {
            consumer.addSource(new LanguagePackSource());
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
                PackPosition.TOP
        ));
    }
}
