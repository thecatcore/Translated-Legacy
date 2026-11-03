package fr.catcore.translatedlegacy.babric;

import fr.catcore.translatedlegacy.babric.language.LanguageManager;
import net.fabricmc.api.ModInitializer;

public class TranslatedLegacyBabric implements ModInitializer {

	@Override
	public void onInitialize() {
		LanguageManager.init();
	}
}
