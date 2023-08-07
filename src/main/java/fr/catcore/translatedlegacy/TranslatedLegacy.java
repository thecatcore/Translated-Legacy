package fr.catcore.translatedlegacy;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class TranslatedLegacy {
    public static final File CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve("translated-legacy").toFile();
    public static final File META_FILE = new File(CONFIG_FOLDER, "pack.mcmeta");

    public static final long MONTH = 2_628_000_000L;

    public static void updateLanguageList() {
        boolean update = false;

        if (!CONFIG_FOLDER.exists()) {
            CONFIG_FOLDER.mkdirs();
            update = true;
        }

        if (!update) {
            if (META_FILE.exists()) {
                long lastModified = META_FILE.lastModified() + MONTH;

                if (System.currentTimeMillis() > lastModified) {
                    update = true;
                }
            } else {
                update = true;
            }
        }

        try {
            URL mcmetaURL = new URL(getRedirectedUrl("https://translations.catcore.fr/mcmeta"));

            if (update) {
                InputStream inputStream = mcmetaURL.openStream();

                Files.copy(
                        inputStream,
                        META_FILE.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                inputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getLangFile(String mcVersion, String code) {
        File versionFolder = new File(CONFIG_FOLDER, mcVersion);

        boolean update = false;

        if (!versionFolder.exists()) {
            update = true;
            versionFolder.mkdirs();
        }

        File langFile = new File(versionFolder, code + ".json");

        if (!update) {
            if (langFile.exists()) {
                long lastModified = langFile.lastModified() + MONTH;

                if (System.currentTimeMillis() > lastModified) {
                    update = true;
                }
            } else {
                update = true;
            }
        }

        if (update) {
            try {
                URL url = new URL(getRedirectedUrl("https://translations.catcore.fr/lang/" + mcVersion + "/" + code));

                InputStream inputStream = url.openStream();

                Files.copy(
                        inputStream,
                        langFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return langFile;
    }

    public static String getRedirectedUrl(String url) throws MalformedURLException, IOException {
        HttpURLConnection connection;
        String finalUrl = url;//from   w  w  w .  ja va2  s  .  co m
        do {
            connection = (HttpURLConnection) new URL(finalUrl).openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode >= 300 && responseCode < 400) {
                String redirectedUrl = connection.getHeaderField("Location");
                if (redirectedUrl == null)
                    break;
                finalUrl = redirectedUrl;
            } else
                break;
        } while (connection.getResponseCode() != HttpURLConnection.HTTP_OK);
        connection.disconnect();
        return finalUrl.replaceAll(" ", "%20");
    }
}
