package org.example;

public final class Colors {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\033[1;91m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\033[1;94m";
    public static final String ANSI_BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String ANSI_WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
    public static final String ANSI_GREEN_BACKGROUND = "\033[42m";

    private Colors() {
        // None
    }
}
