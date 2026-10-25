package fr.catcore.translatedlegacy.ornithe;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.core.api.util.NamespacedIdentifiers;
import net.ornithemc.osl.networking.api.ChannelRegistry;
import net.ornithemc.osl.text.api.TextComponent;

import java.util.HashMap;
import java.util.Map;

public class NetworkingHelper {
    public static NamespacedIdentifier UPDATE_ID = ChannelRegistry.register(NamespacedIdentifiers.from("translated-legacy", "update"), false, true);
    public static NamespacedIdentifier PING_ID = ChannelRegistry.register(NamespacedIdentifiers.from("translated-legacy", "ping"), true, false);
    public static NamespacedIdentifier TRANSLATE_ID = ChannelRegistry.register(NamespacedIdentifiers.from("translated-legacy", "translate"), true, false);

    public static boolean serverSupportsUnicode = false;

    public static Map<Integer, TextComponent[]> packetTranslations = new HashMap<>();

    public static void init() {}
}
