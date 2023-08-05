package fr.catcore.translatedlegacy;


import fr.catcore.translatedlegacy.font.BetterTextRenderer;
import fr.catcore.translatedlegacy.language.LanguageManager;
import net.fabricmc.api.ModInitializer;

public class TranslatedLegacy implements ModInitializer {

	public static BetterTextRenderer TEXT_RENDERER;

	@Override
	public void onInitialize() {
		LanguageManager.init();
	}
}
