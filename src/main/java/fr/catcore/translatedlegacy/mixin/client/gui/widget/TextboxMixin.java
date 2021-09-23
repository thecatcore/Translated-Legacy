package fr.catcore.translatedlegacy.mixin.client.gui.widget;

import fr.catcore.translatedlegacy.font.BetterCharacterUtils;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.widgets.Textbox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Textbox.class)
public class TextboxMixin {

    @Shadow public boolean field_2421;

    @Shadow public boolean field_2420;

    @Shadow private Screen field_2430;

    @Shadow private String field_2427;

    @Shadow private int field_2428;

    /**
     * @author CatCore
     */
    @Overwrite
    public void method_1877(char c, int i) {
        if (this.field_2421 && this.field_2420) {
            if (c == '\t') {
                this.field_2430.method_135();
            }

            if (c == 22) {
                String var3 = Screen.getClipboardContents();
                if (var3 == null) {
                    var3 = "";
                }

                int var4 = 32 - this.field_2427.length();
                if (var4 > var3.length()) {
                    var4 = var3.length();
                }

                if (var4 > 0) {
                    this.field_2427 = this.field_2427 + var3.substring(0, var4);
                }
            }

            if (i == 14 && this.field_2427.length() > 0) {
                this.field_2427 = this.field_2427.substring(0, this.field_2427.length() - 1);
            }

            if (BetterCharacterUtils.getId(c) >= 32 && (this.field_2427.length() < this.field_2428 || this.field_2428 == 0)) {
                this.field_2427 = this.field_2427 + c;
            }

        }
    }
}
