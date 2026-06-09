package chess.ui.renderer;

import java.awt.Color;

/**
 * Central definition of all UI colors.
 * Changing a theme only requires editing this one class.
 */
public class ColorTheme {

    private ColorTheme() {}

    // Board squares
    public static final Color LIGHT_SQUARE      = new Color(240, 217, 181);
    public static final Color DARK_SQUARE       = new Color(181, 136,  99);

    // Highlighting
    public static final Color SELECTED          = new Color( 20, 85,  30, 180);
    public static final Color LEGAL_MOVE_DOT    = new Color( 20, 85,  30, 120);
    public static final Color LEGAL_CAPTURE     = new Color(220,  40,  40, 140);
    public static final Color LAST_MOVE         = new Color(205, 210,  15, 140);
    public static final Color CHECK_HIGHLIGHT   = new Color(220,  10,  10, 160);

    // UI chrome
    public static final Color RANK_FILE_LABEL   = new Color(120,  80,  50);
    public static final Color PANEL_BACKGROUND  = new Color( 40,  40,  45);
    public static final Color BUTTON_BACKGROUND = new Color( 60,  60,  65);
    public static final Color BUTTON_HOVER      = new Color( 80,  80,  90);
    public static final Color BUTTON_TEXT       = Color.WHITE;
    public static final Color STATUS_TEXT       = new Color(230, 230, 230);
    public static final Color THINKING_COLOR    = new Color(255, 200,  50);
}
