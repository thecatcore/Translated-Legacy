package fr.catcore.translatedlegacy.ornithe.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.ornithemc.osl.resource.loader.api.resource.ResourceType;
import net.ornithemc.osl.resource.loader.impl.resource.pack.DirectoryResourcePack;
import net.ornithemc.osl.resource.loader.impl.resource.pack.ResourcePacks;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class LanguagePack extends DirectoryResourcePack {
    private static final Gson GSON = new GsonBuilder().create();

    public LanguagePack(Path directory) {
        super(directory);
    }

    @Override
    public InputStream getResource(String pathName) throws IOException {
        boolean meta = false;
        if (pathName.equals("pack.mcmeta")) {
            pathName = "../" + pathName;
            meta = true;
        }

        InputStream stream = super.getResource(pathName);
        if (meta) {
            try(Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                JsonObject metadata = GSON.fromJson(reader, JsonObject.class);
                JsonObject info = new JsonObject();
                info.addProperty("description", "A pack containing backported languages files");
                info.addProperty("pack_format", ResourcePacks.getSupportedFormat());
                metadata.add("pack", info);

                return new ByteArrayInputStream(GSON.toJson(metadata).getBytes(StandardCharsets.UTF_8));
            }
        }
        return stream;
    }

    @Override
    protected Map<ResourceType, Set<String>> findNamespaces() {
        return Collections.singletonMap(ResourceType.CLIENT_ASSETS, Collections.singleton("minecraft"));
    }

    @Override
    public String getName() {
        return "Language Pack";
    }
}
