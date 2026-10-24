package com.marlow.optimiser;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Swaps the money number on the sidebar for your chosen amount, keeping colours and fonts.
 *
 * Matches (on the scoreboard only):
 *   - a "$" followed by a number, anywhere in the line
 *   - an icon/symbol followed by ONLY a number like 265M or 1,234,567 (your "$ 265M" line)
 *   - a bare number that has a K/M/B/T suffix or commas
 * Lines like "107d 20h" (playtime) are never touched.
 */
public final class MoneyRewriter {

    private static final String NUM = "[0-9][0-9,]*(?:\\.[0-9]+)?[KMBTkmbt]?";

    private static final Pattern DOLLAR = Pattern.compile("\\$\\s*(" + NUM + ")(?![A-Za-z0-9])");
    private static final Pattern ICON_THEN_NUMBER = Pattern.compile("^[^A-Za-z0-9]+(" + NUM + ")\\s*$");
    private static final Pattern BARE_NUMBER = Pattern.compile(
            "^\\s*([0-9][0-9,]*(?:\\.[0-9]+)?[KMBTkmbt]|[0-9]{1,3}(?:,[0-9]{3})+)\\s*$");

    private MoneyRewriter() {
    }

    public static Text rewrite(Text in) {
        if (!MarlowsCrystalOptimiser.enabled || !MarlowsCrystalOptimiser.inSidebar || in == null) {
            return in;
        }

        StringBuilder sb = new StringBuilder();
        List<Style> styles = new ArrayList<>();
        in.visit((style, str) -> {
            for (int i = 0; i < str.length(); i++) {
                sb.append(str.charAt(i));
                styles.add(style);
            }
            return Optional.empty();
        }, Style.EMPTY);

        String original = sb.toString();
        if (original.isEmpty()) {
            return in;
        }

        Matcher m = DOLLAR.matcher(original);
        if (!m.find()) {
            m = ICON_THEN_NUMBER.matcher(original);
            if (!m.find()) {
                m = BARE_NUMBER.matcher(original);
                if (!m.find()) {
                    return in;
                }
            }
        }

        int s = m.start(1);
        int e = m.end(1);
        String shown = display(MarlowsCrystalOptimiser.fakeAmount);

        StringBuilder out = new StringBuilder();
        List<Style> outStyles = new ArrayList<>();
        for (int i = 0; i < s; i++) {
            out.append(original.charAt(i));
            outStyles.add(styles.get(i));
        }
        Style numberStyle = styles.get(s);
        for (int i = 0; i < shown.length(); i++) {
            out.append(shown.charAt(i));
            outStyles.add(numberStyle);
        }
        for (int i = e; i < original.length(); i++) {
            out.append(original.charAt(i));
            outStyles.add(styles.get(i));
        }

        // Rebuild the text, grouping runs that share a style.
        MutableText result = Text.empty();
        int start = 0;
        for (int i = 1; i <= out.length(); i++) {
            if (i == out.length() || !outStyles.get(i).equals(outStyles.get(start))) {
                result.append(Text.literal(out.substring(start, i)).setStyle(outStyles.get(start)));
                start = i;
            }
        }
        return result;
    }

    /**
     * 5b -> 5B, 5000000000 -> 5B, 265000000 -> 265M (matches the way the server shows money).
     * Anything else is shown as typed.
     */
    private static String display(String amount) {
        String a = amount.replace("$", "").replace(" ", "").trim();
        if (a.matches("[0-9.,]+[kmbtKMBT]")) {
            return a.substring(0, a.length() - 1) + Character.toUpperCase(a.charAt(a.length() - 1));
        }
        if (a.matches("[0-9]+")) {
            try {
                double v = Double.parseDouble(a);
                if (v >= 1e12) return trim(v / 1e12) + "T";
                if (v >= 1e9) return trim(v / 1e9) + "B";
                if (v >= 1e6) return trim(v / 1e6) + "M";
                if (v >= 1e3) return trim(v / 1e3) + "K";
            } catch (NumberFormatException ignored) {
            }
        }
        return a;
    }

    private static String trim(double v) {
        String s = String.format(java.util.Locale.ROOT, "%.2f", v);
        s = s.replaceAll("0+$", "").replaceAll("\\.$", "");
        return s;
    }
}
