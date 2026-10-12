package fr.catcore.translatedlegacy.util;

import java.text.Bidi;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.ToIntFunction;

public class TextUtils {
    private static final Locale THAI = new Locale("th");
    private static final char RIGHT_TO_LEFT_MARK = '\u200F';

    public static int isCharacterAllowed(int character) {
        return character != 167 && character >= ' ' && character != 127 ? 0 : -1;
    }

    /**
     * Whether the character is drawn on top of / around the preceding letter instead of after it
     * (Thai vowels and tone marks, Hebrew points, Devanagari and Tamil vowel signs, accents...).
     */
    public static boolean isCombiningMark(char c) {
        int type = Character.getType(c);

        return type == Character.NON_SPACING_MARK
                || type == Character.ENCLOSING_MARK
                || type == Character.COMBINING_SPACING_MARK;
    }

    /**
     * Whether the character belongs to the same grapheme cluster as the one before it,
     * meaning text must never be split, reordered or wrapped right before it.
     */
    public static boolean continuesCluster(char c) {
        return isCombiningMark(c) || c == '‍' || c == '‌' || Character.isLowSurrogate(c);
    }

    /**
     * Invisible characters that only steer bidirectional reordering (LRM, RLM, embeddings, isolates...).
     */
    public static boolean isBidiControl(char c) {
        return c == '‎' || c == '‏' || c == '؜'
                || (c >= '‪' && c <= '‮')
                || (c >= '⁦' && c <= '⁩');
    }

    /**
     * Moves {@code index} back so that it doesn't fall inside a grapheme cluster or between a
     * formatting code's {@code §} and its letter.
     */
    public static int toClusterBoundary(String text, int index) {
        while (index > 0 && index < text.length() && continuesCluster(text.charAt(index))) {
            index--;
        }

        if (index > 0 && text.charAt(index - 1) == '§') {
            index--;
        }

        return index;
    }

    /**
     * Splits a single paragraph (no {@code \n}) into lines no wider than {@code maxWidth}.
     * Lines are broken at the places Unicode allows (spaces, and between words in scripts written
     * without spaces such as Thai); a word longer than a whole line is broken between grapheme clusters.
     * Trailing spaces are removed from every line.
     */
    public static List<String> wrapParagraph(String text, int maxWidth, ToIntFunction<String> widthFunction) {
        List<String> lines = new ArrayList<>();

        BreakIterator breaks = BreakIterator.getLineInstance(containsThai(text) ? THAI : Locale.ROOT);
        breaks.setText(text);

        int lineStart = 0;
        int lineEnd = 0;

        for (int end = breaks.next(); end != BreakIterator.DONE; end = breaks.next()) {
            end = toClusterBoundary(text, end);
            if (end <= lineEnd) continue;

            if (widthFunction.applyAsInt(trimEnd(text.substring(lineStart, end))) <= maxWidth) {
                lineEnd = end;
                continue;
            }

            if (lineEnd > lineStart) {
                lines.add(trimEnd(text.substring(lineStart, lineEnd)));
                lineStart = lineEnd;
            }

            // A single word that is too long for a line on its own
            while (widthFunction.applyAsInt(trimEnd(text.substring(lineStart, end))) > maxWidth) {
                int splitAt = findHardBreak(text, lineStart, end, maxWidth, widthFunction);
                lines.add(text.substring(lineStart, splitAt));
                lineStart = splitAt;
            }

            lineEnd = end;
        }

        if (lineStart < text.length()) {
            lines.add(trimEnd(text.substring(lineStart)));
        }

        if (lines.size() > 1 && isRightToLeftParagraph(text)) {
            // Each line is reordered on its own: keep the paragraph's direction on lines that
            // happen to start with a left-to-right word or a number
            lines.replaceAll(line -> RIGHT_TO_LEFT_MARK + line);
        }

        return lines;
    }

    private static boolean isRightToLeftParagraph(String text) {
        char[] chars = text.toCharArray();

        return Bidi.requiresBidi(chars, 0, chars.length)
                && !new Bidi(text, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).baseIsLeftToRight();
    }

    /**
     * Binary search for the furthest cluster boundary in {@code (start, end)} at which
     * {@code text[start, boundary)} still fits. Always advances by at least one cluster.
     */
    private static int findHardBreak(String text, int start, int end, int maxWidth, ToIntFunction<String> widthFunction) {
        int lo = start + 1, hi = end;

        while (lo < hi) {
            int mid = (lo + hi) >>> 1;

            if (widthFunction.applyAsInt(text.substring(start, mid)) > maxWidth) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }

        int splitAt = toClusterBoundary(text, lo - 1);

        if (splitAt <= start) {
            // Not even one cluster fits: put it on its own line anyway
            splitAt = start + 1;

            while (splitAt < end && (continuesCluster(text.charAt(splitAt)) || text.charAt(splitAt - 1) == '§')) {
                splitAt++;
            }
        }

        return splitAt;
    }

    private static boolean containsThai(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= '฀' && c <= '๿') return true;
        }

        return false;
    }

    private static String trimEnd(String text) {
        int end = text.length();

        while (end > 0 && text.charAt(end - 1) == ' ') {
            end--;
        }

        return text.substring(0, end);
    }
}
