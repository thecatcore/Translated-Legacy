package fr.catcore.translatedlegacy.ornithe.common;

import net.ornithemc.osl.resource.loader.api.resource.ResourceMetadata;
import net.ornithemc.osl.resource.loader.api.resource.pack.ResourcePackMetadata;
import net.ornithemc.osl.resource.loader.impl.ResourceLoader;
import net.ornithemc.osl.resource.loader.impl.resource.JsonResourceMetadata;
import net.ornithemc.osl.resource.loader.impl.resource.pack.DirectoryResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;
import net.ornithemc.osl.resource.loader.impl.resource.pack.SimpleResourcePackMetadata;
import net.ornithemc.osl.text.api.TextComponents;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

public class LanguagePack extends DirectoryResourcePack {
    public LanguagePack(Path directory) {
        super(directory);
    }

    @Override
    public <T> T getMetadata(String name, ResourceMetadata.Section.Serializer<T> serializer) throws IOException {
        InputStream resource = this.getResource("../" + METADATA_FILE);

        if (Objects.equals(name, ResourcePackMetadata.SECTION.name()) && serializer == ResourcePackMetadata.SECTION.serializer()) {
            return (T) new SimpleResourcePackMetadata(ResourcePacks.getSupportedFormat(), TextComponents.literal("A pack containing backported languages files"));
        }

        if (resource != null) {
            try {
                ResourceMetadata metadata = JsonResourceMetadata.fromInputStream(resource);

                if (metadata != null) {
                    return metadata.getSection(name, serializer);
                }
            } catch (Exception e) {
                ResourceLoader.LOGGER.error("Could not load " + this.getName() + " " + name + " metadata", e);
            }
        }

        return null;
    }
}
