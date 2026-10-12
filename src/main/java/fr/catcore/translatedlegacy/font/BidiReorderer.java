package fr.catcore.translatedlegacy.font;

import fr.catcore.translatedlegacy.util.TextUtils;

import java.text.Bidi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Turns a line of styled text from logical order (the order it is typed and stored in) into the
 * order it must be drawn from left to right, so that right-to-left scripts such as Hebrew, Yiddish
 * and Arabic read correctly, including when mixed with left-to-right text and numbers.
 */
public final class BidiReorderer {
    private static final Map<Character, Character> MIRRORED = new HashMap<>();

    static {
        String pairs = "()[]{}<>«»‹›⁅⁆⁽⁾₍₎≤≥≦≧≪≫⟨⟩⟪⟫⟦⟧〈〉《》「」『』【】〔〕〖〗〘〙〚〛";

        for (int i = 0; i < pairs.length(); i += 2) {
            MIRRORED.put(pairs.charAt(i), pairs.charAt(i + 1));
            MIRRORED.put(pairs.charAt(i + 1), pairs.charAt(i));
        }
    }

    private BidiReorderer() {
    }

    /**
     * @param infos one line of text, in logical order, split into runs of identical style
     * @return the same text in visual order, split into runs of identical style
     */
    public static List<TextInfo> toVisualOrder(List<TextInfo> infos) {
        int length = 0;

        for (TextInfo info : infos) {
            length += info.text.length();
        }

        char[] chars = new char[length];
        Style[] styles = new Style[length];
        int index = 0;

        for (TextInfo info : infos) {
            for (int i = 0; i < info.text.length(); i++) {
                chars[index] = info.text.charAt(i);
                styles[index] = info.style;
                index++;
            }
        }

        if (!Bidi.requiresBidi(chars, 0, length)) return infos;

        Bidi bidi = new Bidi(chars, 0, null, 0, length, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);

        if (bidi.isLeftToRight()) return infos;

        int runCount = bidi.getRunCount();
        byte[] levels = new byte[runCount];
        Integer[] runs = new Integer[runCount];

        for (int i = 0; i < runCount; i++) {
            levels[i] = (byte) bidi.getRunLevel(i);
            runs[i] = i;
        }

        Bidi.reorderVisually(levels, 0, runs, 0, runCount);

        char[] visualChars = new char[length];
        Style[] visualStyles = new Style[length];
        index = 0;

        for (int run : runs) {
            int start = bidi.getRunStart(run);
            int limit = bidi.getRunLimit(run);

            if ((bidi.getRunLevel(run) & 1) == 0) {
                for (int i = start; i < limit; i++) {
                    visualChars[index] = chars[i];
                    visualStyles[index] = styles[i];
                    index++;
                }

                continue;
            }

            // Right-to-left: reverse whole clusters, so combining marks stay right after their base letter
            int clusterEnd = limit;

            while (clusterEnd > start) {
                int clusterStart = clusterEnd - 1;

                while (clusterStart > start && TextUtils.continuesCluster(chars[clusterStart])) {
                    clusterStart--;
                }

                for (int i = clusterStart; i < clusterEnd; i++) {
                    char c = chars[i];

                    if (i == clusterStart) {
                        c = MIRRORED.getOrDefault(c, c);
                    }

                    visualChars[index] = c;
                    visualStyles[index] = styles[i];
                    index++;
                }

                clusterEnd = clusterStart;
            }
        }

        List<TextInfo> visualInfos = new ArrayList<>();
        int runStart = 0;

        for (int i = 1; i <= length; i++) {
            if (i == length || visualStyles[i] != visualStyles[runStart]) {
                visualInfos.add(new TextInfo(new String(visualChars, runStart, i - runStart), visualStyles[runStart]));
                runStart = i;
            }
        }

        return visualInfos;
    }
}
