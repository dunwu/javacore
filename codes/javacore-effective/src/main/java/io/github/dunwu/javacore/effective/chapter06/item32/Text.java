// EnumSet - a modern replacement for bit fields - Page 160
package io.github.dunwu.javacore.effective.chapter06.item32;

import java.util.EnumSet;
import java.util.Set;

public class Text {

    // Sample use
    public static void main(String[] args) {
        Text text = new Text();
        text.applyStyles(EnumSet.of(Style.BOLD, Style.ITALIC));
    }

    // Any Set could be passed in, but EnumSet is clearly best
    public void applyStyles(Set<Style> styles) {
        // Body goes here
    }

    public enum Style {

        BOLD,
        ITALIC,
        UNDERLINE,
        STRIKETHROUGH
    }

}
