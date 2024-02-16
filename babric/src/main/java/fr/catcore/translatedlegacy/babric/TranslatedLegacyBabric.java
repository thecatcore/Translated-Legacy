package fr.catcore.translatedlegacy.babric;


import fr.catcore.translatedlegacy.api.TextRenderer;
import fr.catcore.translatedlegacy.babric.fontnewnew.OriginalGlyphProvider;
import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import fr.catcore.translatedlegacy.babric.font.BetterTextRenderer;
import net.fabricmc.api.ModInitializer;

public class TranslatedLegacyBabric implements ModInitializer {

	public static BetterTextRenderer TEXT_RENDERER;

	@Override
	public void onInitialize() {
		LanguageManager.init();
	}

	public static void registerProviders() {
		TextRenderer.registerProvider(new OriginalGlyphProvider());
	}
}
